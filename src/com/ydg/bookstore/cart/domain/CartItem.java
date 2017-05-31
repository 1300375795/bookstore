package com.ydg.bookstore.cart.domain;

import java.math.BigDecimal;

import com.ydg.bookstore.book.domain.Book;
import com.ydg.bookstore.user.domain.User;

/**
 * 购物车条目类
 * 
 * @author admin
 *
 */
public class CartItem {
	private String cartItemId;// 主键
	private int quantity;// 数量
	private Book book;// bid所对应的book对象
	private User user;// uid所对应的user

	public double getSubTotal() {
		BigDecimal b1 = new BigDecimal(book.getCurrPrice() + "");
		BigDecimal b2 = new BigDecimal(quantity + "");
		BigDecimal b3 = b1.multiply(b2);
		return b3.doubleValue();

	}

	public String getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CartItem(String cartItemId, int quantity, Book book, User user) {
		super();
		this.cartItemId = cartItemId;
		this.quantity = quantity;
		this.book = book;
		this.user = user;
	}

	public CartItem() {
		super();
	}

	@Override
	public String toString() {
		return "CartItem [cartItemId=" + cartItemId + ", quantity=" + quantity
				+ ", book=" + book + ", user=" + user + "]";
	}
}
