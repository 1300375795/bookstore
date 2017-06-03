package com.ydg.bookstore.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

import com.ydg.bookstore.book.domain.Book;
import com.ydg.bookstore.category.domain.Category;
import com.ydg.bookstore.peger.Expression;
import com.ydg.bookstore.peger.PageBean;
import com.ydg.bookstore.peger.PageConstants;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 所有的查询方法的底层依赖
	 * 
	 * @param expression
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	private PageBean<Book> findByCriteria(List<Expression> expression, int pc)
			throws SQLException {
		// 得到每页的记录的数据12
		int ps = PageConstants.BOOK_PAGE_SIZE;

		/*
		 * 1.给出一个List数组里面装SQL语句参数 2.循环给出的expression然后拼接上相应的SQL语句
		 */
		List<Object> params = new ArrayList<Object>();
		StringBuilder whereSQL = new StringBuilder(" WHERE 1=1");
		for (Expression exp : expression) {
			whereSQL.append(" AND ").append(exp.getName()).append(" ")
					.append(exp.getOperator()).append(" ");
			if (!"is null".equalsIgnoreCase(exp.getOperator())) {
				whereSQL.append("?");
				params.add(exp.getValue());
			}
		}

		/*
		 * 给出select部分的SQL语句然后加上where部分的语句得到总的记录数
		 */
		String sql = "SELECT COUNT(*) FROM t_book" + whereSQL;
		Number num = (Number) qr.query(sql, new ScalarHandler(),
				params.toArray());
		int tr = num.intValue();

		/*
		 * 给出查询去全部的Book的SQL语句
		 */
		sql = "SELECT * FROM t_book " + whereSQL
				+ " ORDER BY orderBy LIMIT ?,?";
		params.add((pc - 1) * ps);// 从第几个数据查起
		params.add(ps);// 差多少个数据
		List<Book> beanList = qr.query(sql, new BeanListHandler<Book>(
				Book.class), params.toArray());

		/*
		 * 将查出来的全部数据都给设置到PageBean里面
		 */
		PageBean pb = new PageBean();
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		return pb;
	}

	/**
	 * 通过分类查询
	 * 
	 * @param cid
	 * @param pageCode
	 * @return
	 */
	public PageBean<Book> findByCategory(String cid, int pc) {
		List<Expression> exp = new ArrayList<Expression>();
		exp.add(new Expression("cid", "=", cid));

		try {
			return findByCriteria(exp, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过书名的模糊查询
	 * 
	 * @param bname
	 * @param pageCode
	 * @return
	 */
	public PageBean<Book> findByBname(String bname, int pc) {
		List<Expression> exp = new ArrayList<Expression>();
		exp.add(new Expression("bname", "like", "%" + bname + "%"));

		try {
			return findByCriteria(exp, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过作者模糊查询
	 * 
	 * @param bname
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String author, int pc) {
		List<Expression> exp = new ArrayList<Expression>();
		exp.add(new Expression("author", "like", "%" + author + "%"));

		try {
			return findByCriteria(exp, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过出版社模糊查询
	 * 
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String press, int pc) {
		List<Expression> exp = new ArrayList<Expression>();
		exp.add(new Expression("press", "like", "%" + press + "%"));

		try {
			return findByCriteria(exp, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCombination(Book criteria, int pc) {
		List<Expression> exp = new ArrayList<Expression>();
		exp.add(new Expression("bname", "like", "%" + criteria.getBname() + "%"));
		exp.add(new Expression("author", "like", "%" + criteria.getAuthor()
				+ "%"));
		exp.add(new Expression("press", "like", "%" + criteria.getPress() + "%"));
		try {
			return findByCriteria(exp, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过bid查询相应的Book对象，注意使用mapHangler来查询因为这样子的话可以将cid这个属性
	 * 封装给Category中而数据库中查出来的其他的属性则封装在book中然后将category赋值给book
	 * 这样子cid也不会丢失
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public Book findByBid(String bid) throws SQLException {
		String sql = "SELECT * FROM t_book WHERE bid=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), bid);
		Book book = CommonUtils.toBean(map, Book.class);
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		return book;
	}
}
