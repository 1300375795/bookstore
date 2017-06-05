package com.ydg.bookstore.admin.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import com.ydg.bookstore.book.service.BookService;
import com.ydg.bookstore.category.domain.Category;
import com.ydg.bookstore.category.service.CategoryService;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();
	private BookService bookService = new BookService();

	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/list.jsp";
	}

	/**
	 * 添加一级分类
	 * 1.封装表单提供的参数
	 * 2.给出cid
	 * 3.执行service层的方法
	 * 4.插叙你全部显示在list页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category category = CommonUtils.toBean(req.getParameterMap(),
				Category.class);
		category.setCid(CommonUtils.uuid());
		categoryService.add(category);
		return findAll(req, resp);
	}

	/**
	 * 添加二级分类的准备工作
	 * 1.将默认显示的一级分类得到
	 * 2.得到全部的一节分类
	 * 3.保存默认显示的一级分类和全部的一节分类
	 * 4.转发到相应的页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pid = req.getParameter("pid");
		List<Category> parents = categoryService.findParents();
		req.setAttribute("pid", pid);
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/add2.jsp";
	}

	/**
	 * 添加二级分类
	 * 1.封装表单中传递的参数（cname、desc）
	 * 2.给二级分类的cid赋值
	 * 3.得到表单中传递的pid通过pid映射一个相应的Category对象赋值给child的parent
	 * 4.执行添加方法
	 * 5.返回到list界面展示
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category child = CommonUtils.toBean(req.getParameterMap(),
				Category.class);
		child.setCid(CommonUtils.uuid());

		String pid = req.getParameter("pid");
		Category parent = new Category();
		parent.setCid(pid);
		child.setParent(parent);

		categoryService.add(child);
		return findAll(req, resp);
	}

	/**
	 * 修改以及分类的准备工作
	 * 1.得到传递的参数cid
	 * 2.通过cid得到相应的以及分类
	 * 3.保存categgory
	 * 4.转发到相应页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParentPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		Category parent = categoryService.load(cid);
		req.setAttribute("parent", parent);
		return "f:/adminjsps/admin/category/edit.jsp";
	}

	/**
	 * 后台修改一级分类的实现方法
	 * 1.封装表单中传递的参数(cid.cname.desc)
	 * 2.执行修改方法
	 * 3.执行查询全部的一节分类并转发list.jsp页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category category = CommonUtils.toBean(req.getParameterMap(),
				Category.class);
		categoryService.edit(category);
		return findAll(req, resp);
	}

	/**
	 * 后台修改二级分类的准备工作
	 * 1.得到传递的cid参数
	 * 2.通过二级分类的cid得到相应的category
	 * 3.保存category到req中
	 * 4.转发到相应的页面吧
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		Category child = categoryService.load(cid);
		List<Category> parents = categoryService.findParents();
		req.setAttribute("child", child);
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/edit2.jsp";
	}

	/**
	 * 后台修改二级分类的功能实现
	 * 1.封装表单传递的参数得到category对象里面包括(cid.cname.desc)
	 * 2.得到pid参数
	 * 3.创建一个category对应的parent对象，将得到的pid的值赋值给他的cid
	 * 4.将parent赋值给category
	 * 5.通过category编辑修改数据库中的相应数据
	 * 6.通过finaAll方法显示全部的一级二级分类信息并转发到list.jsp页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category category = CommonUtils.toBean(req.getParameterMap(),
				Category.class);
		String pid = req.getParameter("pid");
		Category parent = new Category();
		parent.setCid(pid);
		category.setParent(parent);
		categoryService.edit(category);
		return findAll(req, resp);
	}

	/**
	 * 后台删除一级分类的方法实现
	 * 1.得到cid参数
	 * 2.得到该cid参数下面的子分类的个数
	 * 3.判断个数如果大于0那么给出错误信息:不能删除
	 * 4.否则删除并调用finaAll方法
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String removeParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		int count = categoryService.conutChildCategoryByPid(cid);
		if (count > 0) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "该分类下面拥有子分类，不能删除");
			return "f:/adminjsps/admin/msg.jsp";
		}
		categoryService.remove(cid);
		return findAll(req, resp);
	}
	
	/**
	 * 后台删除删除二级分类的方法
	 * 1.得到参数cid
	 * 2.通过bookService查询这个分类下面book的数量
	 * 3.大于0的话那么就返回错误信息···
	 * 4.否则删除之
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String removeChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		int count = bookService.countBookByCid(cid);
		if (count > 0) {
			req.setAttribute("msg", "该分类下面有图书不能删除该分类");
			req.setAttribute("code", "error");
			return "f:/adminjsps/admin/msg.jsp";
		}
		categoryService.remove(cid);
		return findAll(req, resp);
	}
}
