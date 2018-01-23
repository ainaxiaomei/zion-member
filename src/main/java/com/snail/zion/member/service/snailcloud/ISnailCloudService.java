package com.snail.zion.member.service.snailcloud;

import io.reactivex.Flowable;

public interface ISnailCloudService {
	
	/**
	 * 从云平台获取播放及推流地址
	 * @param secretKey 
	 * @param tcode JSON格式转码参数，使用URLEncode编码
	 * @param expiredHours 多少小时后地址过期
	 * @param streamName 户可以自定义推流名，不填写系统默认生成
	 * @param recordParam 录制参数,参数都是可选
	 * @return
	 */
	public Flowable<String> getPushPlayUrl(String secretKey,String tcode,int expiredHours,String streamName,String recordParam) throws Exception;
}
