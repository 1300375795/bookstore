package com.ydg.bookstore.category.service;

import java.sql.SQLException;
import java.util.List;

import com.ydg.bookstore.category.dao.CategoryDao;
import com.ydg.bookstore.category.domain.Category;

public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();

	public List<Category> findAll() {
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *添加一级分类一二级分类的功能
	 * @param category
	 */
	public void add(Category category) {
		try {
			categoryDao.addParent(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 得到所有的一级分类
	 * @return
	 */
	public List<Category> findParents() {
		try {
			return categoryDao.findParents();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 后台修改以及分类的需求方法
	 * 1.通过cid返回一个相应的category
	 * @param cid
	 * @return
	 */
	public Category load(String cid) {
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 后台修改以及分类的实现方法
	 * 1.通过category返回修改相应的category
	 * @param category
	 */
	public void edit(Category category) {
		try {
			categoryDao.edit(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 删除没有子分类的分类
	 * @param cid
	 */
	public void remove(String cid) {
		try {
			categoryDao.remove(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过cid得到这个分类所拥有的子分类的个数
	 * @param cid
	 * @return
	 */
	public int conutChildCategoryByPid(String pid) {
		try {
			return categoryDao.conutChildCategoryByPid(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * 通过pid查询某个一级分类下面的全部的子分类
	 * @param pid
	 * @return
	 */
	public List<Category> listChildCategiryByPid(String pid) {
		try {
			return categoryDao.listChildCategiryByPid(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
