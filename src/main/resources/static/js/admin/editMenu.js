var $;
var $form;
var form;
layui.config({
	base : "/static/js/jsbase/"
}).use(['form','layer','jquery','laydate','iconPicker'],function(){
	var layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,laydate = layui.laydate,iconPicker = layui.iconPicker;
		$ = layui.jquery;
		form = layui.form;
		iconPicker.render({
            // 选择器，推荐使用input
            elem: '#iconPicker',
            // 数据类型：fontClass/unicode，推荐使用fontClass
            type: 'unicode',
            // 是否开启搜索：true/false
            search: true,
            // 是否开启分页
            page: true,
            // 每页显示数量，默认12
            limit: 12,
            // 点击回调
            click: function (data) {
            }
        });
 	form.on("submit(menuForm)",function(data){
 		//弹出loading
		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
		var msg = "发生错误";
 		$.ajax({
    		type: "post",
            url: "/frame/updateModule",
            data:data.field,
			dataType:"json",
			async:false,
			success:function(d){

				if(d.code==200){
					msg="修改成功！";
					layer.msg(msg,{icon: 1});
				}else {
					layer.msg(msg,{icon: 5});
				}
			}
        });
 		setTimeout(function(){
			layer.close(index);
			layer.closeAll("iframe");
			//刷新父页面
			parent.location.reload();
		},2000);
 		return false;
 	})
})
