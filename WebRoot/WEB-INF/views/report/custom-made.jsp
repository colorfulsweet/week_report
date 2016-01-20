<%@ page language="java" import="com.bean.hibernate.*" pageEncoding="UTF-8"%>
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
		.button-purple{width:150px;height:30px;}
		.button-red{width:80px;height:30px;float:right;}
		textarea{width:100%;height:60px;
	</style>
</head>
<body>
	<table id="show" class="result-tab">
		<tr>
			<th>非受迫性优化与心得体会</th>
		</tr>
		<tr>
			<td>
				<textarea name="newText" class="admin_input" id="new"></textarea>
				<input type="button" value="保存" class="button button-purple" onClick="save()"/>
			</td>
		</tr>
		<c:forEach var="exp" items="${ExpList}">
		<tr class="${exp.id}">
			<td>${exp.text}
				<input type="button" value="删除" class="button button-red" 
						id="${exp.id}"	onClick="del(this)"/>
			</td>
		</tr>
		</c:forEach>
	</table>
	<script type="text/javascript" >
	/**
	 * 保存心得体会文本
	 */
	function save() {
		var text = $("#new");
		if (text.val().length == 0) {
			alert("心得体会的内容不能为空");
			return;
		}
		var url = "${basePath}/report/addExp.do";
		$.post(url,{"text":text.val()},function(data){
			//异步请求的回调函数
			var _tr = document.createElement("tr");
			_tr.setAttribute("class",data);
			var _td = document.createElement("td");
			var _button = createDelBtn(data);
			var table = $("#show");
	
			_td.innerHTML = text.val();
			_td.appendChild(_button);
			_tr.appendChild(_td);
			table.append(_tr);
			text.attr("value","");
		});
	}
	/**
	 * 删除心得体会文本
	 * @param target 点击的"删除按钮对象"
	 * @returns
	 */
	var del = function(target) {
		var id = target.id;
		jConfirm("你确定删除该条心得吗?",null,function(result){
			if(result){
				var url = "${basePath}/report/delExp.do";
				$.get(url,{"id":id},function(data){
					//从页面上移除要删除的tr元素
					$("tr").remove("."+id);
				});
			}
		});
	};
	/**
	 * 创建删除按钮
	 * @param id 心得体会ID
	 * @returns
	 */
	function createDelBtn(id) {
		var _button = document.createElement("input");
		_button.setAttribute("type", "button");
		_button.setAttribute("value", "删除");
		_button.setAttribute("class", "button button-red");
		_button.setAttribute("onClick", "del(this)");
		_button.setAttribute("id", id);
	
		return _button;
	}
	</script>	
</body>
</html>