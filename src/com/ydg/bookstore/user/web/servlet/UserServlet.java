package com.ydg.bookstore.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import com.ydg.bookstore.user.domain.User;
import com.ydg.bookstore.user.service.UserService;
import com.ydg.bookstore.user.service.exception.UserException;

public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();

	/**
	 * 将用户传递的参数进行相应的校验对的话返回正确是信息错误的话返回错误信息 1.封装用户传递的信息
	 * 2.进项相应的校验若是存在错误的话则将错误信息保存在一个map中然后将map存在request中返回给客户端 3.调用service的方法注册用户
	 * 4.返回相应的注册成功信息
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {

			// 封装参数
			User formUser = CommonUtils.toBean(req.getParameterMap(),
					User.class);
			// 判断传递来的参数是否正确，额若是不正确的话转发会客户端
			Map<String, String> errors = validateRegist(formUser,
					req.getSession());
			if (errors.size() > 0) {
				req.setAttribute("errors", errors);
				req.setAttribute("formUser", formUser);
				return "f:/jsps/user/regist.jsp";
			}

			// 如果参数都正确的话那么就调用service的方法添加到数据库中
			userService.regist(formUser);

			// 将注册成功的信息转发到信息板
			req.setAttribute("code", "success");
			req.setAttribute("msg", "注册成功，请到邮箱激活账号");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}

	/**
	 * 具体的校验方法，用来判断给出的几个参数是否正确
	 * 
	 * @param formUser
	 * @param session
	 * @return
	 */
	private Map<String, String> validateRegist(User formUser,
			HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		// 用户名判断
		String loginname = formUser.getLoginname();
		if (loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空");
		} else if (loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在3-20之间");
		} else if (!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "用户名已经被注册");
		}

		// 密码判断
		String loginpass = formUser.getLoginpass();
		if (loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空");
		} else if (loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3-20之间");
		}

		// 再次输入的密码判断
		String reLoginpass = formUser.getReloginpass();
		if (reLoginpass == null || reLoginpass.trim().isEmpty()) {
			errors.put("reloginpass", "再次输入的密码不能为空");
		} else if (!reLoginpass.equals(loginpass)) {
			errors.put("reloginpass", "两次输入的密码不同请重新输入");
		}

		// 邮件判断
		String email = formUser.getEmail();
		if (email == null || email.trim().isEmpty()) {
			errors.put("email", "email不能为空");
		} else if (!email
				.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "email格式不正确");
		} else if (!userService.ajaxValidateEmail(email)) {
			errors.put("email", "当前email被注册，请重新输入");
		}

		// 验证码判断
		String verifyCode = formUser.getVerifyCode();
		String vCode = (String) session.getAttribute("vCode");
		if (verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空");
		} else if (!verifyCode.equalsIgnoreCase(vCode)) {
			errors.put("verifyCode", "验证码错误");
		}

		return errors;
	}

	/**
	 * 通过传递来的名为loginname的参数调用相关的方法判断是否被注册了 若是返回的是false则表示被注册了，true则表示没被注册
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public String ajaxValidateLoginname(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String loginname = req.getParameter("loginname");
		boolean b = userService.ajaxValidateLoginname(loginname);
		resp.getWriter().print(b);
		return null;
	}

	public String ajaxValidateLoginpass(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String loginpass = req.getParameter("loginpass");
		boolean b = userService.ajaxValidateLoginpass(loginpass);
		resp.getWriter().print(b);
		return null;
	}

	/**
	 * 通过传递来的参数email调用相关的方法判断这个email是否被注册了 若是被注册了的话那么返回false没被注册的话返回true
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public String ajaxValidateEmail(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		boolean b = userService.ajaxValidateEmail(email);
		resp.getWriter().print(b);
		return null;
	}

	/**
	 * 将客户端传递来的verifyCode跟通过VerifyCodeServlet存在session域中的
	 * 值进行比较忽略大小写比较然后返回若是不相等的话返回false相等的话返回true
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String verifyCode = req.getParameter("verifyCode");
		String vCode = (String) req.getSession().getAttribute("vCode");
		boolean b = vCode.equalsIgnoreCase(verifyCode);
		resp.getWriter().print(b);
		return null;

	}

	/**
	 * 获取用户点击超链接给出activationCode参数然后通过这个参数查询数据库若是存在的没有抛出异常的话
	 * 那么就给显示板一个正确的信息，若是抛出了异常的话那么就捕获这个异常然后给显示板一个错误的信息
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String activation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String activationCode = req.getParameter("activationCode");
		try {
			userService.activation(activationCode);
			req.setAttribute("code", "success");
			req.setAttribute("msg", "激活成功您可以登录了");
		} catch (UserException e) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", e.getMessage());
		}
		return "f:/jsps/msg.jsp";
	}

	/***
	 * 将用户输入的信息封装，然后校验之，再访问service得到相应的User对象，
	 * 如果返回的为null则表示没有该用户，如果有返回但是激活的信息为false给
	 * 出错误信息，如果都正常的话那么保存查出来的用户信息到session中并且给出 一个cookie保存用户名到cookie中10天
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 封装用户数据
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		// 校验用户数据
		Map errors = validateLogin(formUser, req.getSession());
		if (errors.size() > 0) {
			req.setAttribute("errors", errors);
			req.setAttribute("formUser", formUser);
			return "f:/jsps/user/login.jsp";
		}
		// 判断用户账号密码正确与否
		User user = userService.login(formUser);
		if (user == null) {// 没查到说明没有这个用户
			req.setAttribute("formUser", formUser);
			req.setAttribute("msg", "用户名或者密码错误");
			return "f:/jsps/user/login.jsp";
		} else if (!user.isStatus()) {// 查到了但是没激活
			req.setAttribute("formUser", formUser);
			req.setAttribute("msg", "您还未激活请先进行激活");
			return "f:/jsps/user/login.jsp";
		} else {// 用户存在登陆成功
			// 保存在session中
			req.getSession().setAttribute("sessionUser", user);
			String loginname = user.getLoginname();// 得到用户名
			// 将用户名转换成http格式就不会存在中文，cookie、不可以是中文的
			String cookieLoginname = URLEncoder.encode(loginname, "utf-8");
			// 得到cookie
			Cookie cookie = new Cookie("loginname", cookieLoginname);
			cookie.setMaxAge(60 * 60 * 24 * 10);// 保存十天
			resp.addCookie(cookie);// 添加cookie
			return "r:/index.jsp";// 重定向到index.jsp中
		}

	}

	/**
	 * 登录账号和密码的格式校验
	 * 
	 * @param formUser
	 * @param session
	 * @return
	 */
	private Map<String, String> validateLogin(User formUser, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		String loginname = formUser.getLoginname();
		if (loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空");
		} else if (loginname.length() < 3 || loginname.length() > 20) {
			errors.put("login", "用户名必须在3-20之间");
		}

		String loginpass = formUser.getLoginpass();
		if (loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空");
		} else if (loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度需要在3-20之间");
		}

		String verifyCode = formUser.getVerifyCode();
		String vCode = (String) session.getAttribute("vCode");
		if (verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空");
		} else if (!verifyCode.equalsIgnoreCase(vCode)) {
			errors.put("verifyCode", "验证码输入错误");
		}
		return errors;
	}

	/**
	 * 修改密码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateLoginpass(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		// 封装用户提交的信息
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);

		// 得到错误信息，并转发
		Map errors = validateLoginpass(formUser, req.getSession());
		if (errors.size() > 0) {
			req.setAttribute("formUser", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/pwd.jsp";
		}

		User user = (User) req.getSession().getAttribute("sessionUser");
		try {
			userService.updateLoginpass(user.getUid(), formUser.getLoginpass(),
					formUser.getNewloginpass());
			req.getSession().invalidate();// 直接销毁session这样的话也就退出了
			req.setAttribute("code", "success");
			req.setAttribute("msg", "修改成功请重新登录");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			// 保存错误信息
			req.setAttribute("msg", e.getMessage());
			// 回显填写的信息
			req.setAttribute("formUser", formUser);
			return "f:/jsps/user/pwd.jsp";
		}
	}

	/**
	 * 校验修改密码页面
	 * 
	 * @param formUser
	 * @param session
	 * @return
	 */
	private Map<String, String> validateLoginpass(User formUser,
			HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();

		String oldLoginpass = formUser.getLoginpass();
		if (oldLoginpass == null || oldLoginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空");
		} else if (oldLoginpass.length() < 3 || oldLoginpass.length() > 20) {
			errors.put("loginpass", "密码长度需要在3-20之间");
		}

		String newloginpass = formUser.getNewloginpass();
		if (newloginpass == null || newloginpass.trim().isEmpty()) {
			errors.put("newloginpass", "新密码不能为空");
		} else if (newloginpass.length() < 3 || newloginpass.length() > 20) {
			errors.put("newloginpass", "新密码长度需要在3-20之间");
		}

		String reLoginpass = formUser.getReloginpass();
		if (reLoginpass == null || reLoginpass.trim().isEmpty()) {
			errors.put("reloginpass", "再次输入密码不能为空");
		} else if (!reLoginpass.equals(newloginpass)) {
			errors.put("reloginpass", "两次输入的密码不一样");
		}

		String verifyCode = formUser.getVerifyCode();
		String vCode = (String) session.getAttribute("vCode");
		if (verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空");
		} else if (!verifyCode.equalsIgnoreCase(vCode)) {
			errors.put("verifyCode", "验证码输入错误");
		}
		return errors;
	}
	/**
	 * 退出登录
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String quit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getSession().invalidate();//直接销毁session专业那个也就退出了
		//这个也可以退出登录不过比起上面这个稍微麻烦了点
		//req.getSession().removeAttribute("sessionUser");
		return "r:/jsps/user/login.jsp";
	}
}
