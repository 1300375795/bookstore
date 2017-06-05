package com.ydg.bookstore.admin.book.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import com.ydg.bookstore.book.domain.Book;
import com.ydg.bookstore.book.service.BookService;
import com.ydg.bookstore.category.domain.Category;
import com.ydg.bookstore.category.service.CategoryService;
import com.ydg.bookstore.peger.PageBean;

public class AdminBookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();

	/**
	 * 后台查询全部的一级分类及二级分类然后显示哎手风琴式下拉菜单
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findCategoryAll(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/left.jsp";
	}

	/**
	 * 分页查询的私有方法得到pageCode
	 * 1.给出一个默认的ageCode
	 * 2.如果传递的参数中没有pc那么就默认为1有的话则设置pageCode为参数值
	 * 3.返回pageCode
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req) {
		int pc = 1;
		String param = (String) req.getParameter("pc");
		try {
			// 如果jsp中没有传递相应的参数那么默认为1否则pc为给定的值，防止转换异常try catch掉
			if (param != null && !param.trim().isEmpty()) {
				pc = Integer.parseInt(param);
			}
		} catch (Exception e) {
		}
		return pc;
	}

	/**
	 * 分页查询保存相关的查询条件的方法
	 * 1.得到请求的项目名称和servlet部分以及参数部分，需要加个?
	 * 2.得到&pc=这个子字符串的下标
	 * 3.存在和这个子字符串的话删除之
	 * 4.返回url
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req) {
		// 前面的得到项目和相应的Servlet后面的得到相应的参数
		String url = req.getRequestURI() + "?" + req.getQueryString();
		// 将后面的pc部分的切除不要
		int index = url.lastIndexOf("&pc=");
		if (index != -1) {// 不等于-1表示存在&pc=需要切除
			url = url.substring(0, index);
		}
		return url;
	}

	/**
	 * 通过分类查询相应的PageBean
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		int pc = getPc(req);// 得到pc

		String url = getUrl(req);// 得到url

		// 得到相应的PageBean
		String cid = req.getParameter("cid");
		PageBean pb = bookService.findByCatergory(cid, pc);

		// 设置PageBean
		pb.setUrl(url);

		// 保存pageBean到request中
		req.setAttribute("pb", pb);
		// 转发到list.jsp中
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 通过书名模糊查找相关的书
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByBname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPc(req);// 得到pc

		String url = getUrl(req);// 得到url

		// 得到相应的PageBean
		String bname = req.getParameter("bname");
		PageBean pb = bookService.findByBname(bname, pc);

		// 设置PageBean
		pb.setUrl(url);

		// 保存pageBean到request中
		req.setAttribute("pb", pb);
		// 转发到list.jsp中
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 通过作者模糊查询相关的书
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByAuthor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPc(req);// 得到pc

		String url = getUrl(req);// 得到url

		// 得到相应的PageBean
		String author = req.getParameter("author");
		PageBean pb = bookService.findByAuthor(author, pc);

		// 设置PageBean
		pb.setUrl(url);

		// 保存pageBean到request中
		req.setAttribute("pb", pb);
		// 转发到list.jsp中
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 通过出版社查询
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByPress(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int pc = getPc(req);// 得到pc

		String url = getUrl(req);// 得到url

		// 得到相应的PageBean
		String press = req.getParameter("press");
		PageBean pb = bookService.findByPress(press, pc);

		// 设置PageBean
		pb.setUrl(url);

		// 保存pageBean到request中
		req.setAttribute("pb", pb);
		// 转发到list.jsp中
		return "f:/adminjsps/admin/book/list.jsp";
	}

	public String findByCombination(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		int pc = getPc(req);// 得到pc

		String url = getUrl(req);// 得到url

		// 得到相应的PageBean
		Book criteria = CommonUtils.toBean(req.getParameterMap(), Book.class);

		PageBean pb = bookService.findByCombination(criteria, pc);

		// 设置PageBean
		pb.setUrl(url);

		// 保存pageBean到request中
		req.setAttribute("pb", pb);
		// 转发到list.jsp中
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 *后台添加图书的查一级分类的方法实现
	 *1.调用categoryService行方法得到一级分类集合
	 *2.保存一级分类到req中
	 *3.返回到相关页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Category> parents = categoryService.findParents();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";

	}

	/**
	 * 后台添加图书的一级分类的异步调用方法
	 * 1.得到pid参数
	 * 2.通过pid参数得到相应的所属分类
	 * 3.将此分类对象转换成相应的json对象该json对象只包含cid和cname两个属性（页面只需要这两个属性）
	 * 4.将此json对象响应到页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxListChildrenCategory(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String pid = req.getParameter("pid");
		List<Category> children = categoryService.listChildCategiryByPid(pid);
		String json = toJson(children);
		resp.getWriter().print(json);
		return null;
	}

	/**
	 * 将分类转换成相应的json对象
	 * @param category
	 * @return
	 */
	private String toJson(Category category) {
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").append(category.getCid())
				.append("\"");
		sb.append(",");
		sb.append("\"cname\"").append(":").append("\"")
				.append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 将分类集合转换成相应的json对象
	 * @param categoryList
	 * @return
	 */
	private String toJson(List<Category> categoryList) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < categoryList.size(); i++) {
			sb.append(toJson(categoryList.get(i)));
			if (i < categoryList.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 后台编辑图书中的方法实现
	 * 加载book所拥有的一级分类下面的所有的二级分类、加载全部的一级分类、加载book本身
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 通过bid得到book并保存
		 * 1.得到bid参数
		 * 2.得到book
		 * 3.保存到req中
		 */
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);

		/*
		 * 得到所有的一级分类并保存
		 * 1.得到所有的一级分类
		 * 2.保存到req中
		 */
		List<Category> parents = categoryService.findParents();
		req.setAttribute("parents", parents);

		/*
		 * 得到当前图书所属一级分类的所有的二级分类并保存
		 */
		String cid = book.getCategory().getParent().getCid();
		List<Category> children = categoryService.listChildCategiryByPid(cid);
		req.setAttribute("children", children);

		/*
		 * 转发到desc页面显示
		 */
		return "f:/adminjsps/admin/book/desc.jsp";
	}

	/**
	 * 后台图书修改功能 实现
	 * 1.得到参数的封装的map集合
	 * 2.映射出book对象
	 * 3.映射出category对象
	 * 4.book和category简历联系
	 * 5.执行更新方法
	 * 6.保存修改信息
	 * 7.转发到相关页面
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateBook(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map map = req.getParameterMap();
		Book book = CommonUtils.toBean(map, Book.class);
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		bookService.updateBook(book);
		req.setAttribute("msg", "图书修改成功");
		return "f:/adminjsps/msg.jsp";
	}

	/**
	 * 后台删除图书功能的实现,注意图片也需要删除
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String removeBook(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		String savePath = this.getServletContext().getRealPath("/");
		String image_w = book.getImage_w();
		String image_b = book.getImage_b();
		new File(savePath, image_w).delete();
		new File(savePath, image_b).delete();
		bookService.removeBook(bid);
		req.setAttribute("msg", "图书删除成功");
		return "f:/adminjsps/msg.jsp";
	}
}
