package com.ydg.bookstore.peger;

/**
 * 这个类是所有是条件的结合
 * @author admin
 *
 */
public class Expression {
	private String name;//条件名称
	private String operator;//条件运算符
	private String value;//具体的条件的值

	@Override
	public String toString() {
		return "Expression [name=" + name + ", operator=" + operator
				+ ", value=" + value + "]";
	}

	public Expression() {
		super();
	}

	public Expression(String name, String operator, String value) {
		super();
		this.name = name;
		this.operator = operator;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
