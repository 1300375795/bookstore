package com.ydg.bookstore.order.domain;

import java.util.List;

import com.ydg.bookstore.user.domain.User;

/**
 *订单类
 *oid表示主键
 *ordertime表示已下单时间
 *total表示总的金额
 *status表示订单状态其中1表示下单未付款2.表示已经付款未发货3.表示已付款已经发货4.表示确认收货完成交易5.表示取消订单
 *user表示订单的所有者
 *OederItemList表示拥有的订单条目
 * @author ydg
 *
 */
public class Order {
	private String oid;
	private String ordertime;
	private double total;
	private int status;
	private String address;
	private User owner;
	private List<OrderItem> orderItemList;

	@Override
	public String toString() {
		return "Order [oid=" + oid + ", ordertime=" + ordertime + ", total="
				+ total + ", status=" + status + ", address=" + address
				+ ", owner=" + owner + ", orderItemList=" + orderItemList + "]";
	}

	public Order() {
		super();
	}

	public Order(String oid, String ordertime, double total, int status,
			String address, User owner, List<OrderItem> orderItemList) {
		super();
		this.oid = oid;
		this.ordertime = ordertime;
		this.total = total;
		this.status = status;
		this.address = address;
		this.owner = owner;
		this.orderItemList = orderItemList;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

}
