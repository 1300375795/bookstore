package com.ydg.bookstore.order.domain;


import com.ydg.bookstore.book.domain.Book;

/**
 *订单条目类
 *orderItemId表示主键
 *quantity 表示数量
 *subTotal表示小计
 *Book表示拥有的图书
 *order表示所属用户
 * @author ydg
 *
 */
public class OrderItem {
	private String orderItemId;
	private int quantity;
	private double subtotal;
	private Book book;
	private Order order;

	@Override
	public String toString() {
		return "OrderItem [orderItemId=" + orderItemId + ", quantity="
				+ quantity + ", subtotal=" + subtotal + ", book=" + book
				+ ", order=" + order + "]";
	}

	public OrderItem() {
		super();
	}

	public OrderItem(String orderItemId, int quantity, double subtotal,
			Book book, Order order) {
		super();
		this.orderItemId = orderItemId;
		this.quantity = quantity;
		this.subtotal = subtotal;
		this.book = book;
		this.order = order;
	}

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
