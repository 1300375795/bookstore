package com.ydg.bookstore.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
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
		if (map == null || map.size() == 0) {
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

	/**
	 * 通过uid和bid查询某个用户是否存在当前的这个条目存在的话返回不然的话不需要返回
	 * 
	 * @param uid
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByUidAndBid(String uid, String bid) throws SQLException {
		String sql = "SELECT * FROM t_cartitem WHERE uid=? AND bid=?";
		Object[] params = { uid, bid };
		Map<String, Object> map = qr.query(sql, new MapHandler(), params);
		return toCartItem(map);
	}

	/**
	 * 通过购物车id以及传递的quantity参数修改原先的购物车条目中的book的数量
	 * 
	 * @param cartItemId
	 * @param quantity
	 * @throws SQLException
	 */
	public void updateQuantity(String cartItemId, int quantity)
			throws SQLException {
		String sql = "UPDATE t_cartitem SET quantity=? WHERE cartItemId=?";
		Object[] params = { quantity, cartItemId };
		qr.update(sql, params);
	}

	/**
	 * 通过cartItemId以及quantity更新数量然后返回一个cartItem对象
	 * @param cartItemId
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByCartItemId(String cartItemId) throws SQLException {
		String sql = "SELECT * FROM t_cartitem c,t_book b WHERE c.bid=b.bid and  c.cartItemId=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), cartItemId);
		return toCartItem(map);
	}

	/**
	 * 通过传递来的CartItem对象添加当前该对象到数据库中
	 * 
	 * @param cartItem
	 * @throws SQLException
	 */
	public void addCartItem(CartItem cartItem) throws SQLException {
		String sql = "INSERT INTO t_cartitem(cartItemId,quantity,bid,uid) VALUES(?,?,?,?)";
		Object[] params = { cartItem.getCartItemId(), cartItem.getQuantity(),
				cartItem.getBook().getBid(), cartItem.getUser().getUid() };
		qr.update(sql, params);
	}

	/**
	 * 拼接where字句
	 * 
	 * @param len
	 * @return
	 */
	private static String toWhereSQL(int len) {
		StringBuilder sb = new StringBuilder("cartItemId in (");
		for (int i = 0; i < len; i++) {
			sb.append("?");
			if (i < len - 1) {
				sb.append(",");// 最后一个不拼接
			}
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * 批量删除和单一删除统一的方法
	 * 
	 * @param cartItemIds
	 * @throws SQLException
	 */
	public void batchDelect(String cartItemIds) throws SQLException {
		Object[] cartItemIdsArray = cartItemIds.split(",");
		String whereSQL = toWhereSQL(cartItemIdsArray.length);
		String sql = "DELETE FROM t_cartitem WHERE " + whereSQL;
		qr.update(sql, cartItemIdsArray);
	}

	/**
	 * 购物车模块的结算功能的实现
	 * 1.分割cartItemIds字符串成为相应的包含cartItemI的数组
	 * 2.通过toWhere方法得到SQL语句的后半部分然后拼接前半部分UC恒一个完整的SQL语句
	 * 3.查询数据库得到相应的List集合
	 * 4.通过toCartItemList方法将原先的map转换成CartItem然后返回这个集合
	 * @param cartItemIdsp
	 * @return
	 * @throws SQLException 
	 */
	public List<CartItem> ListCartItems(String cartItemIds) throws SQLException {
		Object[] cartItemIdsArray = cartItemIds.split(",");
		String whwereSQL = toWhereSQL(cartItemIdsArray.length);
		String sql = "SELECT * FROM t_cartitem t,t_book b WHERE c.bid=b.bid AND  "
				+ whwereSQL;
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),
				cartItemIdsArray);
		List<CartItem> cartItemList = toCartItemList(mapList);
		return cartItemList;
	}

}
