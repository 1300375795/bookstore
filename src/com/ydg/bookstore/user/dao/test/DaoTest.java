package com.ydg.bookstore.user.dao.test;

import java.sql.SQLException;

import org.junit.Test;

import com.ydg.bookstore.user.dao.UserDao;

public class DaoTest {
	private UserDao userDao = new UserDao();

	@Test
	public void updateLoginpassTest() {
		String uid = "55790D9C1A1845738E6D93866A148C7E";
		String loginpass = "971810252";
		try {
			userDao.updateLoginpass(uid, loginpass);
		} catch (SQLException e) {
			System.out.println(e.getMessage()	);
		}
	}
}
