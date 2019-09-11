<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <div class="col-sm-3 col-md-2 sidebar">
			<div class="tree">
				<ul style="padding-left:0px;" class="list-group">
					<!-- 通过jstl+el遍历显示菜单集合 -->
					<c:forEach items="${sessionScope.parentMenus }" var="parentMenu">
						<c:choose>
							<c:when test="${empty parentMenu.children }">
								<!-- 判断父菜单的子菜单集合长度：如果长度为0代表 没有子菜单 -->
								<li class="list-group-item tree-closed" >
									<a href="${PATH }/${parentMenu.url }"><i class="${parentMenu.icon }"></i> ${parentMenu.name }</a> 
								</li>
							</c:when>
							<c:otherwise>
								<li class="list-group-item tree-closed">
									<span><i class="${parentMenu.icon }"></i> ${parentMenu.name } <span class="badge" style="float:right">${parentMenu.children.size() }</span></span> 
									<ul style="margin-top:10px;display:none;">
										<c:forEach items="${parentMenu.children }" var="childMenu">
											<li style="height:30px;">
												<a href="${PATH }/${childMenu.url }"><i class="${childMenu.icon }"></i> ${childMenu.name }</a> 
											</li>
										</c:forEach>
									</ul>
								</li>
							</c:otherwise>
						</c:choose>
					
					</c:forEach>
					
				</ul>
			</div>
        </div>