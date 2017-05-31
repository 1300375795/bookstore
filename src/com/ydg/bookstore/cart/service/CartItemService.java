package com.ydg.bookstore.cart.service;

import java.sql.SQLException;
import java.util.List;

import com.ydg.bookstore.cart.dao.CartItemDao;
import com.ydg.bookstore.cart.domain.CartItem;

public class CartItemService {
	private CartItemDao cartItemDao = new CartItemDao();

	/**
	 * 通过uid查询当前用户的购物车
	 * 
	 * @param uid
	 * @return
	 */
	public List<CartItem> myCart(String uid) {
		try {
			return cartItemDao.findByUser(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
