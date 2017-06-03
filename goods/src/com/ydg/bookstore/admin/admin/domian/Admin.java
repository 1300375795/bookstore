package com.ydg.bookstore.admin.admin.domian;

/**
 * 后台管理员实体类
 * @author admin
 *
 */
public class Admin {
	private String adminId;// 管理员id
	private String adminname;// 管理员登录名
	private String adminpwd;// 管理员登录密码

	@Override
	public String toString() {
		return "Admin [adminId=" + adminId + ", adminname=" + adminname
				+ ", adminpwd=" + adminpwd + "]";
	}

	public Admin() {
		super();
	}

	public Admin(String adminId, String adminname, String adminpwd) {
		super();
		this.adminId = adminId;
		this.adminname = adminname;
		this.adminpwd = adminpwd;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getAdminname() {
		return adminname;
	}

	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}

	public String getAdminpwd() {
		return adminpwd;
	}

	public void setAdminpwd(String adminpwd) {
		this.adminpwd = adminpwd;
	}

}
