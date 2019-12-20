var $;
var $form;
var form;
layui.config({
	base : "/static/js/"
}).use(['form','layer','jquery','upload','layedit'],function(){
	var layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,upload = layui.upload;
		$ = layui.jquery;
		form = layui.form;
	    layedit = layui.layedit;
		form.render();




 	form.on("submit(addClass)",function(data){
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
    		type: "post",
            url: "/student/setGuardian",
            data:data.field,
			dataType:"json",
			success:function(d){

				if(d.code==200){
		        	msg="添加成功！";
				}else if(d.code=500){
					msg="你已经添加该信息了！";
				}else{
					msg="系统错误！";
				}
				layer.msg(msg);
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
