package com.ydg.bookstore.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

import com.ydg.bookstore.user.dao.UserDao;
import com.ydg.bookstore.user.domain.User;
import com.ydg.bookstore.user.service.exception.UserException;

public class UserService {
	private UserDao userDao = new UserDao();

	/**
	 * 通过给出的用户名查询数据库若是已经被注册了返回false没有被注册的话返回true
	 * 
	 * @param loginname
	 * @return
	 */
	public boolean ajaxValidateLoginname(String loginname) {
		return userDao.ajaxValidateLoginname(loginname);
	}

	/**
	 * 通过email查询数据库若是已经被注册了返回false没被注册的话返回true
	 * 
	 * @param email
	 * @return
	 */
	public boolean ajaxValidateEmail(String email) {
		return userDao.ajaxValidateEmail(email);

	}

	/**
	 * 用户服务层，向已经封装了部分信息的user中继续封装相应的其他信息，调用dao层的添加用户的方法 然后发送激活邮件
	 * 
	 * @param user
	 */
	public void regist(User user) {
		user.setUid(CommonUtils.uuid());// 设置uid
		user.setStatus(false);// 设置是否激活
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());// 设置激活码

		userDao.add(user);

		Properties pro = new Properties();
		try {// 下面的为得到email相关的配置文件
			pro.load(this.getClass().getClassLoader()
					.getResourceAsStream("email_template.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String host = pro.getProperty("host");// 得到主机信息
		String username = pro.getProperty("username");// 得到用户信息
		String password = pro.getProperty("password");// 得到password
		String from = pro.getProperty("from");// 得到发件人
		String to = user.getEmail();// 得到收件人
		String subject = pro.getProperty("subject");// 得到主题
		String content = MessageFormat.format(pro.getProperty("content"),
				user.getActivationCode());// 通过messageFormat相应的方法处理掉占位符
		// 得到session对象
		Session session = MailUtils.createSession(host, username, password);
		// 得到mail对象
		Mail mail = new Mail(from, to, subject, content);
		try {
			MailUtils.send(session, mail);// 发送邮件
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过给出的activationCode查询相应的用户若是查出类为null那么就抛出异常让用户知道是无效的激活码
	 * 查出来存在但是status已经为true的话那么也抛出异常提示用户已经激活过了
	 * 
	 * @param activationCode
	 * @throws UserException
	 */
	public void activation(String activationCode) throws UserException {
		try {
			User user = userDao.findByActivationCode(activationCode);
			if (user == null) {
				throw new UserException("无效的激活码");
			}
			if (user.isStatus()) {
				throw new UserException("您已经激活过了请勿重复激活");
			}
			userDao.updateStatus(user.getUid(), true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 登录页面逻辑层，参数为用户传递的信息的封装User，返回数据库中的用户的全部的信息
	 * 
	 * @param formUser
	 * @return
	 */
	public User login(User formUser) {
		return userDao.findByLoginnameAndLoginpass(formUser.getLoginname(),
				formUser.getLoginpass());
	}

	/***
	 * 通过用户填写的一些信息查询相应的数据若是没有找到的话那么就抛出异常 若是找到的话那么就修改调用相应的方法修改相应的数据
	 * 
	 * @param formUser
	 * @throws UserException
	 */
	public void updateLoginpass(String uid, String oldloginpass,
			String newloginpass) throws UserException {
		try {
			// 查询数据库相关的uid和密码是否存在
			boolean bool = userDao.findByUidAndLoginpass(uid, oldloginpass);
			if (!bool) {
				// 不存在的话那么就抛出异常
				throw new UserException("您输入的原密码有误请重新输入");
			}
			// 存在的话那么就修改原先的密码
			userDao.updateLoginpass(uid, newloginpass);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean ajaxValidateLoginpass(String loginpass) {

		return userDao.ajaxValidateLoginpass(loginpass);
	}
}
