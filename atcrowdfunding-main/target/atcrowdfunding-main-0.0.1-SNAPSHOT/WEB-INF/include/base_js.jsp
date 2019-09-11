<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script src="jquery/jquery-2.1.1.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/docs.min.js"></script>
<script src="layer/layer.js"></script>
<script src="script/back-to-top.js"></script>
<script src="ztree/jquery.ztree.all-3.5.min.js"></script>

<%-- 
	<%@ include file="/WEB-INF/include/base_css.jsp" %>
	 <%@ include file="/WEB-INF/include/admin_loginbar.jsp" %>
	 <%@ include file="/WEB-INF/include/admin_menubar.jsp" %>
	<%@ include file="/WEB-INF/include/base_js.jsp" %>
	
--%>
<script type="text/javascript">
//注销按钮发送异步请求的方法
$("#logoutA").click(function(){
	//1、弹出注销确认提示
	layer.confirm("你真的要退出吗?" , {icon:2 , title:"确定提示"} , function(){
		layer.msg('正在注销中...', {
			  icon: 16
			  ,shade: 0.01
		});
    	//2、如果选择确定：提交异步的注销请求让服务器销毁session对象
		$.post("${PATH}/logout" , function(data){
			setTimeout(function(){
				if(data=="ok"){
    				layer.msg("注销成功...");
    				setTimeout(function(){
		            	//3、如果异步请求的结果返回成功，让浏览器跳转到首页
    					window.location = "${PATH}/index";
    				} , 1500);
    			}
			} , 2000);
		})
		
	});
	//取消a标签的默认行为
	return false;
});

	$(".tree li a:contains('${menuname}')").css("color" , "red");//当前页面所在的菜单高亮显示
	$("li a:contains('${menuname}')").parents("li.tree-closed").removeClass("tree-closed");//移除当前a标签所在的父菜单标签的  关闭的class值
		//用户维护所在的ul 样式是none，需要修改为block显示出来：   css样式： display:none , block
	$("li a:contains('${menuname}')").parents("ul:hidden").show();//显示隐藏的 当前页面菜单所在的ul
</script>