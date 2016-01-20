<%@ page language="java" import="java.util.*,com.bean.hibernate.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="path" value="<%=request.getContextPath() %>" scope="page"/>
<c:set var="basePath" scope="page">
	<%=request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+request.getContextPath()+"/" %>
</c:set>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="${basePath}">
	<title>发送邮件</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	
	<link rel="stylesheet" type="text/css" href="${basePath}css/send_email.css">
	<link rel="stylesheet" type="text/css" href="${basePath}jquery/jqueryAlert/jquery.alerts.css">
</head>
<body>
	<c:forEach var="email" items="${EmailList}" >
	<div class="EmailInfo" id="${email.id}" onclick="send(this)">
		STMP服务器地址:<span class="info_text">${email.smtpHost}</span>
		登录名:<span class="info_text">${email.username}</span><br/>
		发送人:<span class="info_text">${email.sendFrom}</span>
		接收人:<span class="info_text">${email.sendTo}</span>
		抄送:<span class="info_text">${email.copyTo}</span>
	</div>
	</c:forEach>
	<script type="text/javascript" src="${basePath}jquery/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="${basePath}jquery/jqueryAlert/jquery.alerts.js"></script>
	<script type="text/javascript" >
	function send(target){
		var callback = function(data){
			if(data == "success"){
				jAlert("邮件发送成功");
			} else {
				jAlert("邮件发送失败");
			}
		};
		jConfirm("确定发送邮件吗?","操作确认",function(result){
			if(result){
				$.post("./mail/send.do",{"id":target.id},callback);
			}
		});
	}
	</script>
</body>
</html>
