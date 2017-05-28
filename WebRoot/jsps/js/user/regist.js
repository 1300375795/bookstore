/*$(
		function() {
			
 * 1. 得到所有的错误信息，循环遍历之。调用一个方法来确定是否显示错误信息！
			 
			$(".errorClass").each(function() {
				showError($(this));// 遍历每个元素，使用每个元素来调用showError方法
			}
		
);*///错误演示
//注意jquery中调用方法都需要;结束，{}貌似不需要
$(function() {
	$(".errorClass").each(function() {
		showError($(this));
	});

});

function showError(ele) {
	var text = ele.text();// 获取元素的内容
	if (!text) {// 如果没有内容
		ele.css("display", "none");// 隐藏元素
	} else {// 如果有内容
		ele.css("display", "");// 显示元素
	}
}

function change() {
	$("#vCode").attr("src", "/goods/VerifyCodeServlet?" + new Date().getTime());
}