package com.snail.zion.member.web.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY) 
public class Data {
	
	private int total;
	
	private int pageSize;
	
	private int  pageNum;
	
	@JsonInclude(JsonInclude.Include.ALWAYS) 
	private List<?> records;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public List<?> getRecords() {
		return records;
	}

	public void setRecords(List<?> records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "Data [total=" + total + ", pageSize=" + pageSize + ", pageNum=" + pageNum + ", records=" + records
				+ "]";
	}
	
	
	
	
	
}
