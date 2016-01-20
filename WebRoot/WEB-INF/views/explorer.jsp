<%@ page language="java" 
		import="java.util.*,com.utils.*,com.bean.hibernate.*" 
		pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="path" value="<%=request.getContextPath() %>" scope="page"/>
<c:set var="basePath" scope="page">
	<%=request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+request.getContextPath()+"/" %>
</c:set>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>周报自动生成系统</title>
	
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	
	<link rel="stylesheet" type="text/css" href="${basePath}css/common.css"/>
	<link rel="stylesheet" type="text/css" href="${basePath}css/accordion.css"/>
	<link rel="stylesheet" type="text/css" href="${basePath}css/cover.css"/>
	<link rel="stylesheet" type="text/css" href="${basePath}css/font-awesome.min.css" />
	<link rel="stylesheet" type="text/css" href="${basePath}jquery/jqueryAlert/jquery.alerts.css" />
</head>
<body>
<div id="top">
	<span id="title">周报自动生成系统</span>
	<a href="${basePath}user/logout.do" id="logout">
		<span>
			<img src="${basePath}images/power.png" width="20px" height="20px"/>注销
		</span>
	</a>
</div>
<ul id="accordion" class="accordion">
	<c:forEach items="${menuList}" var="menu">
	<li>
		<div class="link"><i class="fa ${menu.icon}"></i>${menu.name}<i class="fa fa-chevron-down"></i></div>
		<ul class="submenu">
			<c:forEach items="${menu.submenuList}" var="submenu">
			<li><a href="javascript:void(0);" onClick="openContentPage('${submenu.url}')">${submenu.name}</a></li>
			</c:forEach>
		</ul>
	</li>
	</c:forEach>
</ul>
<div id="content"></div>

<script type="text/javascript" src="${basePath}jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${basePath}jquery/jqueryAlert/jquery.alerts.js"></script>
<script type="text/javascript" src="${basePath}My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" >
$(function() {
	var Accordion = function(el, multiple) {
		this.el = el || {};
		this.multiple = multiple || false;

		// Variables privadas
		var links = this.el.find('.link');
		// Evento
		links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown);
	};

	Accordion.prototype.dropdown = function(e) {
		var $el = e.data.el;
			$this = $(this),
			$next = $this.next();

		$next.slideToggle();
		$this.parent().toggleClass('open');

		if (!e.data.multiple) {
			$el.find('.submenu').not($next).slideUp().parent().removeClass('open');
		};
	};

	new Accordion($('#accordion'), false);
});

function openContentPage(menu_url){
	menu_url = "${basePath}"+menu_url;
	var callback = function(response){
		$("#content").html(response);
	};
	$.ajax({
		type:"get",
		async:true,//异步请求
		url: menu_url,
		success:callback,
		error:function(){
			jAlert("获取页面失败!");
		}
	});
}
</script>
</body>
</html>
