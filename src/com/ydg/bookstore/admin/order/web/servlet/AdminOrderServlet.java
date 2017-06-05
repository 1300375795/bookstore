package com.ydg.bookstore.admin.order.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

import com.ydg.bookstore.order.domain.Order;
import com.ydg.bookstore.order.service.OrderService;
import com.ydg.bookstore.peger.PageBean;
import com.ydg.bookstore.user.domain.User;

public class AdminOrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	
	/***
	 * 查询全部订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String listAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPc(req);
		String url = getUrl(req);
		PageBean<Order> pb = orderService.listAll(pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);

		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	/**
	 * 通过订单的状态 查询订单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String listOrderByStatus(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		int pc = getPc(req);
		String url = getUrl(req);
		int status = Integer.parseInt(req.getParameter("status"));
		PageBean<Order> pb = orderService.listOrderByStatus(status, pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);

		return "f:/adminjsps/admin/order/list.jsp";
	}

	/**
	 * 查询一个订单
	 * 1.得到oid参数
	 * 2.得到btn参数，用于给页面返回以提示页面需要显示什么样的按钮
	 * 3.执行service层的代码得到相应的一个order对象
	 * 4.保存order和btn到request中
	 * 5.返回相应的页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		String btn = req.getParameter("btn");
		Order order = orderService.load(oid);
		req.setAttribute("btn", btn);
		req.setAttribute("order", order);
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	
	/**
	 * 取消订单
	 * 1.得到oid
	 * 2.校验状态是否正确
	 * 3.返回信息
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String cancel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		int status = orderService.findStatus(oid);
		if (status != 1) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "不正确的状态，不能取消订单");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "订单已经成功取消");
		return "f:/adminjsps/msg.jsp";
	}

	/**
	 * 确认收货
	 * 1.得打oid
	 * 2.校验状态正确
	 * 3.返回信息
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deliver(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		int status = orderService.findStatus(oid);
		if (status != 2) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "不正确的状态不能发货");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 3);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "订单已经成功发货");
		return "f:/adminjsps/msg.jsp";
	}
	
	/**
	 * 得到url的方法
	 * 1.得到传递的url的前面的部分加上一个？然后加上后面的参数部分
	 * 2.获取&pc=这个字符的下标
	 * 3.如果下标不为-1那么就截取掉包含的这个字符
	 * 4.返回url
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURI() + "?" + req.getQueryString();
		int index = url.lastIndexOf("&pc=");
		if (index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}

	/**
	 * 得到pc参数
	 * 1.设置默认的pc为1
	 * 2.在req中得到pc这个参数
	 * 3.如果不为null或者不为空的话那么就将得到的pc的值设置给pc
	 * 4.否则返回默认的pc的值
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if (param != null && !param.trim().isEmpty()) {
			pc = Integer.parseInt(param);
		}
		return pc;
	}

}
