var bh = document.body.scrollHeight;//网页正文全文高
var bw = document.body.scrollWidth;//网页正文全文宽
var cnt = 0;
var now = null;
/**
 * 用户自己填写心得文本的单选按钮选中时调用函数
 */
function radio_custom(){
	var _custom = document.getElementById("custom");
	_custom.disabled = false;
}
/**
 * 预设的心得体会文本的单选按钮选中时调用函数
 */
function radio_change(){
	var _custom = document.getElementById("custom");
	_custom.disabled = true;
}
/**
 * 显示遮罩层
 */
function showBg(){
	$("#fullbg").css({ 
		height:bh, 
		width:bw, 
		display:"block" 
	}); 
}
/**
 * 隐藏遮罩层
 */
function closeBg() { 
	$("#fullbg,#selectPartner,#nextWeekTask,#sendMail").hide(); 
} 
/**
 * 创建下周主要事项
 * (显示遮罩层)
 */
function createTask(){
	if(cnt >= 5) {
		jAlert("最多只能创建5条任务!");
		return;
	}
	showBg();
	var nextWeekTask =  $("#nextWeekTask");
	var wh = (document.body.clientWidth-400)/2;
	nextWeekTask.css({"left":wh+"px"});
	nextWeekTask.show();
}
/**
 * 保存下周事项(只存储在前端)
 */
function saveTask(){
	cnt++;
	var _input = [$("#taskName"),$("#taskDate"),$("#chargePerson"),$("#partner")];
	var info_text = ["请填写<b>任务名称</b>","请选择<b>完成时间</b>","请填写<b>负责人</b>"];
	for(var i=0 ; i<3 ; i++){
		if(_input[i].val().length == 0){
			jAlert(info_text[i],"提示",function(){
				if(i == 1){
					_input[i][0].click();
				} else {
					_input[i][0].focus();
				}
			});
			return;
		}
	}
	var now = new Date();
	var year_now = now.getFullYear();
	var month_now = now.getMonth()+1;
	var day_now = now.getDate();
	var _date = _input[1].val();
	var year = parseInt(_date.substring(0,_date.indexOf("-")));
	var month = parseInt(_date.substring(_date.indexOf("-")+1,_date.lastIndexOf("-")));
	var day = parseInt(_date.substring(_date.lastIndexOf("-")+1,_date.length));
	if(year>=year_now && month>=month_now & day>=day_now){
		closeBg();
		var html_text = "<tr style='color:blue'><td colspan='6'>\n" +
				"任务名称<input value='"+ _input[0].val()+"' readonly='readonly' style='width:300px' name='taskName"+cnt+"'/><br/>"+
				"计划完成时间<input value='"+ _input[1].val()+"' readonly='readonly' name='taskDate"+cnt+"'/>"+
				"负责人<input value='"+ _input[2].val()+"' readonly='readonly' name='chargePerson"+cnt+"'/>"+
				"其他参与人<input value='"+ _input[3].val()+"' readonly='readonly' name='otherPartner"+cnt+"'/>"+
				"</td></tr>";
		$(".task").after(html_text);
		for(var i=0 ; i<_input.length ; i++){
			_input[i].attr("value","");
		}
	} else {
		jAlert("任务完成日期不能早于当前日期（<font color='blue'>"+year_now+"年"+month_now+"月"+day_now+"日</font>）","提示",function(){
			_input[1][0].click();
		});
	}
}
/**
 * 统计当前总共添加了多少条
 * 并且作为一个隐藏元素跟随表单提交
 */
function countTask(){
	var hidden = document.createElement("input");
	hidden.setAttribute("type","hidden");
	hidden.setAttribute("value",cnt.toString());
	hidden.setAttribute("name","countTask");
	$("form").append(hidden);
	return true;
}
/**
 * 显示"选择参与人"面板
 * @param target
 */
function showPartner(target){
	now = target.name;
	showBg();
	var wh = (document.body.clientWidth-200)/2;
	var selectPartner = $("#selectPartner");
	selectPartner.css({"left":wh+"px"});
	selectPartner.show();
}
/**
 * 保存参与人姓名
 */
function savePartner(){
	var name = $("#newPartnerName");
	if(name.val().length == 0){
		jAlert("请输入参与人姓名");
		return;
	}
	$.post("./report/addPartner.do",{"partnerName":name.val()},function(data){
		var html_text = "<li >"+
			"<input type=\"checkbox\" class=\"partners\" value=\""+name.val()+"\"/>"+
			" <img src=\"./images/person.png\" width=\"18px\" height=\"18px\"/> "+name.val()+
			"<a href=\"javascript:void(0);\" class=\"delPartner\">删除</a>"+
			"</li>";
		$("#partner_list").append(html_text);
		name.attr("value","");
	});
}
/**
 * 确定选择参与人
 */
function confirmPartner(){
	var checked = $("input:checkbox:checked");
	if(checked.length == 0){
		jAlert("请选择参与人");
		return;
	}
	closeBg();
	var partners = "";
	for(var i=0 ; i<checked.length ; i++){
		partners += checked[i].value;
		if(i != checked.length-1){
			partners += "，";
		}
	}
	$("textarea[name="+now+"]").attr("value",partners);
}
/**
 * 显示"发送邮件"浮动窗口
 */
function openSendMail(){
	var _iframe = $("iframe[name=sendMail]")[0];
	_iframe.setAttribute("src","./page/sendEmail.do");
	showBg();
	var sendMail = $("#sendMail");
	var wh = (document.body.clientWidth-600)/2;
	sendMail.css({"left":wh+"px"});
	sendMail.show();
	
	var param = {};
	countTask();
	var _form = $("form")[0];
	for(var i=0 ; true ; i++){
		if(_form[i] != null && _form[i] != undefined){
			if((_form[i].type=="radio" && _form[i].checked)||_form[i].type != "radio"){
				param[_form[i].name] = _form[i].value;
			} 
			continue;
		} else {
			break;
		}
	}
	$.post("./report/saveExcel.do",param,function(data){
		if(data == "failed"){
			alert("文件创建失败");
		}
	});
}