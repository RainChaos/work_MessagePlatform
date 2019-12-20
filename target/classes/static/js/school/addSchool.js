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
 	form.on("submit(addSchool)",function(data){
 		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
    		type: "post",
            url: "/school/addSchool",
            data:data.field,
			dataType:"json",
			success:function(d){
				if(d.code==200){
		        	msg="添加成功！";
					layer.msg(msg,{icon:1});
				}else{
					msg=d.msg;
					layer.msg(msg,{icon:5});
				}

			}
        });
 		setTimeout(function(){
 			layer.close(index);
 			layer.closeAll("iframe");
 			//刷新父页面
        },2000);
 		return false;
 	})

})
