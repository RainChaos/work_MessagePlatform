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

	/*时间插件*/
	var startTime =laydate.render({
		elem: '#startTime',
		type: 'datetime',
		// min:'nowTime',
		max: '2035-12-31 12:30:00',
		calendar: true,
		lang: 'ch',
		done: function(value, date, endDate) {
			endLayDate.config.min = {
				year: date.year,
				month: date.month - 1,
				date: date.date,
				hours: date.hours,
				minutes: date.minutes,
				seconds: date.seconds +1
			};
//
		},
	});
	/*时间插件*/
	var endLayDate = laydate.render({
		elem: '#endTime',
		type: 'datetime',
		max: '2035-12-31 12:30:00',
		btns: ['clear', 'confirm'],  //clear、now、confirm
		done: function(value, date, endDate) {
			startTime.config.max = {
				year: date.year,
				month: date.month - 1,
				date: date.date,
				hours: date.hours,
				minutes: date.minutes,
				seconds: date.seconds -1
			};

		},
	});
 	form.on("submit(editLeave)",function(data){

		var time2 = formatTime(new Date(),"yyyy-MM-dd HH:mm:ss");
		data.statusdate=time2;


		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;
 		$.ajax({
    		type: "post",
            url: "/leave/update",
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
	 		parent.location.reload();
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

