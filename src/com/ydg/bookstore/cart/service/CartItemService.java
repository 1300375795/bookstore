package com.ydg.bookstore.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.commons.CommonUtils;

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

	/**
	 * 添加购物车条目
	 * 
	 * @param cartItem
	 */
	public void add(CartItem cartItem) {
		String uid = cartItem.getUser().getUid();
		String bid = cartItem.getBook().getBid();
		try {
			// 判断是否存在当前传递的条目
			CartItem oldCartItem = cartItemDao.findByUidAndBid(uid, bid);
			if (oldCartItem == null) {
				// 不存在的话给当前的cartItem设置一个id然后存到数据库中
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			} else {
				// 得到新旧啷个条目的数量只和
				int quantity = oldCartItem.getQuantity()
						+ cartItem.getQuantity();
				// 更新数量
				cartItemDao.updateQuantity(oldCartItem.getCartItemId(),
						quantity);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 批量删除和单一删除统一删除的方法
	 * 
	 * @param cartItemIds
	 */
	public void batchDelect(String cartItemIds) {
		try {
			cartItemDao.batchDelect(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *通过cartItemId和quantity更新数量并返回一个cartItem对象 
	 * @param cartItemId
	 * @param quantity
	 * @return
	 */
	public CartItem updeQuantity(String cartItemId, int quantity) {
		try {
			cartItemDao.updateQuantity(cartItemId, quantity);
			return cartItemDao.findByCartItemId(cartItemId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 购物车模块的结算功能的service实现
	 * 通过cartItemIds得到一个List<CartItem
	 * @param cartItemIds
	 * @return
	 */
	public List<CartItem> ListCartItems(String cartItemIds) {
		try {
			return cartItemDao.ListCartItems(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
