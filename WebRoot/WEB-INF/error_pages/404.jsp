<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" scope="page">
	<%=request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+request.getContextPath()+"/" %>
</c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="${basePath}">
	<title>Error 404</title>
	<link rel="stylesheet" type="text/css" href="${basePath}css/error.css">
</head>
  
<body>
	<center>
		<img src="${basePath}images/404.png" title="页面未找到"/>
		<br/>
		<a href="${basePath}page/index.do"> 返回首页</a>
		
	<!-- 错误页面文件不到1KB就无法被找到  这些注释只是为了凑字节数-->
	<!-- 错误页面文件不到1KB就无法被找到  这些注释只是为了凑字节数-->
	<!-- 错误页面文件不到1KB就无法被找到  这些注释只是为了凑字节数-->
	<!-- 错误页面文件不到1KB就无法被找到  这些注释只是为了凑字节数-->
	<!-- 错误页面文件不到1KB就无法被找到  这些注释只是为了凑字节数-->
	<!-- 错误页面文件不到1KB就无法被找到  这些注释只是为了凑字节数-->
	<!-- 错误页面文件不到1KB就无法被找到  这些注释只是为了凑字节数-->
	</center>
</body>
</html>
