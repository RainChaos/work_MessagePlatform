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
		form.render();


	 var nowTime=new Date();
    //
	// // 时间选择器初始化 开始时间
	// laydate.render({
	// 	elem: '#startTime',
	// 	type:'datetime',
	// 	min:'nowTime',
	// 	done: function (value, date, endDate) {
	// 		var startDate = new Date(value).getTime();
	// 		var endTime = new Date($('#endTime').val()).getTime();
	// 		if (endTime < startDate) {
	// 			layer.msg('结束时间不能小于开始时间');
	// 			$('#startTime').val($('#endTime').val());
	// 		}
	// 	}
	// })
	// laydate.render({ //结束时间
	// 	elem: '#endTime',
	// 	type:'datetime',
	// 	done: function (value, date, endDate) {
	// 		var startDate = new Date($('#startTime').val()).getTime();
	// 		var endTime = new Date(value).getTime();
	// 		if (endTime < startDate) {
	// 			layer.msg('结束时间不能小于开始时间');
	// 			$('#endTime').val($('#startTime').val());
	// 		}
	// 	}
	// })
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


	//监听根据班级代码获取改版的学生信息
	form.on('select(classCode)', function(data){
		var classCode =  data.value;
		$.ajax({
			type: "post",
			url: "/student/getStuListByclCode?clCode="+classCode+"&page=1&limit=300",
			success:function(data){
				$('#stuName').empty();
				var t;
				if(data.code==200){
					for ( i in data.content.data){
						t="<option value="+data.content.data[i].stuNo+">"+data.content.data[i].stuName+"</option>"
						$('#stuName').append(t);
					}
				}else {
					t='<option value="1" selected="selected">该班级下没有学生</option>'
					$('#stuName').append(t);
				}
				form.render('select');
			}
		});
	});

	//监听学生请假事项模板选择
	form.on('select(muban)', function(data){
		var mubanValue =  data.value;
		if(mubanValue!="请选择"){
		$("#reason").val(mubanValue);
			form.render('select');
		}


	});



	form.on("submit(addClass)",function(data){
 		//弹出loading
 		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
 		var msg;

 		$.ajax({
    		type: "post",
            url: "/leave/add",
            data:data.field,
			dataType:"json",
			success:function(d){

				if(d.code==200){
		        	msg="添加成功！";
				}else{
					msg=d.msg;
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

