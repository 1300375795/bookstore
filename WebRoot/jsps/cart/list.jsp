<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>cartlist.jsp</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script src="<c:url value='/js/round.js'/>"></script>

<link rel="stylesheet" type="text/css"
	href="<c:url value='/jsps/css/cart/list.css'/>">
<script type="text/javascript">
	$(function() {
		showTotal();//结算总计
		$("#selectAll").click(function() {//给总结设置鼠标点击事件
			//得到checked属性的值
			var bool = $(this).attr("checked");
			setItemCheckBox(bool);//设置相应的 其他条目复选框跟全选复选框一样的checked状态
			setJieSuan(bool);//设置结算按钮状态
			showTotal();//重新计算总价
		});

		//注意需要放在文档加载时间的时候进行，得到全部的checkbox
		$(":checkbox[name=checkboxBtn]").click(function() {
			//得到全部的checkbox的长度
			var all = $(":checkbox[name=checkboxBtn]").length;
			//得到全部勾选起来的checkbox长度
			var select = $(":checkbox[name=checkboxBtn][checked=true]").length;
			if (all == select) {
				//如果相等说明全部是全选状态，设置全选按钮为true结算按钮为亮
				$("#selectAll").attr("checked", true);
				setJieSuan(true);
			} else if (select == 0) {
				//如果选中为0说明全部是全不选状态，设置全选按钮为false结算按钮为灭
				$("#selectAll").attr("checked", false);
				setJieSuan(false);
			} else {
				//否则说明是部分选中全选按钮为false结算按钮为true
				$("#selectAll").attr("checked", false);
				setJieSuan(true);
			}
			showTotal();//重新计算总价
		});

		$(".jian")
				.click(
						function() {
							var id = $(this).attr("id").substring(0, 32);
							var quantity = $("#" + id + "Quantity").val();
							if (quantity == 1) {//如果等于1再减去1的话等于0这个时候询问是否删除这个条目
								if (confirm("请问是否删除这条条目")) {
									location = location = "/goods/CartItemServlet?method=batchDelete&cartItemIds="
											+ id;
								}
							} else {
								updateQuantity(id, Number(quantity) - 1);
							}
						});
		//给加号添加鼠标点击事件
		$(".jia").click(function() {
			//得到当前的标签的id，由于在标签前面加了32位的cartItemId所以需要截取前面的32位
			var id = $(this).attr("id").substring(0, 32);
			//得到quantity标签的数量值
			var quantity = $("#" + id + "Quantity").val();
			updateQuantity(id, Number(quantity) + 1);//调用这个方法参数为ID和增加了1的当前的quantity数量
		});
	});
	
	//异步请求数据库然后修改页面上面的数量以及小计以及总计
	function updateQuantity(id, quantity) {
		$.ajax({
			async : false,//是否异步
			cache : false,//是否缓存
			url : "/goods/CartItemServlet",//请求地址
			data : {//参数
				method : "updateQuantity",
				cartItemId : id,
				quantity : quantity
			},
			type : "POST",//请求方式
			dataType : "json",//数据解析形式
			success : function(result) {//执行成功后调用的方法resulr为返回的json对象
				$("#" + id + "Quantity").val(result.quantity);//将id··Quantity的值改成···
				$("#" + id + "Subtotal").text(result.subTotal);//将id··subTotal的值改成···
				showTotal();//修改总计
			}
		});
	}

	//批量删除jsp页面代码
	function batchDelete() {
		var cartItemIdsArray = new Array();//创建数组
		//循环遍历被选中的复选框
		$(":checkbox[name=checkboxBtn][checked=true]").each(function() {
			cartItemIdsArray.push($(this).val());//将复选框中的value值存到数组中
		});
		//请求下面这个地址
		location = "/goods/CartItemServlet?method=batchDelete&cartItemIds="
				+ cartItemIdsArray;
	}

	//算出全部的勾选的复选框的价格总计
	function showTotal() {
		var total = 0;
		$(":checkbox[name=checkboxBtn][checked=true]").each(function() {
			var id = $(this).val();
			var text = $("#" + id + "Subtotal").text();
			total += Number(text);
		});
		//这里面的round方法是调用了js文件夹下面的round方法这个方式可以四舍五入传入的变量
		$("#total").text(round(total, 2));
	}

	//给出一个布尔值然后设置相应的复选框为勾选或者不勾选
	function setItemCheckBox(bool) {
		//得到checkbox这中种type并且name为checkboxBtn的然后设置他的摄像checked为bool所对应的值
		$(":checkbox[name=checkboxBtn]").attr("checked", bool);
	}

	//给出一个bool值然后设置相应的结算
	function setJieSuan(bool) {
		if (bool) {
			//得到结算标签删除他的kill样式，然后添加上jiesuan样式
			$("#jiesuan").removeClass("kill").addClass("jiesuan");
			//设置jiesuan标签不绑定click事件
			$("#jiesuan").unbind("click");
		} else {
			//当bool为false的时候删除jiesuan样式然后添加上kill样式
			$("#jiesuan").removeClass("jiesuan").addClass("kill	");
			//设置点击事件为false这样子点击就无效了 
			$("#jiesuan").click(function() {
				return false;
			});
		}
	}
