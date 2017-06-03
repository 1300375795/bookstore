package com.ydg.bookstore.peger;

import java.util.List;

public class PageBean<T> {
	private int pc;// 当前页码
	private int tr;// 总记录数
	private int ps;// 每页的记录数
	private String url;// 请求参数以及请求方法
	private List<T> beanList;// 页面数据

	@Override
	public String toString() {
		return "PageBean [pc=" + pc + ", tr=" + tr + ", ps=" + ps + ", url="
				+ url + ", beanList=" + beanList + "]";
	}

	public PageBean() {
		super();
	}

	public PageBean(int pc, int tr, int ps, int tp, String url, List<T> beanList) {
		super();
		this.pc = pc;
		this.tr = tr;
		this.ps = ps;
		this.url = url;
		this.beanList = beanList;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getTr() {
		return tr;
	}

	public void setTr(int tr) {
		this.tr = tr;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

	public int getTp() {
		int i = tr / ps;
		return tr % ps == 0 ? i : i + 1;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<T> getBeanList() {
		return beanList;
	}

	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;

	}
}
