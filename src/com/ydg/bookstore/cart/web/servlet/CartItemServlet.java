package com.ydg.bookstore.cart.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import com.ydg.bookstore.book.domain.Book;
import com.ydg.bookstore.cart.domain.CartItem;
import com.ydg.bookstore.cart.service.CartItemService;
import com.ydg.bookstore.user.domain.User;

public class CartItemServlet extends BaseServlet {
	private CartItemService cartItemService = new CartItemService();

	/**
	 * 通过登录的session对象找到当前用户的uid然后通过uid找到相应的CartList集合
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myCart(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute("sessionUser");
		String uid = user.getUid();
		List<CartItem> cartItemList = cartItemService.myCart(uid);
		req.setAttribute("cartItemList", cartItemList);
		return "f:/jsps/cart/list.jsp";
	}

	/**
	 * 添加购物车条目
	 * 1.先将传递来的传输封装到CartItem中，只封装了quantity
	 * 2.然后再封装map中的bid到book中得到book对象
	 * 3.然后从sessionUser中得到User 
	 * 4.将book和user赋值给cartItem 
	 * 5.调用service层的add方法完成添加
	 * 6.显示结果在list.jsp中
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map map = req.getParameterMap();
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = (User) req.getSession().getAttribute("sessionUser");
		cartItem.setBook(book);
		cartItem.setUser(user);
		cartItemService.add(cartItem);
		return myCart(req, resp);
	}

	/**
	 * 批量删除和单一删除的方法
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String batchDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cartItemIds = req.getParameter("cartItemIds");
		cartItemService.batchDelect(cartItemIds);
		return myCart(req, resp);
	}

	/**
	 * 通过cartItemId更新数量，然后放回一个json对象里面包含一个quantity属性
	 * 以及一个subTotal属性
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateQuantity(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1.得到cartItemId和quantity 
		 * 2.通过service方法得到cartItem
		 * 3.拼接出一个json对象里面包含一个quantity属性以及一个subTotal属性
		 * 4.写向页面
		 */
		String cartItemId = req.getParameter("cartItemId");
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		CartItem cartItem = cartItemService.updeQuantity(cartItemId, quantity);
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"subTotal\"").append(":").append(cartItem.getSubTotal());
		sb.append("}");
		resp.getWriter().print(sb);
		return null;
	}

	/**
	 * 购物车模块结算功能的实现
	 * 1.得到客户端传递的cartItemIds参数
	 * 2.调用service层方法得到List<CartItem>
	 * 3.将cartItemList保存在req中
	 * 4.返回到jsps下面的cart下面的showitem页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String listCartItems(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cartItemIds = req.getParameter("cartItemIds");
		String total = req.getParameter("total");
		List<CartItem> cartItemList = cartItemService
				.listCartItems(cartItemIds);
		req.setAttribute("cartItemList", cartItemList);
		req.setAttribute("total", total);
		req.setAttribute("cartItemIds", cartItemIds);
		return "f:/jsps/cart/showitem.jsp";
	}
}
