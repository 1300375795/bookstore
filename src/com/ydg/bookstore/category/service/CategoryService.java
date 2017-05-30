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
}
