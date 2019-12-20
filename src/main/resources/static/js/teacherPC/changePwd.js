var $;
var $form;
var form;
//var areaData = address;
layui.config({
	base : "js/"
}).use(['form','layer','jquery','laydate'],function(){
	var layer = parent.layer === undefined ? layui.layer : parent.layer,
		laydate = layui.laydate;
		$ = layui.jquery;
		form = layui.form;


		//自定义验证规则
		form.verify({
			pass: [/(.+){6,16}$/, '密码必须6到16位']
		});


 	form.on("submit(updAdmin)",function(data){
 		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
    		type: "post",
            url: "/teacher/updatePwd",
            data:data.field,
			dataType:"json",
			success:function(d){

				if(d.code==200){
		        	msg="修改成功！两秒钟后自动跳转到登录页面";
					layer.msg(msg);
				}else if(d.code==500){
					layer.msg(d.msg);
					return false;
				}
				else{
					layer.msg("系统错误！");
				}


			}
        });
 		setTimeout(function(){
 			if (msg=="修改成功！两秒钟后自动跳转到登录页面"){
				location.href = "/logout";
			}else {
				layer.close(index);
				layer.closeAll("iframe");
				//刷新父页面
				parent.location.reload();

			}
        },2000);
 		return false;
 	})

})

