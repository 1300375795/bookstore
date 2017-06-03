package com.ydg.bookstore.user.domain;

public class User {
	private String uid;
	private String loginname;
	private String loginpass;
	private String reloginpass;
	private String newloginpass;
	private String email;
	private String verifyCode;
	private boolean status;
	private String activationCode;

	@Override
	public String toString() {
		return "User [uid=" + uid + ", loginname=" + loginname + ", loginpass="
				+ loginpass + ", reloginpass=" + reloginpass
				+ ", newloginpass=" + newloginpass + ", email=" + email
				+ ", verifyCode=" + verifyCode + ", status=" + status
				+ ", activationCode=" + activationCode + "]";
	}

	public User() {
		super();
	}

	public User(String uid, String loginname, String loginpass,
			String reloginpass, String newloginpass, String email,
			String verifyCode, boolean status, String activationCode) {
		super();
		this.uid = uid;
		this.loginname = loginname;
		this.loginpass = loginpass;
		this.reloginpass = reloginpass;
		this.newloginpass = newloginpass;
		this.email = email;
		this.verifyCode = verifyCode;
		this.status = status;
		this.activationCode = activationCode;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getLoginpass() {
		return loginpass;
	}

	public String getNewloginpass() {
		return newloginpass;
	}

	public void setNewloginpass(String newloginpass) {
		this.newloginpass = newloginpass;
	}

	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}

	public String getReloginpass() {
		return reloginpass;
	}

	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

}
