layui.config({
	base : "/static/js/"
}).use(['form','layer','jquery','laypage','table','laytpl'],function(){
	var form = layui.form,table = layui.table;
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		$ = layui.jquery;
		//数据表格
		table.render({
			id:'adminList',
		    elem: '#adminList'
			,method:'POST',
			url: '/frame/listUser' //数据接口
		    ,parseData: function(res){ //res 即为原始返回的数据
				return {
					"code": res.code, //解析接口状态
					"msg": res.msg, //解析提示文本
					"count": res.content.total, //解析数据长度
					"data": res.content.rows //解析数据列表
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
              ,{field:'id', title: 'ID', sort: true, width:80}
              ,{field:'name', title: '用户名'}
				,{field:'details', title: '学校'}
              ,{title: '角色',toolbar: '#barLook'}
              ,{title: '操作',toolbar: '#barEdit'}
		    ]]
				,page: true //开启分页
				,where: {timestamp: (new Date()).valueOf()}
		  });




		//监听工具条
		  table.on('tool(test)', function(obj){
		    var data = obj.data,adminId=$("#adminId").val();
		    if(obj.event === 'del'){
		    	if(data.id==adminId){
		    		layer.msg("不允许删除自己！",{icon: 5});
		    		return;
		    	}
				if(data.name=='admin'){
					layer.msg("不允许删除管理员！",{icon: 5});
					return;
				}
		      layer.confirm('真的删除行么', function(index){
		    	  $.ajax({
		    		  url:'/frame/delUser?id='+data.id,
		    		  type : "POST",
		    		  success : function(d){

		    			  if(d.code==200){
		    				  obj.del();
							  layer.msg("删除成功！",{icon: 1});
							  // parent.location.reload();
		    			  }else{
		    				  layer.msg("删除失败！",{icon: 5});
		    			  }
		    		  }
		    	  })
		        layer.close(index);
		      });
		    } else if(obj.event === 'edit'){

				if(data.name=='admin'){
					layer.msg("不允许编辑自己！",{icon: 5});
					return;
				}
		      layer.open({
		    	  type: 2,
		    	  title:"信息修改",
		    	  area: ['401px', '271px'],
		    	  content:"/page/editAdmin/"+data.id, //这里content是一个普通的String
				  end: function(index, layero){
					  //do something
					  table.reload('adminList', {})
				  }
		      })
		    }else if(obj.event === 'editUserRole'){
				layer.open({
					type: 2,
					title:"分配角色",
					area: ['401px', '282px'],
					area: ['401px', '282px'],
					content : "/page/editUserRole/"+data.id+"/"+data.name, //这里content是一个普通的String
				})
			}
		  });


	//添加
	$(".adminAdd_btn").click(function(){
		layer.open({
			type: 2,
			title:"添加管理员",
			area: ['380px', '400px'],
			content : "/page/addAdmin", //这里content是一个普通的String
			end: function(index, layero){
				//do something
				table.reload('adminList', {})
			}

		})

		$(window).resize(function(){
			layui.layer.full(index);
		})
		layui.layer.full(index);
	})
})

//格式化时间
function formatTime(datetime,fmt){
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
