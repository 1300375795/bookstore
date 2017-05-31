package com.ydg.bookstore.cart.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

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
}
