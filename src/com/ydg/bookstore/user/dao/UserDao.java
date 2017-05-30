package com.ydg.bookstore.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.jdbc.TxQueryRunner;

import com.ydg.bookstore.user.domain.User;

public class UserDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 这个方法是判断客户端注册所需的用户名是否已经被注册的 1.给出sql语句 2.查询给吃的用户名是否有记录
	 * 3.若是等于0那么说明没有记录返回true否则的话返回false
	 * 
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLoginname(String loginname) {
		String sql = "SELECT COUNT(1) FROM t_user WHERE loginname=?";
		Number number = null;
		try {
			number = (Number) qr.query(sql, new ScalarHandler(), loginname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return number.intValue() == 0;
	}

	/**
	 * 通过用户填写的email判断是否这个email语句被别人使用过了 1.给出SQL语句 2.查询数据库是否已经存在了这个email
	 * 3.如果查出来的记录为0那么表示这个email没有被使用放回true否则返回false
	 * 
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email) {
		String sql = "SELECT COUNT(1) FROM t_user WHERE email=?";
		Number number = null;
		try {
			number = (Number) qr.query(sql, new ScalarHandler(), email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return number.intValue() == 0;
	}

	/**
	 * 将注册的用户添加到数据库中 1.给出6个参数的SQL语句 2.通过传递来的user得到相应的参数 3.执行update方法添加到数据库
	 * 
	 * @param user
	 */
	public void add(User user) {
		String sql = "INSERT INTO t_user value(?,?,?,?,?,?)";
		Object[] params = { user.getUid(), user.getLoginname(),
				user.getLoginpass(), user.getEmail(), user.isStatus(),
				user.getActivationCode() };
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过给出的activationCode查询相应的用户是否存在
	 * 
	 * @param activationCode
	 * @return
	 * @throws SQLException
	 */
	public User findByActivationCode(String activationCode) throws SQLException {
		String sql = "SELECT * FROM t_user WHERE activationCode=?";
		return qr.query(sql, new BeanHandler<User>(User.class), activationCode);
	}

	/**
	 * 通过uid将某个用户的status改变
	 * 
	 * @param uid
	 * @param status
	 * @throws SQLException
	 */
	public void updateStatus(String uid, boolean status) throws SQLException {
		String sql = "UPDATE t_user SET status=? WHERE uid=? ";
		Object[] params = { status, uid };
		qr.update(sql, params);
	}

	/**
	 * 通过用户的用户名和密码查询相应的User并返回
	 * 
	 * @param login
	 * @param loginpass
	 * @return
	 */
	public User findByLoginnameAndLoginpass(String loginname, String loginpass) {
		String sql = "SELECT * FROM t_user WHERE loginname=? and loginpass=?";
		Object[] params = { loginname, loginpass };
		try {
			return qr.query(sql, new BeanHandler<User>(User.class), params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过用户的uid以及密码查询数据库是否存在这个用户，是的话返回true
	 * 
	 * @param uid
	 * @param loginpass
	 * @return
	 * @throws SQLException
	 */
	public boolean findByUidAndLoginpass(String uid, String oldloginpass)
			throws SQLException {

		String sql = "SELECT COUNT(*) FROM t_user WHERE uid=? and loginpass=? ";
		Object[] params = { uid, oldloginpass };
		Number number = (Number) qr.query(sql, new ScalarHandler(), params);
		int i = number.intValue();
		return i > 0;
	}

	/**
	 * 通过用户的uid以及新的loginpass修改密码
	 * 
	 * @param uid
	 * @param loginpass
	 * @throws SQLException
	 */
	public void updateLoginpass(String uid, String newloginpass)
			throws SQLException {
		System.out.println(uid);
		System.out.println(newloginpass);
		String sql = "UPDATE t_user SET loginpass=? WHERE uid=?";
		Object[] params = { newloginpass, uid };
		qr.update(sql, params);
	}

	public boolean ajaxValidateLoginpass(String loginpass) {
		String sql = "SELECT * FROM t_user WHERE loginpass=?";
		boolean bool = true;
		try {
			User user = qr.query(sql, new BeanHandler<User>(User.class),
					loginpass);
			if (user == null) {
				bool = false;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return bool;
	}
}
