<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="path" value="<%=request.getContextPath() %>" scope="page"/>
<c:set var="basePath" scope="page">
	<%=request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+request.getContextPath()+"/" %>
</c:set>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<style type="text/css">
		#main{background-color:white;width:300px;margin:10px auto;padding:5px;font-size:18px;}
		input[type=submit]{font-size:16px;font-family:"微软雅黑";width:100px;height:30px;}
	</style>
</head>
<body>
	<div id="main">
		<form action="${basePath}user/changePwd.do" method="post" onSubmit="return check();">
		修改用户 <font color="blue"> ${user.username} </font>的密码
			<input type="hidden" name="username" value="${user.username}"/>
		<br/>
		新密码：
			<input type="password" name="newPwd" id="pwd" size="20" />
			<br/>
			<span><font color="red" size="3" id="pwdErr"></font></span>
			<hr/>
			<input type="submit" tabindex="3" class="button button-purple" value="确定" />
		</form>
	</div>
	<script type="text/javascript">
	function check(){
		var newPwd = document.getElementById("pwd").value;
		var pwdErr = document.getElementById("pwdErr");
		if(newPwd.length == 0){
			pwdErr.innerHTML = "密码不能为空";
			return false;
		} 
		pwdErr.innerHTML = "";
		return true;
	}
	</script>
</body>
</html>