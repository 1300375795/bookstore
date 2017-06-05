package com.ydg.bookstore.admin.book.web.servlet;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.commons.CommonUtils;

import com.ydg.bookstore.book.domain.Book;
import com.ydg.bookstore.book.service.BookService;
import com.ydg.bookstore.category.domain.Category;
import com.ydg.bookstore.category.service.CategoryService;

public class AdminAddBookServlet extends HttpServlet {

	/**
	 * 后台添加图书的实现方法
	 * 1.给出
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 创建解析器对象
		 */
		FileItemFactory factory = new DiskFileItemFactory();// 创建工厂对象
		ServletFileUpload sfu = new ServletFileUpload(factory);// 创建解析器对象
		sfu.setFileSizeMax(80 * 1024);// 设置文件的当个上传大小为80kb

		/*
		 * 通过解析器对象解析相应的request请求得到表单字段集合
		 */
		List<FileItem> fileItemList = null;// 创建input对象的集合
		try {
			// 通过解析器解析request得到相应的表单字段集合
			fileItemList = sfu.parseRequest(request);
		} catch (FileUploadException e) {
			// 给出错误信息
			error("您上传的文件超过80kb，禁止上传", request, response);
			return;// 必须结束方法，不然会接着执行下面的方法
		}

		/*
		 * 封装普通表单字段的信息
		 */
		// 创建一个map来保存相应的表单字段
		Map<String, Object> map = new HashMap<String, Object>();
		for (FileItem fileItem : fileItemList) {
			if (fileItem.isFormField()) {// 如果表单是普通表单字段
				// 将普通表单字段保存在map中，前面的这个得到表单字段的name例如cid、cname，后面是他们的相应的值,注意后面的需要编码下以防乱码
				map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
			}
		}
		// 封装表单中book能封装的东西
		Book book = CommonUtils.toBean(map, Book.class);
		// 封装book所对应的二级分类
		Category category = CommonUtils.toBean(map, Category.class);
		// book跟category建立联系
		book.setCategory(category);

		/*
		 * 保存大图
		 */
		FileItem fileItem = fileItemList.get(1);// 得到大图所对应的表单字段
		String fileName = fileItem.getName();// 得到文件的名称
		int index = fileName.lastIndexOf("\\");// 截取文件名这个是转义的相当于\，
		if (index != -1) {// 如果存在\的话那么就截取相应的内容
			fileName = fileName.substring(index + 1);// 截取出来相应的文件名称
		}
		// 给文件加上uuid防止重名文件的出现
		fileName = CommonUtils.uuid() + "_" + fileName;
		// 判断文件名称是否以.jsp为后缀不是的话那么就给出错误信息
		if (!fileName.toLowerCase().endsWith(".jpg")) {
			error("您上传的文件的格式不正确请上传jpg文件", request, response);
			return;// 结束方法
		}

		// 得到当前项目保存图片的路径（下面这行代码的话会得到带盘符的路径即真实路径）
		String savePath = this.getServletContext().getRealPath("/book_img");
		File destFile = new File(savePath, fileName);// 通过文件名和真实路径创建一个目标文件
		try {
			fileItem.write(destFile);// 将临时文件保存到目标文件，然后删临时文件
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// 通过目标文件的绝对路径得到有个ImageIcon用来获取Image对象然后校验文件尺寸
		ImageIcon icon = new ImageIcon(destFile.getAbsolutePath());
		Image image = icon.getImage();
		if (image.getWidth(null) != 350 || image.getHeight(null) != 350) {
			error("图片尺寸大小必须等于350px", request, response);
			destFile.delete();// 删除上传的文件
			return;
		}
		// 给book设置image_w属性
		book.setImage_w("book_img/" + fileName);

		fileItem = fileItemList.get(2);// 得到小图表单字段
		fileName = fileItem.getName();// 得到小图的名称
		index = fileName.lastIndexOf("\\");// 判断是否存在\
		if (index != -1) {
			fileName = fileName.substring(index + 1);// 存在截取之
		}
		fileName = CommonUtils.uuid() + "_" + fileName;// 得到uuid前缀的文件名防止重名
		if (!fileName.toLowerCase().endsWith(".jpg")) {// 判断格式正确与否
			error("您上传的文件的格式不正确请上传jpg类型的文件", request, response);
			return;
		}

		// 得到项目存放文件的真是路径
		savePath = this.getServletContext().getRealPath("/book_img");
		// 创建目标文件
		destFile = new File(savePath, fileName);
		try {
			fileItem.write(destFile);// 向目标文件保存内容
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		icon = new ImageIcon(destFile.getAbsolutePath());
		image = icon.getImage();
		// 判断文件尺寸大小
		if (image.getWidth(null) != 200 || image.getWidth(null) != 200) {
			error("图片尺寸大小必须等于200px", request, response);
			destFile.delete();
			return;
		}
		book.setImage_b("book_img/" + fileName);

		book.setBid(CommonUtils.uuid());// 设置bid
		BookService bookService = new BookService();
		bookService.saveBook(book);// 保存图片

		request.setAttribute("msg", "图书保存成功");
		request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request,
				response);
	}

	/**
	 * 保存错误和相应的一级分类到相关的页面
	 * @param msg
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void error(String msg, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("msg", msg);
		request.setAttribute("parents", new CategoryService().findParents());
		request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(
				request, response);
	}
}
