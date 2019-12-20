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
 	form.on("submit(editUserRole)",function(data){
		var arr = new Array();
		$("input:checkbox[name='rids']:checked").each(function(i){
			arr[i] = $(this).val();
		});
		var uid = $("#uid").val();
 		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
    		type: "post",
            url: "/frame/setUserRoles?uid="+uid+"&rids="+arr,
			success:function(d){
				if(d.code==200){
		        	msg="设置成功！";
					layer.msg(msg,{icon: 1});
				}else{
		        	msg="设置失败";
					layer.msg(msg,{icon: 5});
				}
			}
        });
 		setTimeout(function(){
 			layer.close(index);
 			layer.closeAll("iframe");
			//刷新父页面
			parent.location.reload();
        },2000);
 		return false;
 	})
})
