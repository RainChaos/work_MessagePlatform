layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage','table','laytpl'],function(){
	var form = layui.form,table = layui.table;
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		$ = layui.jquery;

	nowTime = new Date().valueOf(),
		max = null,
		active = {
			search : function() {
				var sc_code = $('#sc_code');
				//执行重载
				table
					.reload(
						'teacherList',
						{
							page : {curr : 1},
							where : {scCode : sc_code.val()
							}
						});
			}
		};

	//加载页面数据
	table
		.render({
			id:'teacherList',
			elem: '#teacherList'
			,method:'POST'
			,url: '/teacher/list' //数据接口
			,parseData: function(res){ //res 即为原始返回的数据
				return {
					"code": res.code, //解析接口状态
					"msg": res.msg, //解析提示文本
					"count": res.content.total, //解析数据长度
					"data": res.content.data //解析数据列表
				};
			}
			,toolbar: '#toolbarDemo'
			,limit : 10//每页默认数
			, limits : [ 10, 20, 30, 40 ]
			,cols: [[ //表头
		{type:'checkbox'}
		,{field:'teNo', title: '教师编号', sort: true}
		,{field:'loginname', title: '登录名'}
		,{field:'name', title: '姓名'}
		,{field:'sex', title: '性别',templet: '#barDemo'}
		,{field:'idcode', title: '身份证号'}
		,{field:'phoneno', title: '电话'}
		,{field:'email', title: '邮箱'}
		,{field:'scCode', title: '学校代码'}
		,{field:'isadmin', title: '是否管理员',templet: '#statusTpl1', unresize: true}
		,{field:'enable', title: '状态',templet: '#statusTpl', unresize: true}
		,{field:'statusreason', title: '原因'}
		// ,{field:'statustime', title: '时间',templet: '<div>{{ formatTime(d.statustime,"yyyy-MM-dd hh:mm:ss")}}</div>'}
		,{title: '操作',toolbar: '#barEdit'}
	]]
		,page: true //开启分页
		,where: {timestamp: (new Date()).valueOf()}
});
		//监听工具条
		  table.on('tool(test)', function(obj){
		    var data = obj.data;
		    if(obj.event === 'del'){
		      layer.confirm('真的删除行么?', function(index){
		    	  $.ajax({
		    		  url:'/teacher/del?teNo='+data.teNo,
		    		  type : "POST",
		    		  success : function(d){

		    			  if(d.code==200){
		    				  table.reload('teacherList', {})
		    			  }else{
		    				  layer.msg("系统错误！",{icon: 5});
		    			  }
		    		  }
		    	  })
		        layer.close(index);
		      });
		    } else if(obj.event === 'edit'){

				layer.open({
					type: 2,
					title:"信息修改",
					area: ['480px', '600px'],
					content:"/page/editTeacher?teNo="+data.teNo //这里content是一个普通的String
				})
		    }
		  });

	//查询
	$(".search_btn").click(function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	})

	//添加角色
	$(".teacherAdd_btn").click(function(){
		if ($("#sc_code").val()==0){
			layer.msg('请先选择学校',{icon: 5});
			return false;
		}
		layer.open({
	    	  type: 2,
	    	  title:"添加教师",
			area: ['476px', '562px'],
	    	  content:"/page/addTeacher?sc_code="+$("#sc_code").val(), //这里content是一个普通的String
	      })
	})

	//监听锁定操作
		  form.on('checkbox(lockStatus)', function(obj){
		   /*  layer.tips(this.value + '-- ' + this.name + '：'+ obj.elem.checked, obj.othis);  */
			  var teNo = obj.elem.id;
			  var enableType = "1";
		    if(obj.elem.checked){
				layer.confirm('是否启用？', {
					btn: ['是','否'] //按钮
					,cancel: function(index, layero){
						//取消操作，点击右上角的X
						table.reload('teacherList', {});
					}
				}, function(){
					//是
					layer.prompt({
						formType: 2,
						title: '请输入解禁原因',
					}, function(value, index, elem){
						$.ajax({
							url:'/teacher/enable?teNo='+teNo+'&enableType='+enableType+"&reason="+value,
							type : "POST",
							success : function(d){

								if(d.code==200){
									layer.msg('设置成功！',{icon: 1});
								}else{
									layer.msg("系统错误！",{icon: 5});
								}
								table.reload('teacherList', {});
							}
						})
						layer.close(index);
					});

				}, function(){
					//否
					table.reload('teacherList', {});
				});


		    }else{
				enableType = "0";
				layer.confirm('是否禁用？', {
					btn: ['是','否'] //按钮
					,cancel: function(index, layero){
						//取消操作，点击右上角的X
						table.reload('teacherList', {});
					}
				}, function(){
					layer.prompt({
						formType: 2,
						title: '请输入禁用原因',
					}, function(value, index, elem){

						$.ajax({
							url:'/teacher/enable?teNo='+teNo+'&enableType='+enableType+"&reason="+value,
							type : "POST",
							success : function(d){

								if(d.code==200){
									layer.msg('设置成功！',{icon: 1});
								}else{
									layer.msg("系统错误！",{icon: 5});
								}
								//重载表格
								table.reload('teacherList', {});
							}
						})
						layer.close(index);
					});
				}, function(){
					//否
					table.reload('teacherList', {});
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

