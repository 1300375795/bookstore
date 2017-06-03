package com.ydg.bookstore.order.service;

import java.sql.SQLException;

import cn.itcast.jdbc.JdbcUtils;

import com.ydg.bookstore.order.dao.OrderDao;
import com.ydg.bookstore.order.domain.Order;
import com.ydg.bookstore.peger.PageBean;

public class OrderService {
	private OrderDao orderDao = new OrderDao();

	/**
	 * 我的订单的service的实现，这个需要使用事务
	 * 1.开启事务
	 * 2.调用dao层的方法返回一个pageBean（有多个）
	 * 3.结束事务
	 * 4.如果执行事务的过程中出现异常的话rollback
	 * @param uid
	 * @param pc
	 * @return
	 */
	public PageBean<Order> myOrders(String uid, int pc) {
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByUser(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (Exception e2) {
			}
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建我的订单的service层的实现代码
	 * 1.开启事务
	 * 2.执行dao层代码创建一个order
	 * 3.关闭事务或者回滚事务
	 * @param order
	 */
	public void createOrder(Order order) {
		try {
			JdbcUtils.beginTransaction();
			orderDao.createOrder(order);
			JdbcUtils.commitTransaction();
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (Exception e2) {
			}
			throw new RuntimeException(e);
		}
	}

	/**
	 * 查询一个订单的功能实现
	 * @param oid
	 * @return
	 */
	public Order load(String oid) {
		try {
			JdbcUtils.beginTransaction();
			Order order = orderDao.load(oid);
			JdbcUtils.commitTransaction();
			return order;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (Exception e2) {
			}
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 修改订单的状态
	 * @param oid
	 * @param status
	 */
	public void updateStatus(String oid, int status) {
		try {
			orderDao.updateStatus(oid, status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 查询某个订单的状态
	 * @param oid
	 * @param status
	 * @return
	 */
	public int findStatus(String oid) {
		try {
			return orderDao.findStatus(oid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
