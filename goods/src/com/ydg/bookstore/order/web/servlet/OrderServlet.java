package com.ydg.bookstore.order.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import com.ydg.bookstore.cart.domain.CartItem;
import com.ydg.bookstore.cart.service.CartItemService;
import com.ydg.bookstore.order.domain.Order;
import com.ydg.bookstore.order.domain.OrderItem;
import com.ydg.bookstore.order.service.OrderService;
import com.ydg.bookstore.peger.PageBean;
import com.ydg.bookstore.user.domain.User;

public class OrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	private CartItemService cartItemService = new CartItemService();

	/**
	 * 我的订单的servlet层的实现
	 * 1.得到pc
	 * 2.得到url
	 * 3.得到session中的User
	 * 4.通过user的uid以及pc得到pageBean
	 * 5.给pageBean设置url
	 * 6.保存pageBean到req中
	 * 7.转发到相关页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrders(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPc(req);
		String url = getUrl(req);
		User owner = (User) req.getSession().getAttribute("sessionUser");
		PageBean<Order> pb = orderService.myOrders(owner.getUid(), pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);

		return "f:/jsps/order/list.jsp";
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

	/**
	 * 创建订单功能的servlet层实现
	 * 1.得到所有的购物车条目用来创建订单条目用
	 * 2.创建一个order对象，给里面的属性相继的赋值
	 * 3.创建一个订单条目对象，通过购物车条目里面的值赋值到相应的属性中
	 * 4.删除购物车中用来生成订单的相关购物车条目
	 * 5.保存订单对象然后转发到相应的页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String createOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cartItemids = req.getParameter("cartItemIds");
		List<CartItem> cartItemList = cartItemService
				.listCartItems(cartItemids);

		Order order = new Order();// 创建一个order对象
		order.setOid(CommonUtils.uuid());// 给order对象的主键赋值
		order.setStatus(1);// 给order对象的状态赋值
		order.setOrdertime(String.format("%tF %<tT", new Date()));// 赋值下单时间
		order.setAddress(req.getParameter("address"));// 给地址赋值
		User owner = (User) req.getSession().getAttribute("sessionUser");
		order.setOwner(owner);// 给订单拥有者赋值

		// 给订单的总金额赋值
		BigDecimal total = new BigDecimal("0");
		for (CartItem cartItem : cartItemList) {
			total = total.add(new BigDecimal(cartItem.getSubTotal() + ""));
		}
		order.setTotal(total.doubleValue());
		// 给订单的订单条目赋值，主要的值从购物车条目中获取
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (CartItem cartItem : cartItemList) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(CommonUtils.uuid());// 给订单条目赋值
			orderItem.setQuantity(cartItem.getQuantity());// 从购物车条目中获取数量给订单的数量赋值
			orderItem.setOrder(order);// 给订单条目的所属订单赋值
			orderItem.setSubtotal(cartItem.getSubTotal());// 从购物车条目中获取小计赋值给订单条目
			orderItem.setBook(cartItem.getBook());// 从购物车条目中获取图书对象赋值给订单条目
			orderItemList.add(orderItem);// 将一个条目保存在条目集合中
		}
		order.setOrderItemList(orderItemList);// 给订单的订单条目集合赋值
		orderService.createOrder(order);

		// 删除购物车中用于生成订单的选项
		cartItemService.batchDelect(cartItemids);

		// 保存到req然后转发到相应的页面
		req.setAttribute("order", order);
		return "f:/jsps/order/ordersucc.jsp";
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
		return "f:/jsps/order/desc.jsp";
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
			return "f:/jsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "订单已经成功取消");
		return "f:/jsps/msg.jsp";
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
	public String confirm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		int status = orderService.findStatus(oid);
		if (status != 3) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "不正确的状态不能确认收货");
			return "f:/jsps/msg.jsp";
		}
		orderService.updateStatus(oid, 4);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "订单已经成功确认收货");
		return "f:/jsps/msg.jsp";
	}

	/**
	 * 支付准备工作返回订单的总金额以及订单的单号
	 * 1.得到oid
	 * 2.得到order
	 * 3.量order保存在req中
	 * 4.转发到相应的页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String paymentPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String oid = req.getParameter("oid");
		Order order = orderService.load(oid);
		req.setAttribute("order", order);
		return "f:/jsps/order/pay.jsp";
	}

	/**
	 * 支付请求处理方法
	 * 1.加载配置文件
	 * 2.给出13个参数
	 * 3.得到重定向url，注意后面链接相应的参数
	 * 4.重定向到易宝界面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String payment(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Properties pro = new Properties();
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("payment.properties");
		pro.load(in);

		String p0_Cmd = "Buy";// 固定值“Buy”
		String p1_MerId = pro.getProperty("p1_MerId");// 商户在易宝支付系统的唯一身份标识.获取方式见“如何获得商户编号”
		String p2_Order = req.getParameter("oid");// 商户订单号
		String p3_Amt = "0.01";// 支付金额
		String p4_Cur = "CNY";// 交易币种
		String p5_Pid = "";// 商品名称
		String p6_Pcat = "";// 商品种类
		String p7_Pdesc = "";// 商品描述
		String p8_Url = pro.getProperty("p8_Url");// 商户接收支付成功数据的地址
		String p9_SAF = "";// 送货地址
		String pa_MP = "";// 商户扩展信息
		String pd_FrpId = req.getParameter("yh");// 支付通道编码
		String pr_NeedResponse = "1";// 应答机制

		String keyValue = pro.getProperty("keyValue");// 商家密码
		// 得到加密信息
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);

		StringBuilder sb = new StringBuilder(
				"https://www.yeepay.com/app-merchant-proxy/node");
		sb.append("?").append("p0_Cmd=").append(p0_Cmd);
		sb.append("&").append("p1_MerId=").append(p1_MerId);
		sb.append("&").append("p2_Order=").append(p2_Order);
		sb.append("&").append("p3_Amt=").append(p3_Amt);
		sb.append("&").append("p4_Cur=").append(p4_Cur);
		sb.append("&").append("p5_Pid=").append(p5_Pid);
		sb.append("&").append("p6_Pcat=").append(p6_Pcat);
		sb.append("&").append("p7_Pdesc=").append(p7_Pdesc);
		sb.append("&").append("p8_Url=").append(p8_Url);
		sb.append("&").append("p9_SAF=").append(p9_SAF);
		sb.append("&").append("pa_MP=").append(pa_MP);
		sb.append("&").append("pd_FrpId=").append(pd_FrpId);
		sb.append("&").append("pr_NeedResponse=").append(pr_NeedResponse);
		sb.append("&").append("hmac=").append(hmac);

		resp.sendRedirect(sb.toString());
		return null;
	}

	/**
	 *支付返回方法
	 *1.
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 得到12个参数
		String p1_MerId = req.getParameter("p1_MerId");// 商户编号
		String r0_Cmd = req.getParameter("r0_Cmd");// 业务类型
		String r1_Code = req.getParameter("r1_Code");// 支付结果
		String r2_TrxId = req.getParameter("r2_TrxId");// 易宝支付交易流水号
		String r3_Amt = req.getParameter("r3_Amt");// 支付金额
		String r4_Cur = req.getParameter("r4_Cur");// 交易币种
		String r5_Pid = req.getParameter("r5_Pid");// 商品名称
		String r6_Order = req.getParameter("r6_Order");// 商户订单号
		String r7_Uid = req.getParameter("r7_Uid");// 易宝支付会员ID
		String r8_MP = req.getParameter("r8_MP");// 商户扩展信息
		String r9_BType = req.getParameter("r9_BType");// 交易结果返回类型
		String hmac = req.getParameter("hmac");// 签名数据

		// 得到商家面并且印证加密信息是不是一样
		Properties pro = new Properties();
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("payment.properties");
		pro.load(in);
		String keyValue = pro.getProperty("keyValue");// 商家密码
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);

		// 如果不正确返回错误信息
		if (!bool) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "无效的签名，支付失败");
			return "f:/jsps/msg.jsp";
		}

		// 成功返回的话那么就修改订单的状态然后根据结果返回类型返回相应的信息
		if (("1").equals(r1_Code)) {
			orderService.updateStatus(r6_Order, 2);
			if ("1".equals(r9_BType)) {
				req.setAttribute("code", "success");
				req.setAttribute("msg", "支付成功");
				return "f:/jsps/msg.jsp";
			} else if ("2".equals(r9_BType)) {
				resp.getWriter().print("success");
			}
		}
		return null;
	}
}
