/**
 * Created by Administrator on 2019/5/22.
 */
function login() {
    var data = {};
    var t = $('#login_form').serializeArray();
    $.each(t, function () {
        data[this.name] = this.value;
    });
    if (isNullOrEmpty(data.userName)) {
        lyAlertFal("请输入用户名！");
        return;
    }
    if (isNullOrEmpty(data.password)) {
        lyAlertFal("请输入密码！");
        return;
    }
    if (isNullOrEmpty(data.verifyCode)) {
        lyAlertFal("请输入验证码！");
        return;
    }
    data.password=md5(encodeURIComponent(data.password));
    post('doLogin', data, function (res) {
        setFatherPage(-1);
        setCheckedPage(0);
        window.location.href = '/index'
    }, function () {
        //刷新验证码
        $("#code_img").click();
        //清除密码和验证码
        $("#password").val("");
        $("#verifyCode").val("");
    })
}

//获取验证码
function getVerify(obj) {
    obj.src = "getverify?" + Math.random();
}

//设置选中页面,存到cookie中
function setCheckedPage(id) {
    $.cookie('LocalPage', id, {path: '/'});
}

//展示选中的页面
$(function () {
    var id = $.cookie('LocalPage');
    $(".box").css('background-color', '');
    $('#' + id).css('background-color', 'rgba(110, 110, 110, 0.09)');
})

//设置选中页面,存到cookie中
function setFatherPage(id) {
    $.cookie('FatherPage', id, {path: '/'});
}


$(function () {
    var id = $.cookie('FatherPage');
    $(".layui-nav-item").attr('class', 'layui-nav-item');
    $('#' + id).attr('class', 'layui-nav-item layui-this');
})


/**
 *LCalendar时间控件初始化封装 BY hzl 2019-6-21 16:50:35
 * @param objId 为点击初始化的dom Id值
 * @param type 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
 * @param fun 确认按钮回调函数
 */
function initLCalendar(objId, type, fun) {
    var calendar = new LCalendar();
    calendar.init({
        'trigger': '#' + objId, //标签id
        'type': type, //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
        'minDate': (new Date().getFullYear() - 3) + '-' + 1 + '-' + 1, //最小日期
        'maxDate': (new Date().getFullYear()) + '-' + 12 + '-' + 31,//最大日期
        'submitCallBack': fun //确认按钮回调函数
    });
}


//修改密码提交方法
function updatePwdSubmit() {
    var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/;
    var oldPwd = $('#oldPassword').val();
    var newPwd = $('#newPassword').val();
    var ogainPwd = $('#againPassword').val();
    if (isNullOrEmpty(oldPwd) || isNullOrEmpty(newPwd) || isNullOrEmpty(ogainPwd)) {
        lyAlertFal("密码不能为空");
        return false;
    }
    if (!reg.test(newPwd)) {
        lyAlertFal("密码格式不正确");
        return false;
    }
    if (!new RegExp(newPwd).test(ogainPwd)) {
        lyAlertFal("两次输入的密码不一致");
        return false;
    }
    var data = {};
    var t = $('#pwd_form').serializeArray();
    $.each(t, function () {
        data[this.name] = this.value;
    });
    post('student/doupdatepwd', data, function (res) {
        $('#pwd_form')[0].reset();
        lyAlertSucRedirect(res, "/index");
    }, function (res) {
        $('#pwd_form')[0].reset();
    })
}

// 密码找回方法：
//获取forget_form表单相关提交数据
function getForgetFormData() {
    var data = {};
    var t = $('#forget_form').serializeArray();
    $.each(t, function () {
        data[this.name] = this.value;
    });
    return data;
}

//用户认证提交方法
function userAuthSubmit() {
    var data = getForgetFormData();
    post('student/uservalid', data, function (res) {
        window.location.href = '/student/forget';
    }, function (res) {
        //刷新验证码
        $("#code_img").click();
        $("#verifyCode").val("");
    })
}

//邮箱认证提交方法
function emailAuthSubmit() {
    var emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
    var email = $('#email').val();
    if (!emailReg.test(email)) {
        lyAlertFal("邮箱格式不正确");
        return false;
    }
    var data = getForgetFormData();
    post('student/emailvalid', data, function (res) {
        //提交按钮隐藏，改成文字显示
        $('#submit_item').hide();
        $('#submit_msg').html(res);
        $('#submit_msg').show();
    }, null)
}

//设置新密码方法
function setNewPwd() {
    var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/;
    var newPwd = $('#password').val();
    var ogainPwd = $('#againPassword').val();

    if (isNullOrEmpty(newPwd) || isNullOrEmpty(ogainPwd)) {
        lyAlertFal("密码不能为空");
        return false;
    }
    if (!reg.test(newPwd)) {
        lyAlertFal("密码格式不正确");
        return false;
    }
    if (!new RegExp(newPwd).test(ogainPwd)) {
        lyAlertFal("两次输入的密码不一致");
        return false;
    }

    var data = getForgetFormData();
    post('student/resetpassword', data, function (res) {
        window.location.href = '/student/forget';
    }, null)
}

//邮箱绑定发送邮件方法
function emailBandSend() {
    var emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
    var email = $('#email').val();
    if (!emailReg.test(email)) {
        lyAlertFal("邮箱格式不正确");
        return false;
    }

    var data = {};
    var t = $('#emailband_form').serializeArray();
    $.each(t, function () {
        data[this.name] = this.value;
    });
    post('student/sendbandemail', data, function (res) {
        //隐藏发送按钮
        $('#send_div').hide();
        //显示消息
        $('#submit_msg').html(res);
        $('#submit_msg').show();
        lyAlertSuc(res);
    }, null)

}
