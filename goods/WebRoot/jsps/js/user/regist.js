/*$(
		function() {
			
 * 1. 得到所有的错误信息，循环遍历之。调用一个方法来确定是否显示错误信息！
			 
			$(".errorClass").each(function() {
				showError($(this));// 遍历每个元素，使用每个元素来调用showError方法
			}
		
);*///错误演示
//注意jquery中调用方法都需要;结束，{}貌似不需要
$(function() {
	// 得到Class为errorClass的标签，然后遍历之
	$(".errorClass").each(function() {
		showError($(this));// 调用方法，将当前的对象作为参数传递过去
	});
	/*
	 * 得到id为submit的标签，然后当鼠标移入的时候将这个标签的src属性改成/goods/images/regist1.jpg
	 * 当鼠标移除的时候这个标签的src属性改成/goods/images/regist2.jpg
	 */
	$("#submit").hover(function() {
		$("#submit").attr("src", "/goods/images/regist1.jpg");
	}, function() {
		$("#submit").attr("src", "/goods/images/regist2.jpg");
	});
	/*
	 * 得到id为inputClass的标签，当得到焦点的时候先获得另一个label标签的id的值 然后
	 * 将这个id对应的标签里面的内容改成空，并且调用showError方法将样式也设置为空
	 */
	$(".inputClass").focus(function() {
		var labelId = "#" + $(this).attr("id") + "Error";
		$(labelId).text("");
		showError($(labelId));
	});

	/**
	 * 下面这个方法是当某个元素失去焦点的时候将这个元素的id得到然后通过拼接的方法
	 * 将这个元素失去焦点的时候所应该去调用的方法拼接出来，然后eval字符串调用相应的方法
	 * 注意拼接的方法的需要遵循驼峰表示法所以需要第二个单词的首字母大学，而js中可以通过sbustring 的方法设置
	 */
	$(".inputClass").blur(
			function() {
				var id = $(this).attr("id");
				var functionName = "validate"
						+ id.substring(0, 1).toUpperCase() + id.substring(1)
						+ "()";
				// alert(functionName);
				eval(functionName);
			});

	/**
	 * 下面这个方法是表单提交的时候给表单进行校验的，主要现在进行校验的话是因为这样子的话不需要访问 服务器这样子的话性能能够有所提高，
	 * 不过这个不是真的进行校验只是初步校验
	 */
	$("#registForm").submit(
			function() {
				if (validateLoginname() && validateLoginpass()
						&& validateReloginpass() && validateEmail()
						&& validateVerifyCode()) {
					return true;
				} else {
					return false;
				}
			});
});

/**
 * 下面的几个方法都差不多相同，主要步骤1.得到当前的标签的id然后 执行id得到这个标签的值，然后得到label的标签的id，如果当前
 * id的值不存在那个反馈不能为空信息，若是格式错误或者长度不够的 话那么就提示格式或者其他错误
 * 
 * @returns {Boolean}
 */

function validateLoginname() {
	var id = "loginname";
	var value = $("#" + id).val();
	var labelId = "#" + id + "Error";
	if (!value) {
		$(labelId).text("用户名不能为空");
		showError($(labelId));
		return false;
	}

	if (value.length < 3 || value.length > 16) {
		$(labelId).text("长度不能低于3或者大于16");
		showError($(labelId));
		return false;
	}

	/**
	 * 访问数据库校验用户名是否已经被注册，需要如下7个参数
	 */
	$.ajax({
		url : "/goods/UserServlet",// 访问的服务器端的地址
		data : {// 给出的参数注意需要{}括起来
			method : "ajaxValidateLoginname",
			loginname : value
		},//
		type : "POST",// post请求
		cache : false,// 不缓存
		async : false,// 不异步
		dataType : "json",// 返回类型为jason
		success : function(result) {// 回调方法
			if (!result) {
				$(labelId).text("用户名已经被注册");
				showError($(labelId));
				return false;
			}
		}
	});
	return true;
}

function validateLoginpass() {
	var id = "loginpass";
	var value = $("#" + id).val();
	var labelId = "#" + id + "Error";
	if (!value) {
		$(labelId).text("密码不能为空");
		showError($(labelId));
		return false;
	}

	if (value.length < 3 || value.length > 16) {
		$(labelId).text("长度不能低于3或者大于16");
		showError($(labelId));
		return false;
	}
	return true;
}

function validateReloginpass() {
	var id = "reloginpass";
	var value = $("#" + id).val();
	var labelId = "#" + id + "Error";
	if (!value) {
		$(labelId).text("请再次输入密码");
		showError($(labelId));
		return false;
	}

	if (value != $("#loginpass").val()) {
		$(labelId).text("两次输入的密码不同，请重新输入");
		showError($(labelId));
		return false;
	}
	return true;
}

function validateEmail() {
	var id = "email";
	var value = $("#" + id).val();
	var labelId = "#" + id + "Error";
	if (!value) {
		$(labelId).text("邮箱不能为空");
		showError($(labelId));
		return false;
	}
	// 邮箱的正则表达式需要这样子写
	if (!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
			.test(value)) {
		$(labelId).text("邮箱格式错误，请正确输入");
		showError($(labelId));
		return false;
	}
	/**
	 * 下面的则是校验email是否已经被注册
	 */
	$.ajax({
		url : "/goods/UserServlet",
		data : {
			method : "ajaxValidateEmail",
			email : value
		},
		type : "POST",
		dataType : "json",
		cache : false,
		async : false,
		success : function(result) {
			if (!result) {
				$(labelId).text("当前email已经被注册");
				showError($(labelId));
				return false;
			}
		}
	});
	return true;
}
function validateVerifyCode() {
	var id = "verifyCode";
	var value = $("#" + id).val();
	var labelId = "#" + id + "Error";
	if (!value) {
		$(labelId).text("验证码不能为空");
		showError($(labelId));
		return false;
	}
	// 邮箱的正则表达式需要这样子写
	if (value.length != 4) {
		$(labelId).text("验证码位数错误");
		showError($(labelId));
		return false;
	}

	$.ajax({
		url : "/goods/UserServlet",
		data : {
			method : "ajaxValidateVerifyCode",
			verifyCode : value
		},
		type : "POST",
		dataType : "json",
		cache : false,
		async : false,
		success : function(result) {
			if (!result) {
				$(labelId).text("验证码错误");
				showError($(labelId));
				return false;
			}
		}
	});
	return true;
}

// 如果传递来的元素的内容为空那么将这个元素的css样式设置为空
function showError(ele) {
	var text = ele.text();// 获取元素的内容
	if (!text) {// 如果没有内容
		ele.css("display", "none");// 隐藏元素
	} else {// 如果有内容
		ele.css("display", "");// 显示元素
	}
}
// 当点击换一张的时候将验证码给换掉
function change() {
	$("#vCode").attr("src", "/goods/VerifyCodeServlet?" + new Date().getTime());
}