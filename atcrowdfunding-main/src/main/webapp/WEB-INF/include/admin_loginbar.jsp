<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- 引入springsecurity的动作标签库 -->
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
  <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li style="padding-top:8px;">
				<div class="btn-group">
				  <button type="button" class="btn btn-default btn-success dropdown-toggle" data-toggle="dropdown">
					<i class="glyphicon glyphicon-user"></i> <%-- 获取springsecurity登录成功保存的用户信息 --%><security:authentication property="name"/>  <%-- ${admin.username } --%> <span class="caret"></span>
				  </button>
					  <ul class="dropdown-menu" role="menu">
						<li><a href="#"><i class="glyphicon glyphicon-cog"></i> 个人设置</a></li>
						<li><a href="#"><i class="glyphicon glyphicon-comment"></i> 消息</a></li>
						<li class="divider"></li>
						<li><a id="logoutAxxxxx" href="${PATH }/logout"><i class="glyphicon glyphicon-off"></i> 退出系统</a></li>
					  </ul>
			    </div>
			</li>
			<security:authorize access="hasRole('SA - 软件架构师')">
            <li style="margin-left:10px;padding-top:8px;">
				<button type="button" class="btn btn-default btn-danger">
				  <span class="glyphicon glyphicon-question-sign"></span> 帮助
				</button>
			</li>
			</security:authorize>
			<security:authorize access="hasRole('SA - 软件架构师') OR hasRole('PM - 项目经理')">
            <li style="margin-left:10px;padding-top:8px;">
				<button type="button" class="btn btn-default btn-danger">
				  <span class="glyphicon glyphicon-question-sign"></span> 呵呵1
				</button>
			</li>
			</security:authorize>
			<security:authorize access="hasAuthority('user:get')">
            <li style="margin-left:10px;padding-top:8px;">
				<button type="button" class="btn btn-default btn-danger">
				  <span class="glyphicon glyphicon-question-sign"></span> 呵呵2
				</button>
			</li>
			</security:authorize>
          </ul>
          <form class="navbar-form navbar-right">
            <input type="text" class="form-control" placeholder="查询">
          </form>
        </div>
     
