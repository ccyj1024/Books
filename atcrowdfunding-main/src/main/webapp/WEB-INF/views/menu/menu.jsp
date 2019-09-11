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

    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
           <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 许可维护</a></div>
        </div>
         <%@ include file="/WEB-INF/include/admin_loginbar.jsp" %>
     
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
         <%@ include file="/WEB-INF/include/admin_menubar.jsp" %>
        
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

			<div class="panel panel-default">
              <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表 <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
			  <div class="panel-body">
                  <ul id="treeDemo" class="ztree"></ul>
			  </div>
			</div>
        </div>
      </div>
    </div>
    <!-- 添加菜单的模态框 -->
<div class="modal fade" id="addMenuModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="exampleModalLabel">添加菜单</h4>
      </div>
      <div class="modal-body">
        <form>
           <input type="hidden" class="form-control" name="pid">
          <div class="form-group">
            <label for="recipient-name" class="control-label">菜单名:</label>
            <input type="text" class="form-control" name="name">
          </div>
          <div class="form-group">
            <label for="recipient-name" class="control-label">菜单图标:</label>
            <input type="text" class="form-control" name="icon">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" id="saveMenuBtn">提交</button>
      </div>
    </div>
  </div>
</div>   
<!-- 修改菜单的模态框 -->
<div class="modal fade" id="updateMenuModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="exampleModalLabel">编辑菜单</h4>
      </div>
      <div class="modal-body">
        <form>
           <input type="hidden" class="form-control" name="id">
          <div class="form-group">
            <label for="recipient-name" class="control-label">菜单名:</label>
            <input type="text" class="form-control" name="name">
          </div>
          <div class="form-group">
            <label for="recipient-name" class="control-label">菜单图标:</label>
            <input type="text" class="form-control" name="icon">
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" id="updateMenuBtn">提交</button>
      </div>
    </div>
  </div>
