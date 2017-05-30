package com.ydg.bookstore.category.domain;

import java.util.List;

public class Category {
	private String cid;// 主键
	private String cname;// 分类名
	private Category parent;// 父分类
	private String desc;// 分类描述
	private List<Category> children;// 子分类

	@Override
	public String toString() {
		return "Category [cid=" + cid + ", cname=" + cname + ", parent="
				+ parent + ", desc=" + desc + ", children=" + children + "]";
	}

	public Category() {
		super();
	}

	public Category(String cid, String cname, Category parent, String desc,
			List<Category> children) {
		super();
		this.cid = cid;
		this.cname = cname;
		this.parent = parent;
		this.desc = desc;
		this.children = children;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Category> getChildren() {
		return children;
	}

	public void setChildren(List<Category> children) {
		this.children = children;
	}

}
