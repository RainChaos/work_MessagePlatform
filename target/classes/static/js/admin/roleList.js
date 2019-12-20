layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage','table'],function(){
	var form = layui.form,table = layui.table;
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		$ = layui.jquery;
		
		//数据表格
	var tableIns = table.render({
			id:'roleList',
		    elem: '#roleList'
		    ,url: '/frame/listRole' //数据接口
			,method:'POST'
			,parseData: function(res){ //res 即为原始返回的数据
		    return {
			"code": res.code, //解析接口状态
			"msg": res.msg, //解析提示文本
			"count": res.content.total, //解析数据长度
			"data": res.content.rows //解析数据列表
		    };
	      }
		    ,cellMinWidth:10
		    ,limit:10//每页默认数
		    ,limits:[10,20,30,40]
		    ,cols: [[ //表头
              {type:'checkbox'}
              ,{field:'autoAdd', width:70, title: '序号',templet:'#autoAdd',unresize:true}
              ,{field:'id', title: 'ID', sort: true}
              ,{field:'name', title: '角色名'}
              ,{title: '操作',toolbar: '#barEdit'}
		    ]]
				,page: true //开启分页
				,where: {timestamp: (new Date()).valueOf()}
		  });

		//监听工具条
		  table.on('tool(roleList)', function(obj){
		    var data = obj.data;
		    if(obj.event === 'del'){
		    	if(data.name=='管理员'){
		    		layer.msg("不允许操作此角色！",{icon: 5});
		    		return;
		    	}
		      layer.confirm('真的删除行么', function(index){
		    	  $.ajax({
		    		  url:'/frame/delRole?id='+data.id,
		    		  type : "post",
		    		  success : function(d){

		    			  if(d.code==200){
		    				  obj.del();
		    				  table.reload('roleList', {})
		    			  }else{
		    				  layer.msg("系统错误！",{icon: 5});
		    			  }
		    		  }
		    	  })
		        layer.close(index);
		      });
		    } else if(obj.event === 'edit'){
		    	if(data.name=='管理员'){
		    		layer.msg("不允许操作此角色！",{icon: 5});
		    		return;
		    	}
				layer.prompt({
					formType: 0,
					value: data.name,
					title: '请输入角色名称',
					area: ['100px', '50px'] //自定义文本域宽高
				}, function(value, index, elem){
					//alert(value); //得到value
					$.ajax({
						url:'/frame/updateRole?id='+data.id+"&name="+value,
						type : "post",
						success : function(d){

							if(d.code==200){
								//obj.del();
								layer.msg("修改成功",{icon: 1});
								table.reload('roleList', {})
							}else{
								layer.msg("修改失败！该角色名称已经存在！",{icon: 5});
							}
						}
					})
					layer.close(index);
				});
				//给角色设置菜单
		    }else if(obj.event === 'editmenu'){

				    layer.open({
				  	  type: 2,
				  	  title:"编辑角色",
				  	  area: ['380px', '355px'],
				  	  content:"/page/editRoleMenu/"+data.id+"/"+data.name, //这里content是一个普通的String
				    })


			}
		  });

	//添加角色
	$(".roleAdd_btn").click(function(){

		layer.prompt({
			formType: 0,
			title: '请输入角色名称',
			area: ['100px', '50px'] //自定义文本域宽高
		}, function(value, index, elem){
			//alert(value); //得到value
			$.ajax({
				url:'/frame/addRole?name='+value,
				type : "post",
				success : function(d){

					if(d.code==200){
						layer.msg("添加成功",{icon: 1});
						table.reload('roleList', {})

					}else{
						layer.msg("添加失败！",{icon: 5});
						table.reload('roleList', {})
					}
				}
			})
			layer.close(index);

		});
	})

	
})


