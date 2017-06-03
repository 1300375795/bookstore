package com.ydg.bookstore.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

import com.ydg.bookstore.category.domain.Category;

public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();

	/***
	 * 通过给出的map将map里面的值转换成相应的一个Category 1.先将map中能封装的部分先给封装了其中包括cid、cname、
	 * desc不能封装的parent 2.判断是够pid为null若为null的话给他创建一个父分类 3.将父分类赋值给当前category
	 * 
	 * @param mapList
	 * @return
	 */
	private Category toCategory(Map<String, Object> map) {
		// 先将Categoy类中与数据库中查出来的进行匹配，封装到Category中
		Category category = CommonUtils.toBean(map, Category.class);
		// 将map里面的pid给拿出来
		String pid = (String) map.get("pid");
		if (pid != null) {
			// 如果pid不为null表明为二级分类，给他创建一个父分类
			Category parent = new Category();
			// 这个父分类里面有一个cid
			parent.setCid(pid);
			// 将代表pid的这个parent赋值给当前category
			category.setParent(parent);
		}
		return category;
	}

	/**
	 * 将数据库查询出来的List<Map<String, Object>>全部转换成相应的List<Category>
	 * 
	 * @param mapList
	 * @return
	 */
	private List<Category> toCategoryList(List<Map<String, Object>> mapList) {
		// 创建一个空的 List集合里面存放Category
		List<Category> caregoryList = new ArrayList<Category>();
		// 循环遍历全部的给出的List<Map<String, Object>>
		for (Map<String, Object> map : mapList) {
			// 通过上面的将单独的一个Map转换成Category
			Category cate = toCategory(map);
			// 添加到List中
			caregoryList.add(cate);
		}
		return caregoryList;
	}

	/**
	 * 查询出某个pid的全部的子分类
	 * 
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	private List<Category> finaByParent(String pid) throws SQLException {
		// 查询出某个pid的全部子分类
		String sql = "SELECT * FROM t_category WHERE pid=?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),
				pid);
		// 将查出来的全部的子分类封装起来由于当前子分类没有接下来的子分类所以只有四个属性cid、cname、desx、parent
		List<Category> children = toCategoryList(mapList);
		return children;
	}

	/**
	 * 查询出全部的一级分类，一份分类的特点就是pid都为null
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findAll() throws SQLException {
		String sql = "SELECT * FROM t_category WHERE pid is null";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		// 将查出来的虽有一份分类进行处理封装了4个属性cid、cname、parent（间接表示pid），desc
		List<Category> parents = toCategoryList(mapList);
		// 遍历全部的parents给起的children属性赋值
		for (Category parent : parents) {
			// 通过当前parent的cid(即子分类的pid)查询出全部的子分类
			List<Category> children = finaByParent(parent.getCid());
			parent.setChildren(children);
		}

		return parents;
	}
}
