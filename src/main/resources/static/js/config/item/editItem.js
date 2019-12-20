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

	layer.ready(function(){
		$.ajax({
			type: "post",
			url: "/configDefine/typeList",
			success:function(data){
				var oldtype = $('#oldtype').val();
				$('#type').empty();
				var t;
				if(data.code==200){
					for ( i in data.content){
						if(data.content[i].value==oldtype){
							t="<option selected value="+data.content[i].value+">"+data.content[i].key+"</option>"
						}else {
							t="<option value="+data.content[i].value+">"+data.content[i].key+"</option>"
						}

						$('#type').append(t);
					}
				}else {
					t='<option value="1" selected="selected">暂无相关信息</option>'
					$('#type').append(t);
				}
				form.render('select');
			}
		});
	});


 	form.on("submit(eidtItem)",function(data){
 		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
            url: "/configDefine/update",
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
 			//刷新父页面
        },2000);
 		return false;
 	})

})
