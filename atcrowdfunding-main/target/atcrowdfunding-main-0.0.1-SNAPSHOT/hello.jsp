<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- 引入bootstrap css样式文件 -->
<!-- 获取上下文路径的需求较多，每次获取比较麻烦
	可以创建一个ServletContextListener：在服务器启动时  将上下文路径存到 application域中，指定简单的key
 -->
<link rel="stylesheet" type="text/css"
	href="${PATH }/static/bootstrap/css/bootstrap.min.css" />
</head>
<body>
	<!-- 测试 BootStrap的使用
			提供了n多 样式，只需要在我们的标签中的class属性值内引入  样式对应的class值  ，bootstrap会自动将样式添加给标签
		使用步骤：
			1、将bootstrap的css和 js拷贝到项目中
				bootstrap.min.css
				bootstrap.min.js(依赖了jquery)
				还需要导入jquery
			2、在需要使用bootstrap的页面中引入css和js文件
			
		BootStrap的核心：
			响应式布局框架： 会自动根据分辨率调整每一行显示的内容
		
	 -->

	<table class="table table-hover">
		<tr>
			<td>222</td>
			<td>333</td>
			<td>444</td>
			<td>5555</td>
		</tr>
		<tr>
			<td>222</td>
			<td>333</td>
			<td>444</td>
			<td>5555</td>

		</tr>
		<tr>
			<td>222</td>
			<td>333</td>
			<td>444</td>
			<td>5555</td>

		</tr>
		<tr>
			<td>222</td>
			<td>333</td>
			<td>444</td>
			<td>5555</td>

		</tr>

	</table>
	<!-- 响应式布局的内容需要写在 容器中(container)  -->
	<div class="container">
		<div class="row">
			<!-- 如果是最低分辨率一行显示1列 ，  如果是小分辨率显示2列 ， 如果是中等分辨率一行显示4列 ， 如果是最大分辨率一行显示8列 -->
			<div class="col-xs-12 col-sm-6 col-md-3 col-lg-2">11</div>
			<div class="col-xs-12 col-sm-6 col-md-3  col-lg-2">12</div>
			<div class="col-xs-12 col-sm-6 col-md-3  col-lg-2">13</div>
			<div class="col-xs-12 col-sm-6 col-md-3  col-lg-2">14</div>
			<div class="col-xs-12 col-sm-6 col-md-3  col-lg-1">15</div>
			<div class="col-xs-12 col-sm-6 col-md-3  col-lg-1">16</div>
			<div class="col-xs-12 col-sm-6 col-md-3  col-lg-1">17</div>
			<div class="col-xs-12 col-sm-6 col-md-3  col-lg-1">18</div>
		</div>
		<div class="row">
			<div></div>
		</div>
		<div class="row">
			<div></div>
		</div>
		<div class="row">
			<div></div>
		</div>
	</div>




	<!-- 在页尾引入jquery和 boostrap的 js文件 ，不会阻塞浏览器解析页面中的内容
	 		href: 引用 ，用一下，引用失败不会影响页面代码的解析    window.onload 不会等待href引用的资源
	 		src: 引入 ， 将文件下载之后再使用，引入失败可能会造成页面代码执行出错  ，  window.onload 会等待引入的资源加载完毕
	 
	  -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/static/jquery/jquery-2.1.1.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath }/static/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${PATH }/static/layer/layer.js"></script>

	<script type="text/javascript">
		//alert("hehe");
		//alert弹出确认
		//layer.alert("hehe");
		//msg弹出提示信息  参数1：提示框中的文本 ， 参数2：js对象   time 显示的时间  单位毫秒 ， btn：显示的按钮  ,icon:显示图标
		/* layer.msg('你确定你很帅么？', {
				time : 10000 ,//不自动关闭
				icon: 1
		}); */
		//confirm 弹出确认框
		/* layer.confirm("你真的要删除吗?" , {icon:2} , function(index){//index代表当前弹出的纵轴 索引
			//点击确定的操作函数 
			alert("hehe");
			layer.close(index);//关闭指定索引的 弹层
		}); */
		//load 进度条
		var index = layer.load(2 , { time:50000});
		//setTimeout 延迟执行传入的方法：参数1：方法 ，  参数2：延迟的时间
		setTimeout(function(){
			layer.close(index);
		}, 2000);
	</script>

</body>
</html>