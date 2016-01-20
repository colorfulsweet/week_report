<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="<%=request.getContextPath() %>" scope="page"/>
<c:set var="basePath" scope="page">
	<%=request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+request.getContextPath()+"/" %>
</c:set>
<!DOCTYPE >
<html>
<head>
	<title>欢迎登陆</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	 
	<link rel="stylesheet" type="text/css" href="${basePath}css/login.css" />
</head>
<body>
<div class="container">
	<section id="content">
		<form action="${basePath}page/login.do" method="post" >
			<h1>周报生成系统</h1>
			<div>
				<input type="text" placeholder="用户名" required="required" id="username" name="username" />
			</div>
			<div>
				<input type="password" placeholder="密码" required="required" id="password" name="password" />
			</div>
			<div style="text-align:left;padding-left:25px;">
				<input type="radio" name="role" value="user" checked="checked"/>普通用户
                <input type="radio" name="role" value="admin">管理员
			</div>
			<div id="info" style="text-align:left;padding-left:20px;color:red;">${info}</div>
			<div>
				<input type="submit" value="登陆" />
				<span id="remember"><input type="checkbox" name="remember" />记住我</span>
				<a href="${basePath}register.html" class="showbtn">新用户注册</a>
			</div>
		</form>
	</section>
</div>
<script type="text/javascript" src="${basePath}jquery/jquery-1.8.3.min.js"></script>
</body>
</html>
