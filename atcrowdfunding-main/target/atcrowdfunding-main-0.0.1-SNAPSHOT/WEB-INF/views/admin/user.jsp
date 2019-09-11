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
          <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 用户维护</a></div>
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
<form action="${PATH }/admin/index.html" class="form-inline" role="form" style="float:left;" method="post">
  <div class="form-group has-feedback">
    <div class="input-group">
      <div class="input-group-addon">查询条件</div>
      <input class="form-control has-success" name="condition" type="text" value="${param.condition }" placeholder="请输入查询条件">
    </div>
  </div>
  <button type="submit" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
</form>
<button type="button" class="deleteAdminsBtn btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
<button type="button" class="btn btn-primary" style="float:right;" onclick="window.location.href='${PATH}/admin/add.html'"><i class="glyphicon glyphicon-plus"></i> 新增</button>
<br>
 <hr style="clear:both;">
          <div class="table-responsive">
            <table class="table  table-bordered">
              <thead>
                <tr >
                  <th width="30">#</th>
                  <!-- 全选复选框 -->
				  <th width="30"><input type="checkbox"></th>
                  <th>账号</th>
                  <th>名称</th>
                  <th>邮箱地址</th>
                  <th width="100">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:choose>
              		<c:when test="${empty pageInfo.list }">
              		 	<tr>
		                  <td colspan="6">没有查询到用户!!!!</td>
		                </tr>
              		</c:when>
              		<c:otherwise>
              			<!-- varStatus:当前遍历的状态，jstl foreach标签开始遍历时会创建一个对象存储正在遍历的状态，每次遍历会更新它的属性值
              					isFirst：
              					isLast：
              					count:已经遍历元素的个数，从1开始计算
              					index:正在遍历元素的索引：从0开始计算
              					current:正在遍历的对象
              			PageInfo{pageNum=1, pageSize=3, size=3, startRow=1, endRow=3, total=10, pages=4, 
              				list=Page{count=true, pageNum=1, pageSize=3, startRow=0, endRow=3, total=10, pages=4, reasonable=true, pageSizeZero=false},
              				 prePage=0, nextPage=2, isFirstPage=true, isLastPage=false, hasPreviousPage=false, hasNextPage=true, 
              				 navigatePages=3, navigateFirstPage1, navigateLastPage3, navigatepageNums=[1, 2, 3]}
              			 -->
              			<c:forEach items="${pageInfo.list }" var="admin" varStatus="vs">
              				<tr>
			                  <td>${vs.count }</td>
			                  <!-- 子复选框  对应一个管理员记录 -->
							  <td><input adminId="${admin.id }" type="checkbox"></td>
			                  <td>${admin.loginacct }</td>
			                  <td>${admin.username }</td>
			                  <td>${admin.email }</td>
			                  <td>
							      <button adminId="${admin.id }" type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>
							      <button adminId="${admin.id }"  type="button" class="editAdminBtn btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>
								  <button adminId="${admin.id }" type="button" class="deleteBtn btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>
							  </td>
			                </tr>
              			</c:forEach>
              		</c:otherwise>
              	</c:choose>
              </tbody>
			  <tfoot>
			     <tr >
				     <td colspan="6" align="center">
						<ul class="pagination">
								<!-- 上一页 -->
								<c:choose>
									<c:when test="${pageInfo.isFirstPage }">
										<li class="disabled"><a onclick="return false;" href="#">上一页</a></li>
									</c:when>
									<c:otherwise>
										<!-- 有上一页 -->
										<li><a class="navA" href="${PATH }/admin/index.html?pageNum=${pageInfo.pageNum-1}">上一页</a></li>
									</c:otherwise>
								</c:choose>
								
								<!-- 具体页码列表 -->
								<c:forEach items="${pageInfo.navigatepageNums }" var="index">
									<c:choose>
										<c:when test="${pageInfo.pageNum==index }">
											<!-- 正在遍历的页码是当前页 -->
											<li class="active"><a onclick="return false;" href="javascript:void(0)">${index } <span class="sr-only">(current)</span></a></li>
										</c:when>
										<c:otherwise>
											<li><a class="navA" href="${PATH }/admin/index.html?pageNum=${index}">${index }</a></li>
										</c:otherwise>
									</c:choose>
								</c:forEach>
								<!-- 下一页 -->
								<c:choose>
									<c:when test="${pageInfo.isLastPage }">
										<li class="disabled"><a onclick="return false;" href="#">下一页</a></li>
									</c:when>
									<c:otherwise>
										<!-- 有下一页 -->
										<li><a class="navA" href="${PATH }/admin/index.html?pageNum=${pageInfo.pageNum+1}">下一页</a></li>
									</c:otherwise>
								</c:choose>
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
	<%
		request.setAttribute("menuname", "用户维护");
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
            $("tbody .btn-success").click(function(){
            	//查询点击当前按钮所在行的 管理员的角色列表
            	var adminId = $(this).attr("adminId");
                window.location.href = "${PATH}/admin/assignRole.html?adminId="+adminId;
            });
            $("tbody .btn-primary").click(function(){
                window.location.href = "edit.html";
            });
            
       		//分页导航栏  的点击跳转的单击事件
       		$(".navA").click(function(){
       			//获取  查询条件
       			var condition = $("input[name='condition']").val();
       			var url;
       			if(condition.length>0 ){
       				url = $(this).prop("href")+"&condition="+condition;
       			}else{
       				url = $(this).prop("href");
       			}
       			//跳转到url
       			window.location = url;
       			return false;
       		});
       		
       		//给删除单个管理员的按钮绑定单击事件
       		$(".deleteBtn").click(function(){
       			var username = $(this).parents("tr").children("td:eq(3)").text();
   				//获取要 删除的管理员id
   				var adminId = $(this).attr("adminId");
       			layer.confirm("你确定删除 "+username+" 吗?" , {icon:1 , title:"删除提示:"} ,function(index){
       				layer.close(index);//关闭确认窗口
       				window.location = "${PATH}/admin/deleteAdmin?id="+adminId;
       			});
       			
       		});
       		
       		//给全选复选框绑定单击事件 ，点击后让所有的子复选框状态和 全选框一致
       		$(".table thead :checkbox").click(function(){
       			$(".table tbody :checkbox").prop("checked" , this.checked);
       		});
       		//给 所有的子复选框绑定单击事件，  每个子复选框被点击时 都判断所有子复选框是否都被选中，如果都选中设置全选框选中，如果不是全部选中设置全选框未选中
       		$(".table tbody :checkbox").click(function(){
       			$(".table thead :checkbox").prop("checked" , $(".table tbody :checkbox").length == $(".table tbody :checkbox:checked").length);//判断所有子复选框和  所有选中的子复选框的数量是否一样
       		});
       		//给批量删除的按钮绑定单击事件
       		$(".deleteAdminsBtn").click(function(){
       			//获取页面中选中的多个要删除的管理员的id
       			var $checkbox = $(".table tbody :checkbox:checked");
       			if($checkbox.length>0){
       				/* var queryStr = "";
           			//根据每个复选框获取所在行的管理员的id    10  12 13     ids=10&ids=12&ids=13&
           			$.each($checkbox , function(){
           				//this代表正在遍历的复选框
           				var adminId = $(this).attr("adminId");
           				queryStr+="ids="+adminId+"&";//字符串拼接 累加得到参数字符串
           			});
           			//截取最后多余的&
           			queryStr = queryStr.substring(0 , queryStr.length-1); */
           			//alert(queryStr);
           			//创建存储 多个id的js数组
           			var arr = new Array();
           			$.each($checkbox , function(){
           				var adminId = $(this).attr("adminId");
           				arr.push(adminId);//向数组中添加一个元素
           			});
           			
           			queryStr = "ids="+arr.join(",");//将数组的元素每两个之间使用,隔开 拼接成一个字符串
           			
           			window.location = "${PATH}/admin/deleteAdmins?"+queryStr;
           			
       			}else{
       				layer.msg("请勾选需要删除的管理员后再进行删除操作！！！");
       			}
       			
       		});
       		
       		//发送跳转到编辑页面的请求
       		$(".editAdminBtn").click(function(){
       			
       			//获取要修改的管理员的id
       			var id = $(this).attr("adminId");
       			
       			window.location = "${PATH}/admin/edit.html/"+id;
       		});
       		
       		
         </script>
  </body>
</html>
