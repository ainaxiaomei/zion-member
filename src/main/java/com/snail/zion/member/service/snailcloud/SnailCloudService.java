package com.snail.zion.member.service.snailcloud;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Formatter;
import java.util.concurrent.CompletionStage;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.rx.rxjava2.RxFlowableInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.reactivex.Flowable;

@Service
public class SnailCloudService implements ISnailCloudService {
	
	private static Logger logger = LoggerFactory.getLogger(SnailCloudService.class);

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	@Autowired
	private SnailCloudConfig snailCloudConfig;
	
	@Autowired
	private Client client;

	private String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
		String res = formatter.toString();
		formatter.close();
		return res;
	}

	private String calculateRFC2104HMAC(String data, String key)
			throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}

	private String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = Base64.getEncoder().encodeToString(b);
		}
		return s;
	}

	private MultivaluedHashMap<String, Object> generateHeaders(String body, String method, String requestPath,
			String secretKey) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {

		MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<String, Object>();

		String nonce = String.valueOf(System.currentTimeMillis());
		headers.add("X-woniu-cloud-secretkey", secretKey);
		headers.add("X-woniu-cloud-nonce", nonce);
		headers.add("X-woniu-cloud-timestamp", nonce);

		StringBuilder builder = new StringBuilder();
		builder.append("body=").append(body).append("&method=").append(method).append("&uri=").append(requestPath)
				.append("&X-woniu-cloud-secretkey=").append(secretKey).append("&X-woniu-cloud-nonce=").append(nonce) // 每次请求数字都是增长不能重复
				.append("&X-woniu-cloud-timestamp=").append(nonce);
		String hmacSHA1 = calculateRFC2104HMAC(builder.toString(), secretKey);
		headers.add("X-woniu-cloud-signature", getBase64(hmacSHA1));
		return headers;
	}

	@Override
	public Flowable<String> getPushPlayUrl(String secretKey, String tcode, int expiredHours, String streamName,
			String recordParam)
			throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException {

		String path = "/v1/snailcloud/ppurl/application";
		String body = "";

		MultivaluedHashMap<String, Object> headers = generateHeaders(body, "GET", path, secretKey);
		
		WebTarget target = client.target(snailCloudConfig.httpUrl()).path(path);

		if (tcode != null && tcode.trim().length() > 0) {
			target.queryParam("tcode", URLEncoder.encode(tcode, "utf-8"));
		}
		
		if (recordParam != null && recordParam.trim().length() > 0) {
			target.queryParam("recordParam", recordParam);
		}
		
		if (expiredHours > 0) {
			target.queryParam("expiredHours", expiredHours);
		}

		if (streamName != null && streamName.trim().length() > 0) {
			target.queryParam("streamName", streamName);
		}
		
		if(logger.isInfoEnabled()){
			logger.info(String.format("request header : %s" , headers));
		}

		Flowable<String> res = target.request()
				.headers(headers)
				.rx(RxFlowableInvoker.class)
				.get(String.class);

		return res;

	}
}
