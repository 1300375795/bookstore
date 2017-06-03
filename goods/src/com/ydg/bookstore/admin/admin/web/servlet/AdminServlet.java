package com.ydg.bookstore.admin.admin.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		req.getSession().setAttribute("sessionAdmin", admin);
		return "r:/adminjsps/admin/index.jsp";
	}
}
