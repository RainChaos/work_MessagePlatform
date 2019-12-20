layui.use(['form', 'layer','element'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery,
        element = layui.element;
    layer.ready(function () {
        var captcha = document.getElementById("captcha");
        captcha.src = "/getverify?t=" + new Date().getTime();
        var  uName=window.localStorage.getItem('userName');
        var  userPass=window.localStorage.getItem('userPass');
        if(uName != null){
            //说明之前存储过值，说明用户希望记住用户名
            document.getElementById('username').value=uName;
            document.getElementById('password').value=userPass;
        }
    })

    $('#wxBtn').click(function(){
        var type = $('input:radio:checked').val();
        if (type == 'teacher') {
            var TeOpenid = $('#TeOpenid').val();
                //用户允许事件
                $.ajax({
                    type: "POST",
                    url: "/teacher/getAccountByWxNo?wxNo="+TeOpenid,
                    success: function (result) {
                        if (result.code == 200) {
                            //登录成功
                            parent.location.href = '/teacher/index';
                        } else {
                            layer.msg(result.msg,{time:1500,icon:5});
                        }
                    }
                });

        }else {
            layer.msg("管理员请用账号密码登录！",{icon:7});
        }
    });

    //管理员登录按钮事件
    form.on("submit(login)", function (data) {

        if($('#verifyCode').val()==""||$('#verifyCode').val().trim().length==0){
            layer.msg('验证码不能为空！',{icon:5});
            return false;
        }

        // var scCode = $('#scCode').find("option:selected").val().trim();
        // if(scCode==""||scCode.length==0){
        //     layer.msg('请先选择学校！',{icon:5});
        //     return false;
        // }

        /*点击登录时存储数据*/
        var username=document.getElementById('username').value;
        var password=document.getElementById('password').value;
         var type = $('input:radio:checked').val();
        if (type=="user"){
            loginurl = "/frame/logon";
        }
        if (type=="teacher"){
            loginurl = "/teacher/login";
        }
        $.ajax({
            type: "POST",
            url: loginurl,
            data:$("#form").serialize(),
            success: function (result) {
                if (result.code == 200) {
                    //登录成功
                    /*判断用户是否选择记住用户名*/
                    var isRemenber=document.getElementById('isRemenber');
                    if(isRemenber.checked==true){
                        /*存储数据到localstorage*/
                        window.localStorage.setItem('userName',username);
                        window.localStorage.setItem('userPass',password);
                    } else{
                        window.localStorage.removeItem('userName');
                        window.localStorage.removeItem('userPass');
                    }
                    if (type=="user"){
                        parent.location.href = '/index';
                    }
                    if (type=="teacher"){
                        parent.location.href = '/teacher/index?wxNo='+$('#TeOpenid').val();
                    }
                } else {
                    var json = eval("("+result+")");
                    top.layer.msg(json.msg);

                }
            }
        });
        return false;
    })

    $("#captcha").click(function(){
        form.render();
        var captcha = document.getElementById("captcha");
        captcha.src = "/getverify?t=" + new Date().getTime();
    });
});

