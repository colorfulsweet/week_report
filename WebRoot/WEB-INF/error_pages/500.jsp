<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" scope="page">
	<%=request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+request.getContextPath()+"/" %>
</c:set>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="${basePath}">
	<title>Error 500</title>
	<link rel="stylesheet" type="text/css" href="${basePath}css/error.css">
</head>
<body>
	<center>
		<img src="${basePath}images/500.png" title="服务器内部错误"/>
		<br/>
		<a href="${basePath}page/index.do"> 返回首页</a>
	 	
		<div class="StackTrace">
	<%
		out.println(exception.getClass());
		String whole_msg = exception.getMessage();
		if(whole_msg != null){
			String[] msgs = whole_msg.split("\n");
		 	for(String msg : msgs){
		 		out.println(msg+"<br/>");
		 	}
	 	}
	 	StackTraceElement[] stacks = exception.getStackTrace();
	 	for(StackTraceElement stack : stacks){
	 		out.println("&nbsp;&nbsp;&nbsp;&nbsp;"+stack+"<br/>");
		}
 	%>
		</div>
	</center>
</body>
</html>
