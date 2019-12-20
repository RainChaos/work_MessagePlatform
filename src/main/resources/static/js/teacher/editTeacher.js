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


 	form.on("submit(editTeacher)",function(data){

		var time2 = formatTime(new Date(),"yyyy-MM-dd HH:mm:ss");
		data.statusdate=time2;


		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
    		type: "post",
            url: "/teacher/update",
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

//格式化时间
function formatTime(datetime,fmt){
	if(datetime==null||datetime==0){
		return "";
	}
	if (parseInt(datetime)==datetime) {
		if (datetime.length==10) {
			datetime=parseInt(datetime)*1000;
		} else if(datetime.length==13) {
			datetime=parseInt(datetime);
		}
	}
	datetime=new Date(datetime);
	var o = {
		"M+" : datetime.getMonth()+1,                 //月份
		"d+" : datetime.getDate(),                    //日
		"h+" : datetime.getHours(),                   //小时
		"m+" : datetime.getMinutes(),                 //分
		"s+" : datetime.getSeconds(),                 //秒
		"q+" : Math.floor((datetime.getMonth()+3)/3), //季度
		"S"  : datetime.getMilliseconds()             //毫秒
	};
	if(/(y+)/.test(fmt))
		fmt=fmt.replace(RegExp.$1, (datetime.getFullYear()+"").substr(4 - RegExp.$1.length));
	for(var k in o)
		if(new RegExp("("+ k +")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
	return fmt;
}
