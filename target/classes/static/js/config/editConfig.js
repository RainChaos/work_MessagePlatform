var $;
var $form;
var form;
layui.config({
	base : "/static/js/"
}).use(['form','layer','jquery','upload','layedit','laydate'],function(){
	var layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,upload = layui.upload;
		$ = layui.jquery;
		form = layui.form;
	  layedit = layui.layedit;

	laydate = layui.laydate;
	layer.ready(function(){
		laydate.render({
			elem: '#value'       //指定元素
			,type: 'datetime'
		});
	});

 	form.on("submit(eidtItem)",function(data){
 		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
            url: "/config/update",
			type:"POST",
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