</div>   
	
	
	<%
		request.setAttribute("menuname", "菜单维护");
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
			   
			    
				//$.fn.zTree.init($("#treeDemo"), setting); //异步访问数据
				//ztree需要解析显示树状图的json字符串
				initZtree();
            });
            function initZtree(){
            	 /*
		    	使用ztree的步骤
		    		1、导入ztree的文件到项目中[zTreeStyle.css、jquery.ztree.all-3.5.min.js、jquery.js]
		   			2、在需要使用ztree的页面中引入ztree样式和js文件[必须引入jquery]
		    		3、在页面中提供 ul容器：显示树状图的地方
		    		4、js代码中创建setting配置：决定ztree如何解析json字符串进行显示
		    		5、异步请求服务器获取 树状图需要的数据[json字符串]
		    		6、调用ztree的init方法解析数据显示到容器中
		    */
		    
		    //鼠标悬停时的方法
			function addHoverDom(treeId, treeNode){  
				var aObj = $("#" + treeNode.tId + "_a"); // tId = permissionTree_1, ==> $("#permissionTree_1_a")
				aObj.attr("href", "javascript:;");
				if (treeNode.editNameFlag || $("#btnGroup"+treeNode.tId).length>0) return;
				var s = '<span id="btnGroup'+treeNode.tId+'">';
				if ( treeNode.level == 0 ) {
					//添加按钮：添加时 需要提交当前的menu id  作为添加的menu的pid属性
					s += '<a onclick="addMenu('+treeNode.id+')" class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="javascript:void(0);" >&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
				} else if ( treeNode.level == 1 ) {
					//修改按钮： 修改时  需要提交当前的menu id  查询当前menu信息 根据id进行修改menu数据
					s += '<a onclick="updateMenu('+treeNode.id+')" class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;"  href="javascript:void(0);" title="修改权限信息">&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';
					if (treeNode.children.length == 0) {
						//删除按钮： 删除时 需要提交menu id  删除指定的menu
						s += '<a onclick="deleteMenu('+treeNode.id+' , \'' + treeNode.name+  '\')" class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="javascript:void(0);" >&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
					}
					//添加
					s += '<a onclick="addMenu('+treeNode.id+')" class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="javascript:void(0);" >&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
				} else if ( treeNode.level == 2 ) {
					//修改按钮
					s += '<a onclick="updateMenu('+treeNode.id+')" class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;"  href="javascript:void(0);" title="修改权限信息">&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';
					//删除按钮
					s += '<a  onclick="deleteMenu('+treeNode.id+' , \'' + treeNode.name+  '\')" class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="javascript:void(0);">&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
				}

				s += '</span>';
				aObj.after(s);
			}
		    
		    
		    //自定义ztree树图标的方法
		    function addDiyDom(treeId, treeNode) {
				//console.log("treeId: "+treeId);//treeId的值就是ztree树容器的id值
				//console.log(treeNode);
				//根据ztree生成图标的规则修改图标内容：
				// treeNode.tId + "_ico"   作为id  显示当前菜单节点的图标
				// treeNode.tId + "_span"  作为id  显示当前菜单的名称
				//删除正在生成的菜单的图标：只需要移除 id 为 treeNode.tId + "_ico"  的标签的 class样式即可
				// #treeDemo_1_ico
				$("#"+treeNode.tId+"_ico").removeClass();
				//treeNode是ztree由  menu对象生成的节点对象，包含了  menu的 icon属性： icon属性就是 字体图标对应节点的样式
				//$("#"+treeNode.tId+"_ico").addClass(treeNode.icon);
				$("#"+treeNode.tId + "_span").before("<span class='"+treeNode.icon+"'></span>");
			};
		    
            	
            	
            	//ztree的核心配置
				var setting = {
					data:{
						simpleData:{
							enable:true,  //开启ztree支持简单数据的功能
							pIdKey:"pid"  //指定 ztree解析简单json字符串时的  父节点id，ztree会自动组装数据为父子关系
						},
						key:{
							url:"asdasdas"
						}
					},
					view: {
						addDiyDom: addDiyDom,
						addHoverDom: addHoverDom,
						removeHoverDom: function(treeId, treeNode){
							$("#btnGroup"+treeNode.tId).remove();
						}
					}
				};
            	$.ajax({
					type:"POST",
					url:"${PATH}/menu/listMenus",
					success:function(menus){
						//给menus菜单手动添加一个根节点元素
						menus.push({"id":0 , "name":"系统菜单" , "icon":"glyphicon glyphicon-send"});
						console.log(menus);
						var d = menus;
						//$("#treeDemo") 显示树状图的容器
						//整合数据 
						var $ztreeObj = $.fn.zTree.init($("#treeDemo"), setting, d);//ztree树生成后，此方法会返回ztree树对应的jquery对象
						$ztreeObj.expandAll(true);
					}
				});
            }
          //增删改 的处理函数 : 由于js加载代码的机制  ，需要将创建的方法放到  $(function(){})外面
		    function addMenu(pid){
		    	//this:window对象
		    	//alert("添加方法："+pid);
		    	//return false;如果是直接给a标签或者 提交按钮绑定 单击事件 ，可以通过此方式取消默认行为
		    	//如果是在标签内通过onclick之类的属性值指定的调用方法，方法内不可以通过return false取消默认行为
		    	//将pid设置到添加菜单的模态框中
		    	$("#addMenuModal input[name='pid']").val(pid);
		  		//显示 添加菜单的模态框  
          		$("#addMenuModal").modal("show");
		  		$("#saveMenuBtn").click(function(){
		  			$.post("${PATH}/menu/saveMenu" , $("#addMenuModal form").serialize() ,function(data){
		  				if("ok"==data){
		  					//关闭模态框，给用户提示
		  					$("#addMenuModal").modal("hide");
		  					layer.msg("添加菜单成功",{time:2000});
		  					//刷新菜单树
		  					initZtree();
		  					//由于模态框上次的数据会自动缓存，每次保存成功后可以手动清空modal
		  					$("#addMenuModal form")[0].reset();
		  				}
		  			} );//发送保存菜单的请求
		  		});
		  		
          
          }
		    
		    function updateMenu(id){
		    	//alert("修改方法："+id);
		    	//查询要修改的menu数据
		    	$.post("${PATH}/menu/getMenu" , {id:id} , function(menu){
			    	//回显到模态框中
		    		$("#updateMenuModal input[name='id']").val(menu.id);
		    		$("#updateMenuModal input[name='name']").val(menu.name);
		    		$("#updateMenuModal input[name='icon']").val(menu.icon);
		    		
			    	//弹出模态框
			    	$("#updateMenuModal").modal("toggle");
		    	});
		    }
		    function deleteMenu(id , name){
		    	//获取删除的a标签所在行的  菜单名
		    	//alert("删除方法："+id);
		    	layer.confirm("你确定删除《"+name+"》吗？",{icon:3} , function(index){
		    		$.post("${PATH}/menu/deleteMenu" , {id:id} , function(data){
		    			if("ok"==data){
				    		layer.close(index);
		    				layer.msg("删除成功");
		    				//刷新 菜单树
		    				initZtree();
		    			}
		    		});
		    		
		    	});
		    }
		  	$("#updateMenuBtn").click(function(){
		  		$.post("${PATH}/menu/updateMenu" , $("#updateMenuModal form").serialize() , function(data){
		  			if(data=="ok"){
		  				//关闭模态框
		  				$("#updateMenuModal").modal("toggle");
		  				//刷新ztree树
		  				initZtree();
		  			}
		  		} );
		  	});  
        </script>
  </body>
</html>
