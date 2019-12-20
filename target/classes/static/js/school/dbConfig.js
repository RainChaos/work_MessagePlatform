layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage','table','laytpl'],function(){
	var form = layui.form,table = layui.table;
	layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		$ = layui.jquery;


	form.on("submit(save)",function(data){
		//弹出loading
		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
		var msg;
		var scCode = $("#scCode").val();
		var url = $("#url").val();
		var userName = $("#userName").val();
		var password = $("#password").val();
		var code = $("#code").val();
		var dbType = $("#dbType").val();

		$.ajax({
			type: "post",
			url: "/school/updateSchoolDB?scCode="+scCode
			+"&url="+url
			+"&userName="+userName
			+"&password="+password
			+"&code="+code
			+"&dbType="+dbType,
			dataType:"json",
			success:function(d){
				if(d.code==200){
					msg="保存成功！";
				}else{
					msg="保存失败！";
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
	});


    //
	// $("#edit").click(function(){
	// 	//弹出loading
	// 	var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
	// 	var msg;
    //
	// 	var scCode = $("#scCode").val();
	// 	var appId = $("#appId").val();
	// 	var appSecret = $("#appSecret").val();
	// 	var normalTmp = $("#normalTmp").val();
	// 	var ascTmp = $("#ascTmp").val();
    //
	// 	$.ajax({
	// 		type: "post",
	// 		url: "/school/updateSchoolWx?scCode="+scCode
	// 		+"&appId="+appId
	// 		+"&appSecret="+appSecret
	// 		+"&normalTmp="+normalTmp
	// 		+"&ascTmp="+ascTmp,
	// 		dataType:"json",
	// 		success:function(d){
	// 			if(d.code==200){
	// 				msg="修改成功！";
	// 			}else{
	// 				msg="系统错误！";
	// 			}
	// 			layer.msg(msg);
	// 		}
	// 	});
	// 	setTimeout(function(){
	// 		layer.close(index);
	// 		 layer.closeAll("iframe");
	// 		//刷新父页面
	// 		parent.location.reload();
	// 	},2000);
	// 	return false;
    //
	// })



	// //保存修改事件
	// $("#save").click(function(){
	// 	//弹出loading
	// 	var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
	// 	var msg;
	// 	var scCode = $("#scCode").val;
	// 	var appId = $("#appId").val;
	// 	var appSecret = $("#appSecret").val;
	// 	var normalTmp = $("#normalTmp").val;
	// 	var ascTmp = $("#ascTmp").val;
    //
	// 	$.ajax({
	// 		type: "post",
	// 		url: "/school/addSchoolWx?scCode="+scCode
	// 		+"&appId="+appId
	// 		+"&appSecret="+appSecret
	// 		+"&normalTmp="+normalTmp
	// 		+"&ascTmp="+ascTmp,
	// 		dataType:"json",
	// 		success:function(d){
	// 			if(d.code==200){
	// 				msg="修改成功！";
	// 			}else{
	// 				msg="系统错误！";
	// 			}
	// 			layer.msg(msg);
	// 		}
	// 	});
	// 	setTimeout(function(){
	// 		layer.close(index);
	// 		layer.closeAll("iframe");
	// 		//刷新父页面
	// 		parent.location.reload();
	// 	},2000);
	// 	return false;
    //
	// })
});
