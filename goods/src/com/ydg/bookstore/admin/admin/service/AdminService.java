package com.ydg.bookstore.admin.admin.service;

import java.sql.SQLException;

import com.ydg.bookstore.admin.admin.dao.AdminDao;
import com.ydg.bookstore.admin.admin.domian.Admin;

/**
 * 后台管理业务逻辑层
 * @author admin
 *
 */

/**
 * 登录功能的service实现
 * 1.通过填写的管理员信息查询数据库得到相应的admin
 * @author ydg
 *
 */
public class AdminService {
	private AdminDao adminDao = new AdminDao();

	public Admin login(Admin formAdmin) {
		try {
			return adminDao.findByAdminnameAndAdminpwd(formAdmin);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
