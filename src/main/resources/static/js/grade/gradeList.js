layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage','table'],function(){
	var form = layui.form,table = layui.table;
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		$ = layui.jquery;
    active = {
        search : function() {
            var sc_code = $('#sc_code');
            //执行重载
            table
                .reload(
                    'gradeList',
                    {
                        page : {
                            curr : 1
                            //重新从第 1 页开始
                        },
                        where : {
                            //key: {
                            sc_code : sc_code
                                .val()
                        }
                    });
        }
    };
		
		//数据表格
		table.render({
			id:'gradeList',
		    elem: '#gradeList'
		    ,url: '/grade/listGrade' //数据接口
			,method:'POST'
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
		    ,cellMinWidth:10
		    ,limit:10//每页默认数
		    ,limits:[10,20,30,40]
		    ,cols: [[ //表头
              {type:'checkbox'}
				,{field:'autoAdd', width:70, title: '序号',templet:'#autoAdd',unresize:true}
				,{field:'schoolName', title: '学校', sort: true}
              ,{field:'gr_code', title: '年级代码', sort: true}
              ,{field:'name', title: '名称'}
              ,{field:'ordernumber', title: '排序', sort: true}
              ,{title: '操作',toolbar: '#barEdit'}
		    ]]
				,page: true //开启分页
				,where: {timestamp: (new Date()).valueOf()}
		  });

		//监听工具条
		  table.on('tool(gradeList)', function(obj){
		    var data = obj.data;
		    if(obj.event === 'del'){
		      layer.confirm('真的删除行么', function(index){
		    	  $.ajax({
		    		  url:'/grade/delGrade?gr_code='+data.gr_code,

		    		  type : "post",
		    		  success : function(d){
		    			  if(d.code==200){
		    				  //obj.del();
							  layer.msg("删除成功！");
		    				  table.reload('gradeList', {})
		    			  }else{
							  var jsonObj = $.parseJSON(d);
							  layer.msg(jsonObj.msg,{icon: 5});
		    			  }
		    		  }
		    	  })
		        layer.close(index);
		      });
		    } else if(obj.event === 'edit'){

				layer.open({
					type: 1 //Page层类型
					//,area: ['500px', '300px']
					,btn:["确定","取消"]
					,title: '年级修改'
					,skin: 'layui-layer-prompt'
					,content: "<div class=''>" +
					"<input type='hidden' name='gr_code' value="+data.gr_code+">" +
					"<div class='layui-form-item'><label class='layui-form-label'>学校</label><div class='layui-input-block'>" +
					"<input type='text' name='sc_code' style='width: 180px;' readonly class='layui-layer-input' value="+data.schoolName+">" + "</div></div>" +
					"<div class='layui-form-item'><label class='layui-form-label'>年级名称</label><div class='layui-input-block'>" +
					"<input type='text' name='name' style='width: 180px;' placeholder='请输入年级名称' class='layui-layer-input' value="+data.name+">" + "</div></div>" +
					"<div class='layui-form-item'><label class='layui-form-label'>排序</label><div class='layui-input-block'>" +
					"<input type='text' name='orderNumber' style='width: 180px;' placeholder='排序（整数）' class='layui-layer-input' lay-verify='number' value="+data.ordernumber+"></div></div></div>"
					,yes: function(index, layero){

						var gr_code= $(layero).find("input[name='gr_code']").val();
						var name= $(layero).find("input[name='name']").val();
						var orderNumber= $(layero).find("input[name='orderNumber']").val();

						var numReg = /^[0-9]*$/;
						if(name == ''){
							layer.msg('年级不能为空',{icon:5});
						}else if(!numReg.test(orderNumber)){
							layer.msg('排序请输入整数',{icon:5});
						}else{
							$.ajax({
								url:'/grade/updateGrade?name='+name+"&orderNumber="+orderNumber+"&gr_code="+gr_code,
								type : "post",
								success : function(d){
									if(d.code==200){
										//obj.del();
										layer.msg("修改成功！");
										table.reload('gradeList', {})
									}else{
										layer.msg(d.msg,{icon: 5});
									}
								}
							})
							layer.close(index);
						}
					}
				});
				//给年级设置
		    }else if(obj.event === 'editmenu'){
				    layer.open({
				  	  type: 2,
				  	  title:"编辑年级",
				  	  area: ['380px', '600px'],
				  	  content:"/page/editgradeMenu/"+data.id+"/"+data.name, //这里content是一个普通的String
				    })
			}
		  });

    //查询
    $(".search_btn").click(function() {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    })
	//添加年级
	$(".gradeAdd_btn").click(function(){
		layer.open({
			type: 2,
			title:"添加年级",
			area: ['400px', '350px'],
			content : "/page/addGrade", //这里content是一个普通的String
			end: function(index, layero){
				//do something
				table.reload('gradeList', {})
			}
		})

		//改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
		$(window).resize(function(){
			layui.layer.full(index);
		})
		layui.layer.full(index);
	})
})
