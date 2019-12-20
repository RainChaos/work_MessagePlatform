var $;
var $form;
var form;
layui.config({
	base : "js/"
}).use(['form','layer','jquery','laydate'],function(){
	var layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,laydate = layui.laydate;
		$ = layui.jquery;
		form = layui.form;
		//自定义验证规则
		form.verify({
			pass: [/(.+){6,16}$/, '密码必须6到16位']
			,repass: function(value){
				var repassvalue = $('#password').val();
				if(value != repassvalue){
					return '两次输入的密码不一致!';
				}
			}
		});
 	form.on("submit(addAdmin)",function(data){
 		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
    		type: "post",
            url: "/frame/addUser",
            data:data.field,
			dataType:"json",
			success:function(d){
				if(d.code==200){
		        	msg="添加成功！";
					layer.msg(msg,{icon: 1});
				}else{
		        	msg="添加失败";
					layer.msg(msg,{icon: 5});
				}
			}
        });
 		setTimeout(function(){
 			layer.close(index);
 			layer.closeAll("iframe");
        },2000);
 		return false;
 	})
}
)

