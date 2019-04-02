package com.djcps.library.model;



import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author djsxs
 *
 */
public class User {
	private Integer userId;
	private String userName;
	@JsonIgnore
	private String userPwd;
	private String userPhone;
	private Integer isAllowBorrow;
	private Double score;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public Integer getIsAllowBorrow() {
		return isAllowBorrow;
	}
	public void setIsAllowBorrow(Integer isAllowBorrow) {
		this.isAllowBorrow = isAllowBorrow;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userPwd=" + userPwd + ", userPhone=" + userPhone
				+ ", isAllowBorrow=" + isAllowBorrow + ", score=" + score + "]";
	}
	
	
	
}
