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

 	form.on("submit(addGrade)",function(data){
		var scCode = $('#scCode').find("option:selected").val();
		if(scCode.length==0){
			layer.msg("请先选择学校",{icon:5});
			return;
		}

 		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
    		type: "post",
            url: "/grade/addGrade",
            data:data.field,
			dataType:"json",
			success:function(d){

				if(d.code==200){
					layer.msg("操作成功！",{icon:1});
					setTimeout(function(){
						layer.close(index);
						layer.closeAll("iframe");
					},2000);
				}else{
					layer.msg(d.msg,{icon:5});
				}

			}
        });
 		setTimeout(function(){
			layer.close(index);
 			layer.closeAll("iframe");
        },2000);
 		return false;
 	})

})
