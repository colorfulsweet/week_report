var flag1,flag2,flag3,flag4;
flag1 = flag2 = flag3 = flag4 = false;
var username = null;
var pwd1 = null;
var pwd2 = null;
var checkcode = null;
/**
 * 初始化函数
 */
$(function() {
	username = $("#username");
	pwd1 = $("#password");
	pwd2 = $("#password2");
	checkcode = $("#checkcode");
})
/**
 * 验证用户名是否合法
 * 1.不能为空 2.长度不超过16位 3.在数据库中不存在
 */
function checkName(){
	var nameErr = $("#nameErr");
	if(username.val().length == 0){
		nameErr.html("<font color='red'>用户名不能为空<font>");
		flag1 = false;
		return;
	}
	if(username.val().length > 16){
		nameErr.html("<font color='red'>用户名长度不得超过16位<font>");
		flag1 = false;
		return;
	}
	var url = "user/checkUserExist.do";
	$.post(url,{"username":username.val()},function(data){
		if(data == "user_exist"){
			nameErr.html("<font color='red'>该用户已存在<font>");
			username.css({"background":"pink"});
			flag1 = false;
		}else{
			nameErr.html("");
			username.css({"background":"white"});
			flag1 = true;
		}
	});
}
/**
 * 验证密码是否合法
 * 验证条件:不能为空 
 */
function checkPwd1(){
	var pwd1Err = $("#pwd1Err");
	if(pwd1.val() == ""){
		pwd1Err.html("<font color='red'>密码不能为空<font>");
		flag2 = false;
	} else {
		pwd1Err.html("");
		flag2 = true;
	}
}
/**
 * 验证密码确认是否合法
 * 验证条件:与第一次输入的密码一致
 */
function checkPwd2(){
	var pwd2Err = $("#pwd2Err");
	if(pwd1.val() != pwd2.val()){
		pwd2Err.html("<font color='red'>两次输入的密码不一致<font>");
		flag3 = false;
	} else {
		pwd2Err.html("");
		flag3 = true;
	}
}
/**
 * 设置图片的src,获取验证码
 */
function showCode(){
	$("img").show();
}

/**
 * 检验验证码是否正确
 */
function checkCode(){
	var checkCode_text = $("#checkcode").val();
	switch(checkCode_text.length){
		case 1:
		case 2:
		case 3:return;
		case 4:
			//当输入的字符串长度达到4的时候自动执行验证
			code(checkCode_text);
			break;
		default:flag4 = false; return;
	}
}
function code(checkCode_text){
	//请求后台验证输入的验证码是否正确
	var callback = function(result,status){
		var checkCodeInfo = $("#checkCodeInfo");
		if(result == "checkcode_true"){
			checkCodeInfo.html("<img src='images/register/true.png' width='20' height='20'/><font color='green'>验证码正确</font>");
			flag4 = true;
		} else {
			checkCodeInfo.html("<img src='images/register/false.png' width='20' height='20'/><font color='red'>验证码错误</font>");
			flag4 = false;
		}
	};
	$.ajax({
		type:"get",
		url:"user/checkCode.do",
		data:{"code":checkCode_text},
		dataType: "text",
		async:false, //此处必须使用同步请求
		success:callback,
		error:function(){
			alert("请求超时!");
		}
	});
}
/**
 * 表单提交事件调用函数
 * @returns {Boolean}
 */
function checkSubmit(){
	if(flag1 && flag2 && flag3 && flag4){
		return true;
	} else {
		checkName();
		checkPwd1();
		checkPwd2();
		if(!flag4){
			var checkCode_text = $("#checkcode").val();
			code(checkCode_text);
		}
		if(flag1 && flag2 && flag3 && flag4){
			return true;
		} else {
			return false;
		}
	}
}
/**
 * 刷新验证码图片
 * @param target 图片元素对象
 */
function refreshImg(target){
	target.src="user/createCheckCode.do?"+Math.random();
}