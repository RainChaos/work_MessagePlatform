

var json;
layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage','table','laydate'],function(){
	var form = layui.form,table = layui.table;
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,laydate = layui.laydate,
		$ = layui.jquery,
	   nowTime = new Date().valueOf(),
		max = null,
		active = {
			search : function() {
				var scCode = $('#sc_code').find("option:selected")
					,startDate = $('#createTimeStart')
					,endDate = $('#createTimeEnd')
					,gr_code = $('#gr_code').find("option:selected")
					,cl_code = $('#clSelect').find("option:selected")
					,stuNo = $('#stuNo');
				//执行重载
				table
					.reload(
						'studentList',
						{
							page : {
								curr : 1
								//重新从第 1 页开始
							},
							where : {
								//key: {
								sc_code : scCode
									.val(),
								startDate : startDate
									.val(),
								endDate : endDate
									.val(),
								gr_code : gr_code
									.val(),
								cl_code : cl_code
								.val(),
								stuNo : stuNo.val()
							}
						});
			}
		};

	var start = laydate.render({
		elem : '#createTimeStart',
		type : 'datetime',
		// max : nowTime,
		btns : [ 'clear', 'confirm' ],
		done : function(value, date) {
			endMax = end.config.max;
			end.config.min = date;
			end.config.min.month = date.month - 1;
		}
	});
	var end = laydate.render({
		elem : '#createTimeEnd',
		type : 'datetime',
		done : function(value, date) {
			if ($.trim(value) == '') {
				var curDate = new Date();
				date = {
					'date' : curDate.getDate(),
					'month' : curDate.getMonth() + 1,
					'year' : curDate.getFullYear()
				};
			}
			start.config.max = date;
			start.config.max.month = date.month - 1;
		}
	})

		//数据表格
		table.render({
			id:'studentList',
		    elem: '#studentList'
			,method:'POST'
		    ,url: '/att/list' //数据接口
			,parseData: function(res){ //res 即为原始返回的数据

		 return {
			"code": res.code, //解析接口状态
			"msg": res.msg, //解析提示文本
			"count":res.total, //解析数据长度
			"data": res.data //解析数据列表
			};
		}
			,response:{
				statusName:'code'
				,statusCode:200
			}
		    ,cellMinWidth: 80
			,toolbar : true
		    ,limit:10//每页默认数
		    ,limits:[10,20,30,40]
		    ,cols: [[ //表头
              {type:'checkbox'}
				,{field:'autoAdd', width:70, title: '序号',templet:'#autoAdd',unresize:true}
				,{field:'schoolName', title: '学校', sort: true,width:137}
				,{field:'gradeName', title: '年级',width: 137}
				,{field:'className', title: '班级',width: 137}
              ,{field:'stuNo', title: '学生编号', sort: true,width:137}
              ,{field:'stuName', title: '学生姓名',width: 137}
				,{field:'actionTimeStr', title: '操作时间',width:180,templet: '<div>{{ formatTime(d.actionTimeStr,"yyyy-MM-dd hh:mm:ss")}}</div>'}
				,{field:'type', title: '考勤状态'}
		    ]]
				,page: true //开启分页
			,where: {timestamp: (new Date()).valueOf()}
		  });
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


	//监听年级选择
	form.on('select(gr_code)', function (data) {
		var gr_code = data.value;
		$.ajax({
			type: "post",
			url: "/class/listByGrCode?gr_code=" + gr_code,
			success: function (data) {
				$('#clSelect').empty();
				var t, t1;
				t1 = '<option value="">请选择班级</option>';
				if (data.code == 200) {
					$('#clSelect').append(t1);
					var classList = data.content;
					for (i in classList) {
						t = "<option value=" + classList[i].cl_code + ">" + classList[i].name + "</option>"
						$('#clSelect').append(t);
					}
				} else {
					t = '<option value="1" selected="selected">暂无相关信息</option>'
					$('#clSelect').append(t);
				}
				form.render('select');
			}
		});
	});
		//监听工具条
		  table.on('tool(studentList)', function(obj){
		    var data = obj.data;
		    if(obj.event === 'del'){
				layer.confirm('删除该信息也会将相应的接收人信息删除，您确定要删除吗？', function(index){
					$.ajax({
						url:'/message/del?id='+data.id,
		    		  type : "POST",
		    		  success : function(d){

		    			  if(d.code==200){

		    				  table.reload('studentList', {})
		    			  }else{
		    				  layer.msg("系统错误！",{icon: 5});
		    			  }
		    		  }
		    	  })
		        layer.close(index);
		      });
		    }else if(obj.event === 'messageDetails'){
		    	//查看接收人信息详情

				layer.open({
					type: 2,
					title:"接收人信息",
					area: ['710px', '600px'],
					content:"/page/detailsList?logId="+data.id+"&typemsg="+data.msgType//这里content是一个普通的String
				})
			}else if(obj.event === 'Details'){
				//查看信息详情
                var sendtime = formatTime(data.sendDate,"yyyy-MM-dd hh:mm:ss");

				var title,content;
				if(data.msgType=="手机短信"){
					title = "手机短信";
					content = "<div style='margin: 30px;color: dimgray;'>"+"<script type='text/html' style='display:block'>"+data.msgContent+"</script></div>";
				}
				if(data.msgType=="微信消息"){
					title = "微信消息";
					content = "<div style='margin: 30px; color: dimgray;'>学校:XXX"+"<br>"+"所在班级:XXX"+"<br>"+"学生姓名:XXX"+"<br>"+"通知人:"+data.teName+"<br>"+"时间:"+sendtime+"<br>"+"通知内容:"+"<br>"+""+data.msgContent+"</div>";
				}

				//多窗口模式，层叠置顶
				layer.open({
					type: 1 //此处以iframe举例
					,title: title
					,area: ['518px', '388px']
					,scrollbar:false
					,shade: 0
					,maxmin: true
					,content: content
					,btn: ['确定'] //只是为了演示
				});
			}

			else if(obj.event === 'imgContentLook'){
				//查看信息详情

				var  content;
				if(data.msgType=="手机短信"){
					title = "手机短信";
					content = "<div style='margin: 30px;color: dimgray;'>"+"<script type='text/html' style='display:block'>"+data.msgContent+"</script></div>"
				}
				if(data.msgType=="微信消息"){
					title = "微信消息";
					content = "<div style='margin: 30px;color: dimgray;'><code>"+data.msgContent+"</code></div>"
				}

				//多窗口模式，层叠置顶
				layer.open({
					type: 1 //此处以iframe举例
					,title: title
					,area: ['300px', '288px']
					,shade: 0
					,content: content
				});
			}
		  });

	//查询
	$(".search_btn").click(function() {

		var scCode = $('#sc_code').find("option:selected")
		if(scCode.val()==""){
			layer.msg("请先选择学校！",{icon:5});
			return;
		}
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	})

	//监听锁定操作
		  form.on('checkbox(lockStatus)', function(obj){
		   /*  layer.tips(this.value + '-- ' + this.name + '：'+ obj.elem.checked, obj.othis);  */
			  var sc_code = obj.elem.value;
			  var status = false;
			  alert(obj.elem.checked);
		    if(obj.elem.checked){

				layer.prompt({
					formType: 2,
					title: '请输入解禁原因',
				}, function(value, index, elem){
					status = true;
					$.ajax({
						url:'/student/setStatus?sc_code='+sc_code+'&status='+status+"&reason="+value,
						type : "POST",
						success : function(d){

							if(d.code==200){
								layer.msg('设置成功！',{icon: 1});
							}else{
								layer.msg("系统错误！",{icon: 5});
							}
							table.reload('studentList', {});
						}
					})
					layer.close(index);
				});
		    }else{
				layer.prompt({
					formType: 2,
					title: '请输入禁用原因',
				}, function(value, index, elem){


					$.ajax({
						url:'/student/setStatus?sc_code='+sc_code+'&status='+status+"&reason="+value,
						type : "POST",
						success : function(d){

							if(d.code==200){
								layer.msg('设置成功！',{icon: 1});
							}else{
								layer.msg("系统错误！",{icon: 5});
							}
							//重载表格
							table.reload('studentList', {});
						}
					})
					layer.close(index);
				});
		    }
		  });
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

