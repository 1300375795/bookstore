package com.ydg.bookstore.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

import com.ydg.bookstore.book.domain.Book;
import com.ydg.bookstore.order.domain.Order;
import com.ydg.bookstore.order.domain.OrderItem;
import com.ydg.bookstore.peger.Expression;
import com.ydg.bookstore.peger.PageBean;
import com.ydg.bookstore.peger.PageConstants;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 通过uid查询相应的一个pageBean出来 
	 * 1.创建条件集合 
	 * 2.创建条件 
	 * 3.添加条件到条件集合中 
	 * 4.调用底层依赖 
	 * 5.返回pageBean
	 * 
	 * @param uid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByUser(String uid, int pc) throws SQLException {
		List<Expression> expreList = new ArrayList<Expression>();
		Expression exp = new Expression("uid", "=", uid);
		expreList.add(exp);
		PageBean<Order> pageBean = findByCriteria(expreList, pc);
		return pageBean;

	}

	/**
	 * 所有查询方法的底层依赖 
	 * 1.得到每个分页的记录数 
	 * 2.得到where字句，注意is null情况的 
	 * 3.注意是select count（*）
	 * from 查询记录数
	 * 4.查询订单的数量 
	 * 5.创建pageBean对象然后给他的一些值赋值 
	 * 6.返回pageBean
	 * 
	 * @param expreList
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	private PageBean<Order> findByCriteria(List<Expression> expreList, int pc)
			throws SQLException {
		int ps = PageConstants.ORDER_PAGE_SIZE;

		List<Object> params = new ArrayList<Object>();
		StringBuilder whereSQL = new StringBuilder(" WHERE 1=1");
		for (Expression exp : expreList) {
			whereSQL.append(" AND ").append(" ").append(exp.getName())
					.append(" ").append(exp.getOperator()).append(" ");

			if (!"is null".equalsIgnoreCase(exp.getOperator())) {
				whereSQL.append("?");
				params.add(exp.getValue());
			}
		}

		String sql = "SELECT COUNT(*) FROM t_order " + whereSQL;
		Number number = (Number) qr.query(sql, new ScalarHandler(),
				params.toArray());
		int tr = number.intValue();

		sql = "SELECT * FROM t_order " + whereSQL
				+ "ORDER BY ordertime desc limit ?,?";
		params.add((pc - 1) * ps);
		params.add(ps);

		List<Order> beanList = qr.query(sql, new BeanListHandler<Order>(
				Order.class), params.toArray());

		for (Order order : beanList) {
			loadOrderItem(order);
		}

		PageBean<Order> pb = new PageBean();
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);

		return pb;
	}

	/**
	 * 给每一个order加载他拥有的orderItem 
	 * 1.查询数据库该order下面的条目
	 * 2.得到相应的mapList集合
	 * 3.将mapList集合转换成相应的orderItem集合
	 * 4.给每个order赋值他拥有的OrderItem
	 * 
	 * @param order
	 * @throws SQLException
	 */
	private void loadOrderItem(Order order) throws SQLException {
		String sql = "SELECT * FROM t_orderitem where oid=?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),
				order.getOid());
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);

	}

	/**
	 * 将一个List<Map<String, Object>>的集合转化成一个List<OrderItem> 
	 * 1.判断事故list是否为null是的话返回null 
	 * 2.创建一个List<OrderItem>集合 
	 * 3.循环遍历这个list是的map转换相应的orderItem然后存到集合里面返回这个转换好的list集合
	 * 
	 * @param mapList
	 * @return
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		if (mapList == null || mapList.size() == 0) {
			return null;
		}
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (Map<String, Object> map : mapList) {
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}

	/**
	 * 将一个Map转换成OrderItem 
	 * 1.将map映射成一个OrderItem里面只有OrderItem相关的内容
	 * 2.将map映射成一个Book里面存在Book的数据 
	 * 3.将Book赋值给相应的OrderItem 
	 * 4.返回OrderItem
	 * 
	 * @param map
	 * @return
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

	/**
	 * 我的订单的提交功能
	 * 1.给出添加订单的SQL语句
	 * 2.同过传递的order将相应的参数给其
	 * 3.执行添加功能
	 * 4.给出添加orderItem的SQL语句
	 * 5.创建一个orderItem长度的二维数组
	 * 6.循环给这个二位数组添加相应的参数
	 * 7.执行批量添加功能
	 * @param order
	 * @throws SQLException
	 */
	public void createOrder(Order order) throws SQLException {
		try {

			String sql = "INSERT INTO t_order VALUES(?,?,?,?,?,?)";
			Object[] params = { order.getOid(), order.getOrdertime(),
					order.getTotal(), order.getStatus(), order.getAddress(),
					order.getOwner().getUid() };
			qr.update(sql, params);

			sql = "INSERT INTO t_orderitem	VALUES(?,?,?,?,?,?,?,?)";
			int len = order.getOrderItemList().size();
			Object[][] objs = new Object[len][];
			for (int i = 0; i < len; i++) {
				OrderItem orderItem = order.getOrderItemList().get(i);
				objs[i] = new Object[] { orderItem.getOrderItemId(),
						orderItem.getQuantity(), orderItem.getSubtotal(),
						orderItem.getBook().getBid(),
						orderItem.getBook().getBname(),
						orderItem.getBook().getCurrPrice(),
						orderItem.getBook().getImage_b(), order.getOid() };
			}
			qr.batch(sql, objs);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 查询某个订单和他下面就的全部的条目
	 * 1.给出SQL语句
	 * 2.查询相应的订单
	 * 3.给这个订单加载他下面的订单条目
	 * @param oid
	 * @return
	 * @throws SQLException
	 */
	public Order load(String oid) throws SQLException {
		String sql = "SELECT * FROM t_order WHERE oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);
		loadOrderItem(order);// 加载他所拥有的全部订单
		return order;
	}

	/**
	 * 修改某个订单的状态
	 * 1.给出SQL语句
	 * 2.执行更新操作
	 * @param oid
	 * @param status
	 * @throws SQLException
	 */
	public void updateStatus(String oid, int status) throws SQLException {
		String sql = "UPDATE t_order SET status=? WHERE oid=?";
		qr.update(sql, status, oid);
	}

	/**
	 * 查询某个订单的状态
	 * 1.给出SQL语句
	 * 2.执行相关的方法
	 * 3.返回status
	 * @param oid
	 * @param status
	 * @return
	 * @throws SQLException 
	 */
	public int findStatus(String oid) throws SQLException {
		String sql = "SELECT status FROM t_order WHERE oid=?";
		Number number = (Number) qr.query(sql, new ScalarHandler(), oid);
		int status = number.intValue();
		return status;
	}
}
