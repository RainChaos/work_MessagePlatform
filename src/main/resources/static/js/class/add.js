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
	//监听学校选择
	form.on('select(sc_code)', function(data){
		var sc_code =  data.value;
		$.ajax({
			type: "post",
			url: "/grade/listSchoolGrade?sc_code="+sc_code,
			success:function(data){

				$('#gr_code').empty();
				var t,t1;
				t1 ='<option value="allClassData">全部</option>';
				if(data.code==200){
					$('#gr_code').append(t1);
					for ( i in data.content.rows){
						t="<option value="+data.content.rows[i].gr_code+">"+data.content.rows[i].name+"</option>"
						$('#gr_code').append(t);
					}
				}else {
					t='<option value="1" selected="selected">暂无相关信息</option>'
					$('#gr_code').append(t);
				}
				form.render('select');
			}
		});
	});

 	form.on("submit(addClass)",function(data){
 		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
    		type: "post",
            url: "/class/addClass",
            data:data.field,
			dataType:"json",
			success:function(d){
				if(d.code==200){
		        	msg="添加成功！";
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