</script>
</head>
<body>
	<c:choose>
		<c:when test="${empty cartItemList}">
			<table width="95%" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right"><img align="top"
						src="<c:url value='/images/icon_empty.png'/>" /></td>
					<td><span class="spanEmpty">您的购物车中暂时没有商品</span></td>
				</tr>
			</table>
		</c:when>
		<c:otherwise>
			<table width="95%" align="center" cellpadding="0" cellspacing="0">
				<tr align="center" bgcolor="#efeae5">
					<td align="left" width="50px"><input type="checkbox"
						id="selectAll" checked="checked" /><label for="selectAll">全选</label>
					</td>
					<td colspan="2">商品名称</td>
					<td>单价</td>
					<td>数量</td>
					<td>小计</td>
					<td>操作</td>
				</tr>



				<c:forEach items="${cartItemList }" var="cartItem">
					<tr align="center">
						<td align="left"><input value="${cartItem.cartItemId }"
							type="checkbox" name="checkboxBtn" checked="checked" /></td>
						<td align="left" width="70px"><a class="linkImage"
							href="<c:url value='/jsps/book/desc.jsp'/>"><img border="0"
								width="54" align="top"
								src="<c:url value='/${cartItem.book.image_b }'/>" /></a></td>
						<td align="left" width="400px"><a
							href="<c:url value='/jsps/book/desc.jsp'/>"><span>${cartItem.book.bname }</span></a>
						</td>
						<td><span>&yen;<span class="currPrice">${cartItem.book.currPrice }</span></span></td>
						<td><a class="jian" id="${cartItem.cartItemId }Jian"></a><input
							class="quantity" readonly="readonly"
							id="${cartItem.cartItemId }Quantity" type="text"
							value="${cartItem.quantity }" /><a class="jia"
							id="${cartItem.cartItemId }Jia"></a></td>
						<td width="100px"><span class="price_n">&yen;<span
								class="subTotal" id="${cartItem.cartItemId }Subtotal">${cartItem.subTotal }</span></span></td>
						<td><a
							href="<c:url value='/CartItemServlet?method=batchDelete&cartItemIds=${cartItem.cartItemId }'/>">删除</a></td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="4" class="tdBatchDelete"><a
						href="javascript:batchDelete();">批量删除</a></td>
					<td colspan="3" align="right" class="tdTotal"><span>总计：</span><span
						class="price_t">&yen;<span id="total"></span></span></td>
				</tr>
				<tr>
					<td colspan="7" align="right"><a
						href="<c:url value='/jsps/cart/showitem.jsp'/>" id="jiesuan"
						class="jiesuan"></a></td>
				</tr>
			</table>
			<form id="form1" action="<c:url value='/jsps/cart/showitem.jsp'/>"
				method="post">
				<input type="hidden" name="cartItemIds" id="cartItemIds" /> <input
					type="hidden" name="method" value="loadCartItems" />
			</form>
		</c:otherwise>
	</c:choose>
</body>
</html>
