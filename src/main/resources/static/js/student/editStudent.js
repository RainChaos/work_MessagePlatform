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

	$(function () {
		//从父层获取值，json是父层的全局js变量。eval是将该string类型的json串变为标准的json串
		var parent_json = eval('('+parent.json+')');
	});
 	form.on("submit(editStudent)",function(data){
 		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
    		type: "post",
            url: "/student/update",
            data:data.field,
			dataType:"json",
			success:function(d){

				if(d.code==200){
		        	msg="更新成功！";
				}else{
		        	msg="系统错误！";
				}
				layer.msg(msg);
			}
        });
 		setTimeout(function(){
 			layer.close(index);

 			layer.closeAll("iframe");
        },2000);
 		return false;
 	})

})
