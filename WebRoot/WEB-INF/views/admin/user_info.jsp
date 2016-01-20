<%@ page language="java" 
	import="java.util.*,java.text.SimpleDateFormat,com.bean.hibernate.User" 
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="path" value="<%=request.getContextPath() %>" scope="page"/>
<c:set var="basePath" scope="page">
	<%=request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+request.getContextPath()+"/" %>
</c:set>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head></head>
<body>
	<table class="result-tab" >
		<tr>
			<th style="width:30%;">用户姓名</th>
			<th style="width:30%;">创建时间</th>
			<th style="width:10%;">状态</th>
			<th style="width:30%;">操作</th>
		</tr>
		<c:forEach var="user" items="${users}" >
		<tr class="${user.id}" >
			<td>${user.username}</td>
			<td><fmt:formatDate value="${user.createTime}" type="date" pattern="yyyy年MM月dd日 HH:mm"/></td>
			<td id="status_${user.id}">
				<c:choose >
					<c:when test="${user.status}">可用</c:when>
					<c:otherwise>禁用</c:otherwise>
				</c:choose>
			</td>
			<td style="text-align:center;">
				<a href="javascript:void(0);" id="${user.id}" onclick="delUser(this)" >
					<img src="${basePath}images/admin/delete.png" width="20px" height="20px" title="删除"/>
				</a>
				<a href="javascript:void(0);" id="${user.id}" onclick="changeUserStatus(this)">
					<c:choose>
					<c:when test="${user.status}">
						<img src="${basePath}images/admin/unlock.png" width='20px' height='20px' title='禁用'/>
					</c:when>
					<c:otherwise>
						<img src="${basePath}images/admin/lock.png" width='20px' height='20px' title='启用'/>
					</c:otherwise>
					</c:choose>
				</a>
			</td>
		</tr>
		</c:forEach>
	</table>
	<div id="paging_div" >
		<span class="pageNum" <c:if test="${pageNow!=1}">onClick="getInfoPage('UserInfo',1)"</c:if> >首页</span>
		<span class="pageNum" <c:if test="${pageNow!=1}">onClick="getInfoPage('UserInfo',${pageNow-1})"</c:if> >上一页</span>
		<c:forEach begin="1" end="${pageCount}" var="pageNum" >
			<span class="pageNum"
				<c:if test="${pageNow!=pageNum}">onClick="getInfoPage('UserInfo',${pageNum})"</c:if>
				<c:if test="${pageNow==pageNum}">id="pageNow"</c:if>
			>${pageNum}</span>
		</c:forEach>
		<span class="pageNum" <c:if test="${pageNow!=pageCount}">onClick="getInfoPage('UserInfo',${pageNow+1})"</c:if> >下一页</span>
		<span class="pageNum" <c:if test="${pageNow!=pageCount}">onClick="getInfoPage('UserInfo',${pageCount})"</c:if> >尾页</span>
		每页显示数量
		<select name="pageSize" onchange="getInfoPage('UserInfo',1)">
			<c:forEach begin="10" end="50" step="10" var="size">
			<option value="${size}" <c:if test="${size==pageSize}">selected="selected"</c:if> >${size}</option>
			</c:forEach>
		</select>
	</div>
</body>
</html>