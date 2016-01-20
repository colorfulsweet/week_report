<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="<%=request.getContextPath() %>" scope="page"/>
<c:set var="basePath" scope="page">
	<%=request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+request.getContextPath()+"/" %>
</c:set>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>后台管理</title>
	
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	
	<link rel="stylesheet" type="text/css" href="${basePath}css/common.css" />
	<link rel="stylesheet" type="text/css" href="${basePath}css/admin.css" />
	<link rel="stylesheet" type="text/css" href="${basePath}jquery/jqueryAlert/jquery.alerts.css" />
</head>
<body>
	<div class="nav">
		<ul class="nav_list">
			<li><a href="${basePath}index.jsp" class="home"><span>首页</span></a></li>
			<li class="drop-menu-effect">
				<a href="javascript:void(0);"><span>系统管理</span></a>
				<ul class="submenu">
					<li><a href="javascript:void(0);" onClick="initDatabase()">初始化数据库</a></li>
					<li><a href="javascript:void(0);" onclick="changeRefreshTime()">重置刷新时间</a></li>
					<li><a href="javascript:void(0);">清除临时文件</a></li>
				</ul>
			</li>
			<li class="drop-menu-effect">
				<a href="javascript:void(0);" ><span>数据维护</span></a>
				<ul class="submenu">
					<li><a href="javascript:void(0);" onClick="getInfoPage('UserInfo')">用户管理</a></li>
					<li><a href="javascript:void(0);" onClick="getInfoPage('SmtpInfo')">SMTP服务器</a></li>
				</ul>
			</li>
    	</ul>
	</div>
   	<div id="content"></div>
  	<script type="text/javascript" src="${basePath}jquery/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="${basePath}jquery/jqueryAlert/jquery.alerts.js"></script>
	<script type="text/javascript" src="${basePath}js/admin.js" ></script>
</body>
</html>
