package com.ydg.bookstore.book.service;

import java.sql.SQLException;

import com.ydg.bookstore.book.dao.BookDao;
import com.ydg.bookstore.book.domain.Book;
import com.ydg.bookstore.peger.PageBean;

public class BookService {
	private BookDao bookDao = new BookDao();

	/**
	 * 通过分类来查询
	 * 
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCatergory(String cid, int pc) {
		return bookDao.findByCategory(cid, pc);
	}

	/**
	 * 通过书名模糊查询
	 * 
	 * @param bname
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByBname(String bname, int pc) {
		return bookDao.findByBname(bname, pc);
	}

	/**
	 * 通过作者模糊查询
	 * 
	 * @param author
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String author, int pc) {
		return bookDao.findByAuthor(author, pc);
	}

	/**
	 * 通过出版社模糊查询
	 * 
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String press, int pc) {
		return bookDao.findByPress(press, pc);
	}

	/**
	 * 多条件组合查询
	 * 
	 * @param criteria
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCombination(Book criteria, int pc) {
		return bookDao.findByCombination(criteria, pc);
	}
	
	/**
	 * 通过bid加载book对象
	 * @param bid
	 * @return
	 */
	public Book load(String bid) {
		try {
			return bookDao.findByBid(bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}