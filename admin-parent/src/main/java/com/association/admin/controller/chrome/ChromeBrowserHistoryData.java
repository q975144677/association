package com.association.admin.controller.chrome;

public class ChromeBrowserHistoryData {
	
	//url地址
	private String url;
	//标题
	private String title;
	//访问次数
	private int visitCount;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public ChromeBrowserHistoryData(String url, String title, int visitCount) {
		super();
		this.url = url;
		this.title = title;
		this.visitCount = visitCount;
	}

	public ChromeBrowserHistoryData() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ChromeBrowserHistoryData [url=" + url + ", title=" + title + ", visitCount=" + visitCount + "]";
	}
}