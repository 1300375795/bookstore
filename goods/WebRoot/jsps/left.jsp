<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>left</title>
<base target="body" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript"
	src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/menu/mymenu.js'/>"></script>
<link rel="stylesheet" href="<c:url value='/menu/mymenu.css'/>"
	type="text/css" media="all">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/jsps/css/left.css'/>">
<script language="javascript">
	/* 下面的表示创建一个手风琴式下拉菜单，var后面的名称必须与第一个参数相同
	 下面的function则表示页面加载完成执行，其中第一行的4表示颜色样式
	 第二行表示+和-号的那个图片，第三行则表示是否支持多个二级分类同时显示
	 */
	var bar = new Q6MenuBar("bar", "网上书城");
	$(function() {
		bar.colorStyle = 4;
		bar.config.imgDir = "<c:url value='/menu/img/'/>";
		bar.config.radioButton = false;

		<c:forEach items="${requestScope.parents}" var="parent">
			<c:forEach items="${parent.children}" var="child">
				bar.add("${parent.cname}", "${child.cname}",
				"/goods/BookServlet?method=findByCategory&cid=${child.cid}",
				"body");
			</c:forEach>
		</c:forEach>

		$("#menu").html(bar.toString());
	});
</script>
</head>

<body>
	<div id="menu"></div>
</body>
</html>
