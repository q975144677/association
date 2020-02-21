package com.association.admin.controller.chrome;

import java.util.Date;

public class ChromeBrowserHistory {
	
	//编号
	private Integer id;
	//标题
	private String title;
	//url地址
	private String url;
	//访问时间
	private Date visitTime;
	//停留时间
	private long visitDuration;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}

	public long getVisitDuration() {
		return visitDuration;
	}

	public void setVisitDuration(long visitDuration) {
		this.visitDuration = visitDuration;
	}

	@Override
	public String toString() {
		return "ChromeBrowserHistory [id=" + id + ", title=" + title + ", url=" + url + ", visitTime=" + visitTime
				+ ", visitDuration=" + visitDuration + "]\n";
	}

	public ChromeBrowserHistory(Integer id, String title, String url, Date visitTime, long visitDuration) {
		super();
		this.id = id;
		this.title = title;
		this.url = url;
		this.visitTime = visitTime;
		this.visitDuration = visitDuration;
	}

	public ChromeBrowserHistory() {
		super();
	}
}