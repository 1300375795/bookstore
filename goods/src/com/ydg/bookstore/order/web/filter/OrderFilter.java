package com.ydg.bookstore.order.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ydg.bookstore.user.domain.User;

/**
 * Servlet Filter implementation class OrderFilter
 */
@WebFilter(urlPatterns = { "/jsps/order/*" }, servletNames = { "OrderServlet" })
public class OrderFilter implements Filter {

	public void destroy() {
	}

	/**
	 * 过滤全部的订单jsp页面以及订单OrderServlet只有登录了才能
	 * 1.将req请求转换成HttpServletRequest请求，然后得到session
	 * 2.得到session中的登录用户
	 * 3.判断用户是否登录，登录了放行否则保存错误信息到msg.jsp页面
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("sessionUser");
		if (user != null) {
			chain.doFilter(req, response);
		} else {
			req.setAttribute("msg", "请先登录在访问当前页面");
			req.setAttribute("code", "error");
			req.getRequestDispatcher("/jsps/msg.jsp").forward(req, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
