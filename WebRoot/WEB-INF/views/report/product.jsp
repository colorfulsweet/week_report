<%@ page language="java" import="com.bean.hibernate.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%!String[] WEEK_DAYS = {"星期六","星期日","星期一","星期二","星期三","星期四","星期五"};%>

<c:set var="WEEK_DAYS" value="<%=WEEK_DAYS %>" scope="page"/>
<c:set var="path" value="<%=request.getContextPath() %>" scope="page"/>
<c:set var="basePath" scope="page">
	<%=request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+request.getContextPath()+"/" %>
</c:set>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head></head>
<body>
<form action="${basePath}report/download.do" method="post" onSubmit="countTask()">
<!-- 主体表格(填写本周工作任务相关内容) -->
	<table class="result-tab" >
		<tr>
			<td colspan="6">
		姓名 <input type="text" name="name" class="admin_input" value="${user.username}" />
			<button type="button" onclick="openSendMail()" style="float:right;">
				<img src="${basePath}images/send_mail.png" width="25px" height="25px"/>
				<span style="font-size:16px;color:#2FB320;" >发送邮件</span>
			</button>
			</td>
		</tr>
		<tr>
			<th colspan="2" width="20%">具体时间</th>
			<th width="24%">工作内容记录</th>
			<th width="10%">其他参与人</th>
			<th width="23%">存在问题</th>
			<th width="23%">提议解决办法</th>
		</tr>
		<c:forEach begin="1" end="7" step="1" var="index">
		<tr>
			<td >${WEEK_DAYS[index-1]}</td>
			<td >${dates_str[index-1]}</td>
			<td><textarea name="task${index}" class="admin_input"></textarea></td>
			<td><textarea name="partner${index}" class="admin_input" onFocus="showPartner(this)" readonly="readonly" ></textarea></td>
			<td><textarea name="question${index}" class="admin_input"></textarea></td>
			<td><textarea name="answer${index}" class="admin_input"></textarea></td>
		</tr>
		</c:forEach>
		<tr>
			<th colspan="6">
			选择心得体会
			</th>
		</tr>
		<tr>
		<!-- 用户自定义心得体会文本 -->
			<td colspan="6">
				<input type="radio" name="exps" value="custom" checked="checked" onclick="radio_custom()"/>
				<textarea name="exp" class="admin_input" id="custom" style="width:95%"></textarea>
			</td>
		</tr>
		<c:forEach var="exp" items="${ExpList}">
		<tr>
			<td colspan="6">
			<!-- 从当前用户预定义的心得体会文本当中选择 -->
				<input type="radio" name="exps" value="${exp.id}" onclick="radio_change()"/>
				${exp.text}<br/>
			</td>
		</tr>
		</c:forEach>
		<tr class="task">
			<th colspan="6">
			下周主要事项
			<!-- 点击新增出现下周事项添加(遮罩层) -->
				<div style="float:right;" onclick="createTask()">
					<a href="javascript:void(0);">新增</a>
				</div>
			</th>
		</tr>
		<tr>
			<td colspan="6">
				<div style="text-align:center;">
					<input type="submit" class="btn btn-primary" value="下载Excel表格" />
					<input type="reset" class="btn btn-primary" value="清空"/>
		  		</div>
			</td>
		</tr>
  	</table>
</form>
<div id="main">
 		<!-- 灰色遮罩层 -->
	<div id="fullbg"></div> 
	<!-- 添加下周事项 浮动窗口start -->
	<div id="nextWeekTask"> 
		<p class="close">
			<span style="float:left;">添加下周事项</span>
			<a href="javascript:void(0);" onclick="closeBg();">
				<img src="${basePath}images/close.gif"/>
			</a>
		</p> 
		<div>
			<span style="font-size:16px;color:red;" >注：带*的为必填项</span>
			<table class="tab">
				<tr>
					<td align="right"><font color="red">*</font>任务名称</td>
					<td align="left"><textarea id="taskName" class="admin_input"></textarea></td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>计划完成时间</td>
					<td align="left">
					<input class="Wdate" type="text" readonly="readonly" onClick="WdatePicker()" id="taskDate" />
					</td>
				</tr>
				<tr>
					<td align="right"><font color="red">*</font>负责人</td>
					<td align="left"><input type="text" id="chargePerson" class="admin_input" /></td>
				</tr>
				<tr>
					<td align="right">其他参与人</td>
					<td align="left"><input type="text" id="partner" class="admin_input" /></td>
				</tr>
			</table>
			<input type="button" value="确定" onClick="saveTask()" class="btn btn-primary" style="margin-top:20px;"/>
		</div> 
	</div> 
	<!-- 添加下周事项 浮动窗口end -->
	
	<!-- 选择参与人 浮动窗口start-->
	<div id="selectPartner"> 
		<p class="close">
			<span style="float:left;">选择其他参与人</span>
			<a href="javascript:void(0);" onclick="closeBg();">
				<img src="${basePath}images/close.gif"/>
			</a>
		</p> 
		<div>
			<input type="text" class="admin_input" id="newPartnerName" style="width:130px;"/>
			<input type="button" class="button button-purple" value="保存" onClick="savePartner()" style="padding:0px 5px;"/>
			<%-- 保存操作时,访问后台数据库执行保存并在页面中添加该项目 --%>
		</div>
		<div class="partner_list">
			<ul id="partner_list">
			<c:forEach var="partner" items="${PartnerList}">
				<li >
					<input type="checkbox" class="partners" value="${partner.partnerName}"/>
					<img src="${basePath}images/person.png" width="18px" height="18px"/>
					${partner.partnerName}
					<a href="javascript:void(0);" class="delPartner">删除</a>
				</li>
			</c:forEach>
			</ul>
		</div>
		<input type="button" class="btn btn-primary" value="确定" onclick="confirmPartner()"/>
	</div> 
	<!-- 选择参与人 浮动窗口end-->
	
	<!-- 发送邮件选择发送设置浮动窗口start -->
	<div id="sendMail">
		<p class="close">
			<span style="float:left;">请选择发送邮件的配置</span>
			<a href="javascript:void(0);" onclick="closeBg();">
				<img src="${basePath}images/close.gif"/>
			</a>
		</p> 
		<iframe name="sendMail" src="" width="590px" height="350px"></iframe>
	</div>
	<!-- 发送邮件选择发送设置浮动窗口end -->
</div>
<script type="text/javascript" src="${basePath}js/product.js"></script>
</body>
</html>