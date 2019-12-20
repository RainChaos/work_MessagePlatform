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
	    upload = layui.upload;
	    layedit = layui.layedit;
		form.render();

	upload.render({
		method: 'post'
		//,data:_jsonDate
		,elem: '#test8'
		,accept:'file'
		,exts: 'xls|xlsx'
		,url: '/uploadExcel'
		,auto: true
		,choose: function(obj){
			//回显文件名
			obj.preview(function(index, file, result){
				$('#filenameview').text(file.name);//显示文件名
			});
		}
		, done: function (res, index, upload) {
			$('#sheetName').empty();
			$('#sheetName').append('<option value="0">请选择工作表名称</option>');
			var t;

			if(res.code==200){
				for ( i in res.content.sheetName){
					t="<option value="+res.content.sheetName[i]+">"+res.content.sheetName[i]+"</option>"
					$('#sheetName').append(t);
					$('#fileName').val(res.content.fileName);

				}
			}else {
				t='<option value="1" selected="selected">暂无相关信息</option>'
				$('#sheetName').append(t);
			}
			form.render('select');

		}
		, error: function () {
			//请求异常回调
			layer.msg("系统错误！");
		}
	});

	// 用户点击上传按钮
	form.on("submit(btnupload)",function(data){
		//弹出loading
		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
		var msg;

		//校验用户是否选择
		var fileName = $('#fileName').val();
		var sheetName = $('#sheetName').val();

		if (fileName==""||fileName==null){
			layer.msg("请选择要导入的教师文件！",{icon: 5});
			return;
		}
		if(sheetName=="0"){
			layer.msg("请选择工作表名称！",{icon: 5});
			return;
		}
		if(sheetName=="1"){
			layer.msg("导入的文件存在问题！",{icon: 5});
			return;
		}

		$.ajax({
			type: "post",
			url: "/teacher/import",
			data:data.field,
			dataType:"json",
			success:function(d){

				if(d.code==200){
					//显示导入进度

					setTimeout(function(){
						$.ajax({
							type: "post",
							url: "/teacher/importMsg",
							success:function(d){

								if(d.code==200){
									layer.msg("导入成功！",{icon: 1});
									setTimeout(function(){
										layer.close(index);
										layer.closeAll("iframe");
										//刷新父页面
										parent.location.reload();
									},2000);
								}else if(d.code==500){
									msg="导入失败!原因："+d.msg;
									layer.msg(msg,{icon: 5});

								}else {
									msg=d.msg+"导入失败，具体请看文件"+d.content;
									layer.msg(msg,{icon: 5});
								}
							}
						});
					},2000);


				}else{
					msg="添加失败";
					layer.msg(msg,{icon: 5});
				}
			}
		});

		return false;
	});


























	form.on("submit(addSchool)",function(data){
 		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
    		type: "post",
            url: "/student/add",
            data:data.field,
			dataType:"json",
			success:function(d){

				if(d.code==200){
		        	msg="添加成功！";
					layer.close(index);
					layer.closeAll("iframe");
					//刷新父页面
					parent.location.reload();
				}else{

					msg="系统错误！";
				}
				layer.msg(msg);
			}
        });
 		setTimeout(function(){

        },2000);
 		return false;
 	})

})
