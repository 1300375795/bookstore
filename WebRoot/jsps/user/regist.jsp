<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>注册</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/css.css'/>">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/jsps/css/user/regist.css'/>">
<script type="text/javascript"
	src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/common.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/jsps/js/user/regist.js'/>"></script>
</head>

<body>
	<div id="divMain">
		<div id="divTitle">
			<span id="spanTitle">新用户注册</span>
		</div>
		<div id="divBody">
			<form action="" method="post" id="registForm">
				<table>
					<tr>
						<td class="tdTitlt">用户名:</td>
						<td class="tdInput"><input class="inputClass" type="text"
							name="loginname" id="loginname" /></td>
						<td class="tdLabel"><label class="errorClass"></label></td>
					</tr>
					<tr>
						<td class="tdTitlt">登录密码:</td>
						<td class="tdInput"><input class="inputClass" type="passWord"
							name="loginpass" id="loginpass" /></td>
						<td class="tdLabel"><label class="errorClass"></label></td>
					</tr>
					<tr>
						<td class="tdTitlt">确认密码:</td>
						<td class="tdInput"><input class="inputClass" type="password"
							name="reloginpassword" id="reloginpass" /></td>
						<td class="tdLabel"><label class="errorClass"></label></td>
					</tr>
					<tr>
						<td class="tdTitlt">email:</td>
						<td class="tdInput"><input class="inputClass" type="text"
							name="email" id="email" /></td>
						<td class="tdLabel"><label class="errorClass"></label></td>
					</tr>
					<tr>
						<td class="tdTitlt">验证码:</td>
						<td class="tdInput"><input class="inputClass" type="text"
							name="verifyCode" id="verifyCode" /></td>
						<td class="tdLabel"><label class="errorClass"></label></td>
					</tr>
					<tr>
						<td></td>
						<td><span class="spanImg"> <img id="vCode"
								src="<c:url value='/VerifyCodeServlet'/>" />
						</span></td>
						<td><label><a id="verifyCode"
								href="javascript:change()">换一张</a></label></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="image" id="submit"
							src='<c:url value="/images/regist1.jpg"></c:url>' /></td>
						<td><label></label></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
