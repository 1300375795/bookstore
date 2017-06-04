package com.ydg.bookstore.admin.admin.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import com.ydg.bookstore.admin.admin.domian.Admin;
import com.ydg.bookstore.admin.admin.service.AdminService;

public class AdminServlet extends BaseServlet {
	private AdminService adminService = new AdminService();

	/**
	 * 后台登录功能的实现方法
	 * 1.封装表单传递的参数
	 * 2.通过表单数据查询数据库得到相应的admin
	 * 3.判断查询出的admin是否为null
	 * 4.是的话说明没有这个admin保存错误信息
	 * 5.否则保存admin到session和cookie中
	 * 6.转发到主页
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Admin formAdmin = CommonUtils
				.toBean(req.getParameterMap(), Admin.class);
		Admin admin = adminService.login(formAdmin);
		if (admin == null) {
			req.setAttribute("msg", "无效的管理员账号，请输入正确是账号或密码");
			return "f:/adminjsps/login.jsp";
		}
		String adminname = URLEncoder.encode(admin.getAdminname(), "utf-8");
		Cookie cookie = new Cookie("adminname", adminname);
		cookie.setMaxAge(60 * 60 * 24 * 10);
		resp.addCookie(cookie);
		req.getSession().setAttribute("sessionAdmin", admin);
		return "r:/adminjsps/admin/index.jsp";
	}
}
