package com.ydg.bookstore.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

import com.ydg.bookstore.category.domain.Category;
import com.ydg.bookstore.category.service.CategoryService;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();

	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/list.jsp";
	}
}
