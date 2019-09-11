<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <%@ include file="/WEB-INF/include/base_css.jsp" %>
	<style>

	</style>
  </head>
  <body>
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
      </div>
    </nav>

    <div class="container">

      <form class="form-signin" role="form" method="post">
      	<div style="color: red;">${errorMsg }</div>
        <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i> 用户登录</h2>
		  <div class="form-group has-success has-feedback">
			<input type="text" class="form-control" name="loginacct" id="inputSuccess4" placeholder="请输入登录账号" autofocus>
			<span class="glyphicon glyphicon-user form-control-feedback"></span>
		  </div> 
		  <div class="form-group has-success has-feedback">
			<input type="text" class="form-control" name="userpswd" id="inputSuccess4" placeholder="请输入登录密码" style="margin-top:10px;">
			<span class="glyphicon glyphicon-lock form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<select class="form-control" >
                <option value="member">会员</option>
                <option value="user">管理</option>
            </select>
		  </div>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> 记住我
          </label>
          <br>
          <label>
            忘记密码
          </label>
          <label style="float:right">
            <a href="reg.html">我要注册</a>
          </label>
        </div>
        <a class="btn btn-lg btn-success btn-block" onclick="dologin()" > 登录</a>
      </form>
    </div>
    <%@ include file="/WEB-INF/include/base_js.jsp" %>
    <script>
    function dologin() {
       // var type = $(":selected").val();
        //登录按钮被点击时，希望验证账号密码
        var loginacct = $("input[name='loginacct']").val();
        var userpswd = $("input[name='userpswd']").val();
        var loginacctReg = /^[a-z0-9_-]{3,16}$/;
        var userpswdReg = /^[a-z0-9_-]{6,18}$/;
        if(!loginacctReg.test(loginacct)){
        	layer.msg("请输入正确格式的账号!" , {icon:2,time:3000});
        	return;
        }
        if(!userpswdReg.test(userpswd)){
        	layer.msg("请输入正确格式的密码!" , {icon:5,time:3000});
        	return;
        }
        //账号密码格式无误，提交登录请求
       // window.location = "/doLogin";//修改浏览器地址栏地址属性值，浏览器会自动向新地址发起请求
       $("form").prop("action" , "${PATH}/login").submit();//prop():获取或设置标签自带属性     attr()：获取或设置标签自定义属性
    }
    </script>
  </body>
</html>