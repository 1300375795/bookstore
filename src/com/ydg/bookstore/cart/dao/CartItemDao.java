package com.ydg.bookstore.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

import com.ydg.bookstore.book.domain.Book;
import com.ydg.bookstore.cart.domain.CartItem;
import com.ydg.bookstore.user.domain.User;

public class CartItemDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 将map类型的转换成相应的CateItem
	 * 
	 * @param map
	 * @return
	 */
	private CartItem toCartItem(Map<String, Object> map) {
		if (map == null) {
			return null;
		}
		// 封装cartItem
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		// 封装book
		Book book = CommonUtils.toBean(map, Book.class);
		// 封装User
		User user = CommonUtils.toBean(map, User.class);
		// 赋值book
		cartItem.setBook(book);
		// 赋值user
		cartItem.setUser(user);
		return cartItem;
	}

	/**
	 * 通过List<Map>转换成List<CartItem>
	 * 
	 * @param mapList
	 * @return
	 */
	private List<CartItem> toCartItemList(List<Map<String, Object>> mapList) {
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		for (Map<String, Object> map : mapList) {
			CartItem cartItem = toCartItem(map);
			cartItemList.add(cartItem);
		}
		return cartItemList;
	}

	/**
	 * 通过uid返回CartItem集合
	 * 
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> findByUser(String uid) throws SQLException {
		// 多表组合查询
		String sql = "SELECT * FROM t_cartitem c, t_book b WHERE c.bid=b.bid and uid=? order by c.orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),
				uid);
		return toCartItemList(mapList);
	}
}
