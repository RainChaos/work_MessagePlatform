layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage','table','laytpl'],function(){
	var form = layui.form,table = layui.table;
	layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		$ = layui.jquery;

	var sc_code = $("#scCode").val();

	//展示原有数据
	$.ajax({
		type:"post",
		url:'/school/getSchoolWx?scCode='+sc_code  //数据接口
		,success : function(d){
			if(d.code==200){
				if (d.content==null||d.content==""){

				}else {
					$("#appId").val(d.content.appId);
					$("#appSecret").val(d.content.appSecret);
					$("#normalTmp").val(d.content.normalTmp);
					$("#ascTmp").val(d.content.ascTmp);
				}



				}


			}

	})



	//点击修改事件

	// form.on("submit(edit)",function(data){
	// 	//弹出loading
	// 	//弹出loading
	// 	var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
	// 	var msg;
    //
	// 	var scCode = $("#scCode").val();
	// 	var appId = $("#appId").val();
	// 	var appSecret = $("#appSecret").val();
	// 	var normalTmp = $("#normalTmp").val();
	// 	var ascTmp = $("#ascTmp").val();
	// 	// layer.alert("/school/updateSchoolWx?scCode="+scCode);
	// 	// return false;
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
	// 		layer.closeAll("iframe");
	// 	},2000);
	// 	return false;
	// });
    //


	form.on("submit(save)",function(data){
		//弹出loading

		//弹出loading
		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
		var msg;
		var scCode = $("#scCode").val();
		var appId = $("#appId").val();
		var appSecret = $("#appSecret").val();
		var normalTmp = $("#normalTmp").val();
		var ascTmp = $("#ascTmp").val();




		$.ajax({
			type: "post",
			url: "/school/updateSchoolWx?scCode="+scCode
			+"&appId="+appId
			+"&appSecret="+appSecret
			+"&normalTmp="+normalTmp
			+"&ascTmp="+ascTmp,
			dataType:"json",
			success:function(d){
				if(d.code==200){
					msg="保存成功！";
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
