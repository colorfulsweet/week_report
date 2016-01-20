<%@ page language="java" import="java.util.*,com.bean.hibernate.SmtpHost" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
			<th style="width:30%;">名称</th>
			<th style="width:35%;">邮箱域名</th>
			<th style="width:35%;">SMTP服务器地址</th>
		</tr>
		<c:forEach var="host" items="${smtpHosts}" >
		<tr class="${host.id}" >
			<td>${host.serverName}</td>
			<td>${host.host}</td>
			<td>${host.smtpHost}</td>
		</tr>
		</c:forEach>
		<tr class="input">
			<td colspan="3">
				<form action="mail/saveHost.do" method="post" onsubmit="return saveHost()">
				服务商名称<input type="text" name="serverName" class="admin_input" />
				邮箱域名<input type="text" name="host" class="admin_input"/><br/>
				SMTP服务器地址<input type="text" name="smtpHost" class="admin_input"/>
				<input type="submit" value="保存" />
				</form>
			</td>
		</tr>
	</table>
	<div id="paging_div" >
		<span class="pageNum" <c:if test="${pageNow!=1}">onClick="getInfoPage('SmtpInfo',1)"</c:if> >首页</span>
		<span class="pageNum" <c:if test="${pageNow!=1}">onClick="getInfoPage('SmtpInfo',${pageNow-1})"</c:if> >上一页</span>
		<c:forEach begin="1" end="${pageCount}" var="pageNum" >
			<span class="pageNum"
				<c:if test="${pageNow!=pageNum}">onClick="getInfoPage('SmtpInfo',${pageNum})"</c:if>
				<c:if test="${pageNow==pageNum}">id="pageNow"</c:if>
			>${pageNum}</span>
		</c:forEach>
		<span class="pageNum" <c:if test="${pageNow!=pageCount}">onClick="getInfoPage('SmtpInfo',${pageNow+1})"</c:if> >下一页</span>
		<span class="pageNum" <c:if test="${pageNow!=pageCount}">onClick="getInfoPage('SmtpInfo',${pageCount})"</c:if> >尾页</span>
		每页显示数量
		<select name="pageSize" onchange="getInfoPage('SmtpInfo',1)">
			<c:forEach begin="10" end="50" step="10" var="size">
			<option value="${size}" <c:if test="${size==pageSize}">selected="selected"</c:if> >${size}</option>
			</c:forEach>
		</select>
	</div>
	<script type="text/javascript">
	function saveHost(){
		var params = {};
		var inputs = $("form>input[type=text]");
		for(var i=0 ; i<inputs.length ; i++){
			params[inputs[i].name] = inputs[i].value;
		}
		var url = "${basePath}/mail/saveHost.do";
		$.post(url,params,function(response){
			if(response == "success"){
				var input_line = $(".result-tab .input");
				var trs = $(".result-tab tr");
				var pageSize = parseInt($("select[name=pageSize]")[0].value);
				if(trs.length-2 < pageSize){
					var html = "<tr><td>"+params["serverName"]
							+"</td><td>"+params["host"]
							+"</td><td>"+params["smtpHost"]
							+"</td></tr>";
					input_line.before(html);
				}
				jAlert("保存成功");
			} else {
				jAlert("保存失败");
			}
		});
		$("form")[0].reset();
		return false;
	}
	</script>
</body>