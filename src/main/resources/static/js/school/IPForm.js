layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage','table','laytpl'],function(){
	var form = layui.form,table = layui.table;
	layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		$ = layui.jquery;

	var sc_code = $("#sc_code").val();

	//展示原有数据
	$.ajax({
		type:"post",
		url:'/school/getIPList?sc_code='+ sc_code  //数据接口
		,success : function(d){

			if(d.code==200){
				for (i = 0; i < d.content.length; i++) {
					var oritext= $('#iptext').val(); //先获取原有的值
					$("#iptext").val(oritext+d.content[i].ip_pass+"\r\n");
					$("#iptext").attr("disabled","disabled");
					if (d.content[i].ip_pass==""||d.content[i].ip_pass==null){
						$("#iptext").val("暂无数据");
					}
				}
			}else{
			}
		}
	})



	//点击修改事件
	//修改按钮不显示    提交按钮显示     文本可编辑
	$("#edit").click(function(){
		$("#iptext").removeAttr("disabled");
		$("#edit").hide();
		$("#save").show();
	})

	//保存修改事件
	$("#save").click(function(){
		var iptextlist = $('#iptext').val();
		var newiptext = iptextlist.replace(/\s*/g,"");
		if (newiptext==""){
			layer.msg("内容不能为空！",{icon:5});
			return;
		}

		//校验内容
		var ip6reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
		var ip4reg = /^((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$/

		var IPCheck = newiptext.split(";");
		for(i=0;i<IPCheck.length;i++){
			if(ip6reg.test(IPCheck[i])== false){
				if(ip4reg.test(IPCheck[i])== false){
					layer.alert("IP地址格式不正确",{ico:5});
					var oritext= $('#iptext').val(); //先获取原有的值
					$("#iptext").val(iptextlist);
					$("#iptext").removeAttr("disabled");
					$("#edit").hide();
					$("#save").show();
					return;
				}
			}else {

			}
		}

		//弹出loading
		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
		var msg;

		$.ajax({
			type: "post",
			url: "/school/setIP?sc_code="+sc_code+"&ipAddr="+newiptext,
			dataType:"json",
			success:function(d){

				if(d.code==200){
					msg="修改成功！";
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
			//parent.location.reload();
		},2000);
		return false;

	})
});


