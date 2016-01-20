/**
 * 初始化数据库
 */
function initDatabase(){
	jConfirm("确定要初始化数据库吗?",null,function(result){
		if(result){
			var url = "../admin/initDatabase.do";
			$.get(url,null,function(data){
				if(data == "success"){
					jAlert("数据库初始化完毕!");
				} else {
					jAlert("操作失败");
				}
			});
		}
	});
}
/**
 * 重置服务器内存中的日期序列刷新间隔
 */
function changeRefreshTime(){
	var pattern = /^[0-9]*$/;
	jPrompt("设定日期序列刷新时间间隔(范围1~1000分钟)",null,"重置",function(input){
		if(input == null){
			return;
		}
		if(!pattern.test(input)){
			jAlert("请输入正确的数字",null,function(){
				changeRefreshTime();
			});
			return;
		}
		var num = parseInt(input);
		if(num > 1000 || num < 1){
			jAlert("请输入1~1000之内的整数",null,function(){
				changeRefreshTime();
			});
			return;
		}
		var url = "../admin/changeRefreshTime.do";
		$.get(url,{"time":input},function(data){
			if(data == "success"){
				jAlert("操作成功，刷新时间设置为"+input+"分钟");
			} else {
				jAlert("操作失败");
			}
		});
	});
}
/**
 * 获取用户信息列表
 * @param name 菜单项名称
 * @param page 页码
 */
function getInfoPage(name,page){
	var _url = "../admin/get"+name+".do";
	var params = {};
	if(page != null && page != undefined){
		params["page"] = page;
	}
	var option_selected = $("select[name=pageSize] option:selected");
	if(option_selected.length != 0){
		params["pageSize"] = option_selected[0].value;
	}
	var callback = function(response){
		$("#content").html(response);
	};
	$.ajax({
		type:"get",
		async:true,//异步请求
		url:_url,
		data:params,
		success:callback,
		error:function(){
			jAlert("获取信息失败!");
		}
	});
}

/**
 * 删除用户
 * @param target
 */
function delUser(target){
	var id = target.id;
	var url = "../admin/delUser.do";
	jConfirm("确定删除该用户吗?","操作确认",function(result){
		if(result){
			$.post(url,{"id":id},function(data){
				if(data == "success"){
					$("tr").remove("."+id);
				} else {
					alert("操作失败!");
				}
			});
		}
	});
}
/**
 * 改变用户的账户状态(可用变为禁用,禁用变为可用)
 * @param target
 */
function changeUserStatus(target){
	var id = target.id;
	var url = "../admin/lockUser.do";
	$.post(url,{"id":id},function(data){
		if(data == "failed"){
			jAlert("操作失败");
			return;
		}
		var _target = $(target).find("img");
		if(data == "user_lock"){
			_target.attr("src","../images/admin/lock.png");
			_target.attr("title","启用");
			$("#status_"+id).html("禁用");
			jAlert("操作成功,用户已禁用");
		} else {
			_target.attr("src","../images/admin/unlock.png");
			_target.attr("title","禁用");
			$("#status_"+id).html("可用");
			jAlert("操作成功,用户已启用");
		}
	});
}
/**
 * 导航菜单交互功能实现函数
 * @param obj
 */
function dropMenu(obj){
	$(obj).each(function(){
		var theSpan = $(this);
		var theMenu = theSpan.find(".submenu");
		var tarHeight = theMenu.height();
		theMenu.css({height:0,opacity:0});
		
		function expand() {
			clearTimeout(null);
			theSpan.find('a').addClass("selected");
			theMenu.stop().show().animate({height:tarHeight,opacity:1},200);
		}
		
		function collapse() {
			clearTimeout(null);
			t1 = setTimeout(function(){
				theSpan.find('a').removeClass("selected");
				theMenu.stop().animate({height:0,opacity:0},200,function(){
					$(this).css({display:"none"});
				});
			}, 250);
		}
		
		theSpan.hover(expand, collapse);
		theMenu.hover(expand, collapse);
	});
}

$(document).ready(function(){
	dropMenu(".drop-menu-effect");
});