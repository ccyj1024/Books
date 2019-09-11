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
	table tbody tr:nth-child(odd){background:#F4F4F4;}
	table tbody td:nth-child(even){color:#C00;}
	</style>
  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
          <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 角色维护</a></div>
        </div>
       <%@ include file="/WEB-INF/include/admin_loginbar.jsp" %>
	
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <%@ include file="/WEB-INF/include/admin_menubar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<div class="panel panel-default">
			  <div class="panel-heading">
				<h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
			  </div>
			  <div class="panel-body">
<form class="form-inline" role="form" style="float:left;">
  <div class="form-group has-feedback">
    <div class="input-group">
      <div class="input-group-addon">查询条件</div>
      <input type="hidden" name="pageNum" />
      <input name="condition" class="form-control has-success" type="text" placeholder="请输入查询条件">
    </div>
  </div>
  <button type="button" class="queryBtn btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
</form>
<button type="button" class="deleteRolesBtn btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
<button type="button" onclick="showAddRoleModal()" class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-plus"></i> 新增</button>
<br>
 <hr style="clear:both;">
          <div class="table-responsive">
            <table class="table  table-bordered">
              <thead>
                <tr >
                  <th width="30">#</th>
				  <th width="30"><input type="checkbox"></th>
                  <th>名称</th>
                  <th width="100">操作</th>
                </tr>
              </thead>
              <tbody>
                
                
              </tbody>
			  <tfoot>
			     <tr >
				     <td colspan="6" align="center">
						<ul class="pagination">
								
						</ul>
					 </td>
				 </tr>

			  </tfoot>
            </table>
          </div>
			  </div>
			</div>
        </div>
      </div>
    </div>
    <!-- 模态框：  bootStrap的弹层插件，可以在页面中弹出一个小的对话框，对话框中可以自定义标签 
    	使用步骤：
    		1、将bootstrap拷贝到项目中  在页面中引入  样式和 js
    		2、将模态框代码拷贝到页面中
    		3、可以通过单击事件  操作模态框
        -->
   <!-- 添加角色的模态框 -->
  <div class="modal fade" id="addRoleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="exampleModalLabel">新增角色</h4>
      </div>
      <div class="modal-body">
        <form>
          <div class="form-group">
            <input type="hidden" class="form-control" name="pages">
            <label for="recipient-name" class="control-label">角色名:</label>
            <input type="text" class="form-control" name="name">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="saveRole()">提交</button>
      </div>
    </div>
  </div>
</div>
   <!-- 修改角色的模态框 -->
  <div class="modal fade" id="updateRoleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="exampleModalLabel">修改角色</h4>
      </div>
      <div class="modal-body">
        <form>
          <div class="form-group">
            <input type="hidden" class="form-control" name="id">
            <input type="hidden" class="form-control" name="pageNum">
            <label for="recipient-name" class="control-label">角色名:</label>
            <input type="text" class="form-control" name="name">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" id="updateRoleBtn">修改</button>
      </div>
    </div>
  </div>
</div>   
   <!-- 给角色分配权限的模态框(显示权限树) -->
  <div class="modal fade" id="assignPermissionsModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="exampleModalLabel">分配权限</h4>
      </div>
      <div class="modal-body">
      	<input type="hidden" name="roleId"/>
      	<!-- ztree树容器 -->
        <ul id="treeDemo" class="ztree"></ul>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消分配</button>
        <button type="button" class="btn btn-primary" id="assignPermissionsBtn">确认分配</button>
      </div>
    </div>
  </div>
</div>   
    
		<%
			request.setAttribute("menuname", "角色维护");
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
            
            //给新增按钮绑定单击事件  点击时显示模态框
            function showAddRoleModal(){
            	$("#addRoleModal").modal('toggle');
            }
            //给模态框的  提交按钮绑定事件  点击时 提交保存请求给服务器
            function saveRole(){
            	var roleName = $("#addRoleModal input[name='name']").val();
            	$.ajax({
            		type:"POST",
            		url:"${PATH}/role/saveRole",
            		data: {"name":roleName},
            		//dataType:"text",
            		success: function(result){
            			if(result=="ok"){
            				//保存成功  关闭模态框  让页面角色的分页数据显示最后一页
            				$("#addRoleModal").modal('toggle');
            				//获取总页码
            				var pages = $("#addRoleModal input[name='pages']").val();
            				 initRolesTable(pages+1);//防止添加完毕后又多了一页， 之前的总页码就失效了
            			}
            		}
            	});
            }
            //弹出修改角色的模态框按钮
            function updateRoleModel(roleId){
            	//1、根据角色id查询角色信息
            	$.post("${PATH}/role/getRole" , {id:roleId} , function(role){
	            	//2、查询后 将角色信息回显到 修改的模态框中[直接回显 角色名 ， 隐藏回显角色id(修改时服务器需要根据id确定一条记录)]
	            	$("#updateRoleModal input[name='name']").val(role.name);
	            	$("#updateRoleModal input[name='id']").val(role.id);
	            	//3、显示模态框
	            	$("#updateRoleModal").modal("toggle");
            	})
            }
            //给修改按钮绑定单击事件  点击时 提交请求给服务器修改角色信息
            $("#updateRoleBtn").click(function(){
            	//1、获取修改后的角色信息[id/name]
            	var name = $("#updateRoleModal input[name='name']").val();
            	var id = $("#updateRoleModal input[name='id']").val();
            	//2、发送修改的ajax请求让服务器修改
            	$.post("${PATH}/role/updateRole" , {"name":name , "id":id} ,function(result){
            		if("ok"==result){//=== 判断值和类型   ，== 只判断值
		            	//3、修改后如果响应成功，关闭模态框   刷新当前页  显示修改之后的内容
            			$("#updateRoleModal").modal("toggle");
            			//刷新当前页?
            			var pageNum = $("#updateRoleModal input[name='pageNum']").val();	
            			//获取条件
            			var condition =$("input[name='condition']").val();
            			initRolesTable(pageNum,condition);
            		}
            	});
            });
            //========================给角色分配权限按钮的单击事件====================================
           	var $ztreeObj;//提升局部变量的作用域
            function assignPermissionsToRole(roleId){
           		//获取到角色id时 可以将角色id在页面中指定标签中存储，当点击确认分配时 可以获取使用
           		$("#assignPermissionsModal input[name='roleId']").val(roleId);
           		
            	//查询所有的权限集合
            	$.post("${PATH}/permission/listPermissions" , function(permissions){
            		console.log(permissions);
	            	//使用ztree 将权限树显示到模态框中
	            	//1、将ztree拷贝到项目中 并在需要使用的页面中引入  ztree样式和js文件
	            	//2、在页面中准备ztree树 显示需要使用的容器(在模态框中)： <ul></ul>
	            	//3、准备ztree需要的数据：  json字符串、settings配置
	            	var setting = {
	            			data: {
	            				key: {
	            					name: "title"
	            				},
	            				simpleData: {
	            					enable: true,
	            					pIdKey: "pid",
	            				}
	            			},
	            			view: {
	            				addDiyDom: addDiyDom
	            			},
	            			check: {
	            				enable: true
	            			}
	            	};
	            	//自定义 权限图标的方法
	            	function addDiyDom(treeId, treeNode) {
	            		$("#"+treeNode.tId +"_ico").removeClass();
	            		$("#"+treeNode.tId+"_span").before("<span class='"+treeNode.icon+"'></span>");
	            	};
	            	//查询当前角色所拥有的权限id集合[根据角色id 去 角色权限表中查询 所拥有的权限id集合]
	            	$.post("${PATH}/permission/getPermissionIdsByRoleId" , {roleId:roleId} , function(assignPermissionIds){
	            		//让ztree解析的节点json字符串中，只要一个节点元素 checked属性为true，ztree解析时会设置默认勾选
	            		//遍历所有的权限集合，如果正在遍历的权限节点  的id值 在assignPermissionIds中存在，那么就可以给该元素提供checked=true的属性
		            	
	            		$.each(permissions , function(){
	            			//this.checked = true;//修改正在遍历元素的checked属性为true
	            			if(assignPermissionIds.indexOf(this.id)>-1){
	            				//已分配的权限id中有正在遍历的权限
	            				this.checked = true;
	            			}
	            		});
	            		$ztreeObj = $.fn.zTree.init($("#treeDemo"), setting, permissions);
		            	$ztreeObj.expandAll(true);
		            	//弹出模态框
		            	$("#assignPermissionsModal").modal("toggle");
	            	});
	            	
	            	
            	});
            }	
            
            $("#assignPermissionsBtn").click(function(){
            	//1、获取所有的被选中的权限id列表
            	var $checkedNodes = $ztreeObj.getCheckedNodes(); 
            	//console.log($checkedNodes);
            	//将权限id拼接成参数字符串提交给服务器
            	var permissionIdsParam = "";
            	$.each($checkedNodes , function(){
            		permissionIdsParam+="pids="+this.id+"&";
            	})
            	permissionIdsParam = permissionIdsParam.substring(0 , permissionIdsParam.length-1);
            	//获取分配权限的角色id
            	var roleId = $("#assignPermissionsModal input[name='roleId']").val();
            	console.log(roleId);
            	//2、将权限id和 角色id提交给服务器：保存到t_role_permission表中
            	var paramStr = "roleId="+roleId+"&"+permissionIdsParam;
            	$.post("${PATH}/permission/assignPermissionsToRole" , paramStr , function(result){
            		if(result=="ok"){
            			//关闭模态框
            			$("#assignPermissionsModal").modal("toggle");
            		}
            	});
            	
            });
            
            
            //===============================================
            
            $("tbody .btn-success").click(function(){
                window.location.href = "assignPermission.html";
            });
            //页面加载完成立即调用初始化角色列表的方法
            initRolesTable(1);
			
            //===================给分页导航栏可以点击的超链接绑定单击事件===================
            //如果是通过同步的方式绑定事件，不能够直接给ajax代码中后创建的标签绑定事件
            //可以通过动态委派事件  delegate();    祖先元素.delegate("要动态委派事件的后代元素" , "事件列表" , function(){事件处理函数})
           	$("tfoot ul").delegate(".navA" , "click" , function(){
           		//获取被点击的a标签的pageNum属性值
           		var pageNum = $(this).attr("pageNum");
           		//无论有没有查询条件，都获取查询条件输入框中的内容 提交给服务器
				var condition = $("input[name='condition']").val();           		
           		initRolesTable(pageNum, condition);
           		return false;
           	})
           	
           	//给条件查询的按钮绑定单击事件，点击时  获取条件发送给服务器处理
           	$(".queryBtn").click(function(){
           		//获取查询条件
           		var condition = $("input[name='condition']").val();
           		initRolesTable(1 , condition);
           		
           	});
           	
           	
           	
           	
           	
           	
           	
           	
            //提取发送ajax请求role列表 并解析 role列表到表格 中以及生成分页导航栏的代码
            function initRolesTable(pageNum , condition){
            	$("input[name='pageNum']:hidden").val(pageNum);
                //当页面解析到当前行时 直接发送ajax请求获取角色列表json字符串 解析显示到页面中
                $.post("${PATH}/role/listRoles" ,{"pageNum":pageNum , "condition":condition} , function(pageInfo){
                	if(pageInfo=="unauth"){
                		layer.msg("您还没有权限访问当前页面，请联系管理员");
                		return;
                	}
	                	//将pageInfo的  总页码属性存储到模态框中 ，当保存角色后可以使用
	                	$("#addRoleModal input[name='pages']").val(pageInfo.pages);
	                	//将当前页码设置到修改的模态框中   修改后可以刷新pageNum页
	                	$("#updateRoleModal input[name='pageNum']").val(pageInfo.pageNum);
	                	
	                	console.log(pageInfo);
	                	//=========================解析 分页的角色列表显示到tbody中===================================
	                	//查找显示 role列表的  表格 的tbody
	                	var $tbody = $(".table tbody");//jquery对象推荐使用$开始命令
	                	//清除tbody之前的 生成的表格行数据
	                	$tbody.empty();
	                	$.each(pageInfo.list , function(index){// index代表正在遍历元素的索引
	                		//this代表正在遍历的一个  role json对象  数据显示到一个tr中,再将tr追加给tbody
	                		/*
	                		<tr>
	    	                  <td>1</td>
	    					  <td><input type="checkbox"></td>
	    	                  <td>PM - 项目经理</td>
	    	                  <td>
	    					      <button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>
	    					      <button type="button" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>
	    						  <button type="button" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>
	    					  </td>
	    	                </tr>
	    	                \ 代表 一个字符串没有写完整  下一行的内容继续拼接
	                		*/
	                		$("<tr></tr>").append('<td>'+(index+1)+'</td>')
	                					.append('<td><input roleId="'+this.id+'" type="checkbox"></td>')
	                					.append('<td>'+this.name+'</td>')
	                					.append('<td>\
	                						      <button type="button"  onclick="assignPermissionsToRole('+this.id+')"   class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>\
	                						      <button type="button" onclick="updateRoleModel('+this.id+')"  class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>\
	                							  <button  onclick="deleteRole('+this.id+' , this)" type="button" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>\
	                						  </td>')
	                		.appendTo($tbody);
	                	});
	                	//===============================角色列表解析完成=================================================
	                	//===============================分页导航栏开始解析 显示到tfoot的ul中=================================================
	               		/* 
	               			<li class="disabled"><a href="#">上一页</a></li>
	    					<li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>
	    					<li><a href="#">2</a></li>
	    					<li><a href="#">3</a></li>
	    					<li><a href="#">4</a></li>
	    					<li><a href="#">5</a></li>
	    					<li><a href="#">下一页</a></li>
	               		
	               		*/
	                	var $navul = $("table tfoot ul");
	               		//清除之前生成的分页导航栏信息
	               		$navul.empty();
	               		//上一页
	               		if(pageInfo.isFirstPage){
	               			$('<li class="disabled"><a href="javascript:void(0);">上一页</a></li>').appendTo($navul);
	               		}else{
	               			$('<li><a class="navA" pageNum="'+(pageInfo.pageNum-1)+'" href="${PATH}/role/listRoles">上一页</a></li>').appendTo($navul);
	               		}
	               		//具体页码的拼接
	               		$.each(pageInfo.navigatepageNums , function(){
	               			//this
	               			if(this==pageInfo.pageNum){
	               				//当前页
	    	           			$('<li class="active"><a href="javascript:void(0);">'+this+' <span class="sr-only">(current)</span></a></li>').appendTo($navul);
	               			}else{
	    	           			$('<li><a class="navA" pageNum="'+this+'" href="${PATH}/role/listRoles">'+this+'</a></li>').appendTo($navul);
	               				
	               			}
	               		});
	               		//下一页
	               		if(pageInfo.isLastPage){
	               			$('<li class="disabled"><a  href="javascript:void(0);">下一页</a></li>').appendTo($navul);
	               		}else{
	               			$('<li><a class="navA" pageNum="'+(pageInfo.pageNum+1)+'" href="${PATH}/role/listRoles">下一页</a></li>').appendTo($navul);
	               		}
	                });
            }
            
            //处理删除按钮的单击事件的方法
            function deleteRole(roleId , btn){
            	//alert(roleId);
            	layer.confirm("你真的要删除吗?" , {icon:3 , title:"提示:"} , function(index){
            		layer.close(index);
	            	//1、提交删除请求  让服务器删除id对应的role
	            	$.post("${PATH}/role/deleteRole" , {id:roleId} , function(data){
	            		if(data=="ok"){
			            	//2、服务器删除成功响应成功时，删除页面中的被点击标签所在的行
			            	$(btn).parents("tr").remove();
			            	//3、判断当前页面tbody中的行数，如果没有了  需要重新请求上一页数据
			            	if($("table tbody tr").length == 0 ){
			            		//访问pageNum的上一页，因为当前页没有数据了
			            		var pageNum = $("input[name='pageNum']:hidden").val()-1;
			            		var condition = $("input[name='condition']").val();
			            		initRolesTable(pageNum , condition);
			            	}
	            		}
	            	})
            	});
            	
            };
            
            
            //===========批量删除========
            $("table thead :checkbox").click(function(){
            	//全选框 点击后控制 表格所有行的子复选框  状态和它一样
            	$(".table tbody :checkbox").prop("checked" , this.checked);
            });	
            //当子复选框被点击时  需要判断如果所有子复选框都被选中  则设置全选框选中
            //委派事件：标签是ajax后生成的
            $(".table tbody").delegate(":checkbox" , "click" , function(){
            	$("table thead :checkbox").prop("checked" ,$(".table tbody :checkbox").length==$(".table tbody :checkbox:checked").length);
            });
            
            //给批量删除的按钮绑定单击事件
            $(".deleteRolesBtn").click(function(){
            	layer.confirm("真的要删除吗?" , {icon:3} , function(index){
            		layer.close(index);
	            	//获取选中的所有的子复选框
	            	var $checked = $(".table tbody :checkbox:checked");
	            	if($checked.legnth==0){
	            		layer.msg("请选择要删除的角色");
	            		return ;
	            	}
	            	var ids = "";
	            	//拼接参数列表
	            	$.each($checked , function(){
	            		//this代表复选框 ， 需要获取  复选框所在行的 角色id
	            		var roleId = $(this).attr("roleId");
	            		ids +="ids="+roleId+"&";
	            	});
	            	// ids=1&ids=2&ids=3&
	            	//将多余的&截取掉
	            	ids = ids.substring(0 , ids.length-1);
	            	//发送ids到服务器处理删除请求
	            	$.post("${PATH}/role/deleteRoles" , ids , function(result){
						if(result=="ok"){
		            		//删除成功刷新页面
							var pageNum = $("#updateRoleModal input[name='pageNum']").val();	
	            			//获取条件
	            			var condition =$("input[name='condition']").val();
	            			initRolesTable(pageNum,condition);			
						}	            		
	            	});
            	});
            });
            
            
            
            
            
        </script>
  </body>
</html>
