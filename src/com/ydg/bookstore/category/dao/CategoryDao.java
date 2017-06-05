package com.ydg.bookstore.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

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
	public List<Category> listChildCategiryByPid(String pid) throws SQLException {
		// 查询出某个pid的全部子分类
		String sql = "SELECT * FROM t_category WHERE pid=? ORDER BY orderBy";
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
		String sql = "SELECT * FROM t_category WHERE pid is null ORDER BY orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		// 将查出来的虽有一份分类进行处理封装了4个属性cid、cname、parent（间接表示pid），desc
		List<Category> parents = toCategoryList(mapList);
		// 遍历全部的parents给起的children属性赋值
		for (Category parent : parents) {
			// 通过当前parent的cid(即子分类的pid)查询出全部的子分类
			List<Category> children = listChildCategiryByPid(parent.getCid());
			parent.setChildren(children);
		}

		return parents;
	}

	/**
	 * 添加一级分类和二级分类
	 * 1.给出SQL语句注意desc需要``引起来
	 * 2.设置以及分类的pid为null另行判断parent是否为null不是的话那么就得到他的parent的cid赋值给他的pid
	 * 3.给出相应的参数
	 * 4.执行添加操作
	 * @param category
	 * @throws SQLException
	 */
	public void addParent(Category category) throws SQLException {
		String sql = "INSERT INTO t_category(cid,cname,pid,`desc`)  VALUES(?,?,?,?)";
		String pid = null;// 一级分类
		/*
		 * 为什么要下面这样子判断下 因为一级分类没有parent而如果直接parent.getCid那么就会出现空指针异常
		 * 所以需要判断下如果不为null的话那么就直接设置他的pid为当前的parent的cid
		 */
		if (category.getParent() != null) {
			pid = category.getParent().getCid();//
		}
		Object[] params = { category.getCid(), category.getCname(), pid,
				category.getDesc() };
		qr.update(sql, params);
	}

	/**
	 * 查询所有的父分类不带pid属性
	 * 1.执行sql语句
	 * 2.得到全部的一级分类
	 * 3.返回所有一级分类
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findParents() throws SQLException {
		String sql = "SELECT * FROM t_category WHERE pid is null ORDER BY orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		return toCategoryList(mapList);
	}

	/**
	 * 后台修改分类的方法
	 * 1.给出SQL语句通过cid语句查询某个特定给定category
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public Category load(String cid) throws SQLException {
		String sql = "SELECT * FROM t_category WHERE cid=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), cid);
		Category category = toCategory(map);
		return category;
	}

	/**
	 * 后台修改一级分类的方法实现
	 * 1.给出sql语句,注意desc跟数据库的关键词冲突一定要加上``记住！！！！！
	 * 2.给出相应的参数
	 * 3.执行更新操作
	 * @param category
	 * @throws SQLException
	 */
	public void edit(Category category) throws SQLException {
		String sql = "UPDATE t_category SET cname=?, pid=?, `desc`=? WHERE cid=?";
		String pid = null;
		if (category.getParent() != null) {
			pid = category.getParent().getCid();
		}
		Object[] params = { category.getCname(), pid, category.getDesc(),
				category.getCid() };
		qr.update(sql, params);
	}

	/**
	 * 后台删除没有子分类的某个分类的方法
	 * 1.给出sql语句
	 * 2.执行删除操作
	 * @param cid
	 * @throws SQLException
	 */
	public void remove(String cid) throws SQLException {
		String sql = "DELETE FROM t_category WHERE cid=?";
		qr.update(sql, cid);
	}

	/**
	 * 通过cid得到某个分类的子分类的个数
	 * 1.给出sql语句
	 * 2.得到这个cid所拥有的子分类的Number值并转换成相应的int类型
	 * 3.返回拥有的子类的值
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public int conutChildCategoryByPid(String pid) throws SQLException {
		String sql = "SELECT COUNT(*) FROM t_category WHERE pid=?";
		Number number = (Number) qr.query(sql, new ScalarHandler(), pid);
		return number == null ? 0 : number.intValue();
	}

}
