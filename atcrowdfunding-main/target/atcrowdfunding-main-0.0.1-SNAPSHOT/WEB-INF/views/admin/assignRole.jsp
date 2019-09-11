<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

	<%@ include file="/WEB-INF/include/base_css.jsp" %>
	<style>
	.tree li {
        list-style-type: none;
		cursor:pointer;
	}
	</style>
  </head>

  <body>
	<%-- ${assignRoles }
	<br/>
	${unAssignRoles } --%>
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="user.html">众筹平台 - 用户维护</a></div>
        </div>
        <%@ include file="/WEB-INF/include/admin_loginbar.jsp" %>
      
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <%@ include file="/WEB-INF/include/admin_menubar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<ol class="breadcrumb">
				  <li><a href="#">首页</a></li>
				  <li><a href="#">数据列表</a></li>
				  <li class="active">分配角色</li>
				</ol>
			<div class="panel panel-default">
			  <div class="panel-body">
				<form role="form" class="form-inline">
				  <div class="form-group">
					<label for="exampleInputPassword1">未分配角色列表</label><br>
					<select class="form-control unassignSelect" multiple size="10" style="width:400px;overflow-y:auto;">
                        <c:forEach items="${unAssignRoles }" var="role">
	                        <option value="${role.id }">${role.name }</option>
                        </c:forEach>
                    </select>
				  </div>
				  <div class="form-group">
                        <ul>
                            <li id="addRolesToAdminBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                            <br>
                            <li id="removeRolesToAdminBtn" class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top:20px;"></li>
                        </ul>
				  </div>
				  <div class="form-group" style="margin-left:40px;">
					<label for="exampleInputPassword1">已分配角色列表</label><br>
					<select class="form-control assignSelect" multiple size="10" style="width:400px;overflow-y:auto;">
                        <c:forEach items="${assignRoles }" var="role">
	                        <option value="${role.id }">${role.name }</option>
                        </c:forEach>
                    </select>
				  </div>
				</form>
			  </div>
			</div>
        </div>
      </div>
    </div>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-dialog">
		<div class="modal-content">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">帮助</h4>
		  </div>
		  <div class="modal-body">
			<div class="bs-callout bs-callout-info">
				<h4>测试标题1</h4>
				<p>测试内容1，测试内容1，测试内容1，测试内容1，测试内容1，测试内容1</p>
			  </div>
			<div class="bs-callout bs-callout-info">
				<h4>测试标题2</h4>
				<p>测试内容2，测试内容2，测试内容2，测试内容2，测试内容2，测试内容2</p>
			  </div>
		  </div>
		  <!--
		  <div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			<button type="button" class="btn btn-primary">Save changes</button>
		  </div>
		  -->
		</div>
	  </div>
	</div>
	<!-- 设置高亮显示的 菜单名 -->
	<%
		pageContext.setAttribute("menuname", "用户维护");
		
	%>
    <%@ include file="/WEB-INF/include/base_js.jsp" %>
        <script type="text/javascript">
            $(function () {
			    $(".list-group-item").click(function(){
				    if ( $(this).find("ul") ) {
						$(this).toggleClass("tree-closed");
						if ( $(this).hasClass("tree-closed") ) {
							$("ul", this).hide("fast");
						} else {
							$("ul", this).show("fast");
						}
					}
				});
            });
            
            //给分配角色的按钮绑定单击事件
            $("#addRolesToAdminBtn").click(function(){
            	//获取选中的所有的未分配的option
            	var $selectedOpts = $(".unassignSelect option:selected");
            	//将选中的角色的id  和 要添加角色的管理员id提交给服务器。将数据存到数据库t_admin_role表中
            	var roleIdArr = new Array();
            	$.each($selectedOpts , function(){
            		roleIdArr.push(this.value);
            	});
            	var roleIdsStr = roleIdArr.join(",")
            	var adminId = "${param.adminId}";//之前获取的数据都是字符串 ，js中字符串需要使用引号引起
            	//将参数提交给服务器处理
            	$.post("${PATH}/admin/assignRolesToAdmin" , {adminId: adminId , roleIdsStr: roleIdsStr} , function(data){
            		if(data=="ok"){
		            	//将未分配的选中的option移动到右边分配角色的列表中
		            	$(".assignSelect").append($selectedOpts);
            		}
            	});
            	
            });
            
            //给删除角色的按钮绑定单击事件
             $("#removeRolesToAdminBtn").click(function(){
            	//获取选中的所有的已分配的option
            	var $selectedOpts = $(".assignSelect option:selected");
            	//获取选中的需要删除的角色的id
            	var roleIdsParamStr = "";
            	$.each($selectedOpts , function(){
            		roleIdsParamStr+="roleIds="+this.value+"&";
            	});
            	//  2 3 4  ===      roleIds=2&roleIds=3&roleIds=4&
            	roleIdsParamStr = roleIdsParamStr.substring(0 , roleIdsParamStr.length-1);
            	//提交请求给服务器
            	var adminId = "${param.adminId}";
            	roleIdsParamStr +="&adminId="+adminId;
            	$.post("${PATH}/admin/deleteRolesToAdmin" ,roleIdsParamStr ,  function(data){
            		if("ok"==data){
		            	//将未分配的选中的option移动到右边分配角色的列表中
		            	$(".unassignSelect").append($selectedOpts);
            		}
            	});
            	
            });
            
            
            
        </script>
  </body>
</html>
