package com.ydg.bookstore.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

import com.ydg.bookstore.book.domain.Book;
import com.ydg.bookstore.book.service.BookService;
import com.ydg.bookstore.peger.PageBean;

public class BookServlet extends BaseServlet {
	private BookService bookService = new BookService();

	/**
	 * 得打pc这个参数
	 * 
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
	 * 得到url
	 * 
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
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
		return "f:/jsps/book/list.jsp";
	}

	/**
	 * 通过bid查询book并转发到desc.jsp中
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String loda(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
}
