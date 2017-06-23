package com.wwj.config.batchJob.domain;

import java.io.Serializable;

/*
* 缴费通知
* */
public class Message implements Serializable{
    private String id;
	private User user;
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {return user;}
	public void setUser(User user) {this.user = user;}
	public String getContent() {return content;}
	public void setContent(String content) {this.content = content;}

	@Override
	public String toString() {
		return "Message{" +
				"id='" + id + '\'' +
				", user=" + user +
				", content='" + content + '\'' +
				'}';
	}
}
