package com.ydg.bookstore.admin.admin.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import cn.itcast.jdbc.TxQueryRunner;

import com.ydg.bookstore.admin.admin.domian.Admin;

/**
 * 后台数据库持久层
 * @author admin
 *
 */
public class AdminDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 登录功能
	 * 1.给出SQL语句（通过管理员名称和密码）
	 * 2.查询数据库得到相应的管理员
	 * 3.返回这个管理员
	 * @param form
	 * @return
	 * @throws SQLException
	 */
	public Admin findByAdminnameAndAdminpwd(Admin formAdmin)
			throws SQLException {
		String sql = "SELECT * FROM t_admin WHERE adminname=? AND adminpwd=?";

		Admin admin = qr.query(sql, new BeanHandler<Admin>(Admin.class),
				formAdmin.getAdminname(), formAdmin.getAdminpwd());
		return admin;
	}
}
