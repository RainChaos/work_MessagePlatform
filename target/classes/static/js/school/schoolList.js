

var json;
layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage','table','laytpl'],function(){
	var form = layui.form,table = layui.table;
	layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		$ = layui.jquery;
	//数据表格
	table.render({
		id:'schoolList',
		elem: '#schoolList'
		,url: '/school/listSchool' //数据接口
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
		,cellMinWidth: 80
		,toolbar : true
		,limit:10//每页默认数
		,limits:[10,20,30,40]
		,cols: [[ //表头
			{type:'checkbox'}
			,{field:'autoAdd', width:70, title: '序号',templet:'#autoAdd',unresize:true}
			,{field:'sc_code', title: '学校代码', sort: true, width:80}
			// ,{field:'pimg', title: '图片',templet:'#tableImg'}
			,{field:'name', title: '名称'}
			,{field:'address', title: '地址'}
			,{field:'contacts', title: '联系方式'}
			,{field:'contactphone', title: '手机号'}
			,{field:'createTime', title: '创建时间',templet: '<div>{{ formatTime(d.createtime,"yyyy-MM-dd hh:mm:ss")}}</div>'}
			,{field:'status', title: '状态',templet: '#statusTpl',width:100}
			,{field:'statusreason', title: '原因'}
			,{field:'statustime', title: '时间',templet: '<div>{{ formatTime(d.statustime,"yyyy-MM-dd hh:mm:ss")}}</div>'}
			,{title: '操作',toolbar: '#barEdit',width:320}
		]]
		,page: true //开启分页
	});
	//监听工具条
	table.on('tool(test)', function(obj){
		var data = obj.data;
		if(obj.event === 'del'){
			layer.confirm('真的删除行么?', function(index){
				$.ajax({
					url:'/school/delSchool?sc_code='+data.sc_code,
					type : "POST",
					success : function(d){

						if(d.code==200){
							table.reload('schoolList', {})
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
				,btn:["修改","取消"]
				,title: '信息修改'
				,skin: 'layui-layer-prompt'
				,content:
				"<input type='hidden' name='sc_code' value="+data.sc_code+">" +
				"<div class='layui-form-item'><label class='layui-form-label'>学校名称</label><div class='layui-input-block'>"+
				"<input type='text' class='layui-input userName' lay-verify='required' placeholder='请输入学校名称' name='name' value="+data.name+">" +"</div></div>" +
				"<div class='layui-form-item'><label class='layui-form-label'>学校地址</label><div class='layui-input-block'>"+
				"<input type='text' class='layui-input userName' lay-verify='required' placeholder='请输入学校地址' name='address' value="+data.address+">" +"</div></div>" +
				"<div class='layui-form-item'><label class='layui-form-label'>联系人</label><div class='layui-input-block'>"+
				"<input type='text' class='layui-input userName' lay-verify='required' placeholder='请输入联系人' name='contacts' value="+data.contacts+">" +"</div></div>" +
				"<div class='layui-form-item'><label class='layui-form-label'>联系电话</label><div class='layui-input-block'>"+
				"<input type='text' class='layui-input userName' lay-verify='required' placeholder='请输入联系电话' name='contactPhone' value="+data.contactphone+">" +"</div></div>"

				,yes: function(index, layero){
					var sc_code= $(layero).find("input[name='sc_code']").val();
					var name= $(layero).find("input[name='name']").val();
					var address= $(layero).find("input[name='address']").val();
					var contacts= $(layero).find("input[name='contacts']").val();
					var contactPhone= $(layero).find("input[name='contactPhone']").val();
					var menhuKey= $(layero).find("input[name='menhuKey']").val();
					data.sc_code=sc_code;
					data.name=name;
					data.address=address;
					data.contacts=contacts;
					data.contactPhone=contactPhone;
					data.menhuKey=menhuKey;
					if(!data.status){
						data.status="false";
					}

					var phoneReg = /^1\d{10}$/;
					if(name == ''){
						layer.msg('学校名称不能为空',{icon:5});
					}else if(address == ''){
						layer.msg('学校地址不能为空',{icon:5});
					}else if(contacts == ''){
						layer.msg('联系人不能为空',{icon:5});
					}else if(!phoneReg.test(contactPhone)){
						layer.msg('请输入正确的手机号',{icon:5});
					}else{
						$.ajax({
							type:"post",
							url:"/school/updateSchool",
							data:data,
							type:json,
							success : function(d){

								if(d.code==200){
									//obj.del();
									layer.msg("修改成功！");
									table.reload('schoolList', {})
								}else{
									layer.msg("系统错误！",{icon: 5});
								}
							}
						})
						layer.close(index);
					}
				}

			});
		}else if(obj.event==='IPEdit'){
			layer.open({
				type: 2,
				title:"学校IP访问白名单",
				area: ['476px', '500px'],
				content:"/page/school/IPForm?sc_code="+data.sc_code, //这里content是一个普通的String
			})

		}else if(obj.event==='menhuEdit'){
			layer.prompt({
				formType: 2,
				title: '请输入门户Key',
				value: data.menhukey,
			}, function(value, index, elem){
				$.ajax({
					url:'/school/setMenhuKey?sc_code='+data.sc_code+"&menhuKey="+value,
					type : "POST",
					success : function(d){
						if(d.code==200){
							layer.msg('配置成功！',{icon: 1});
							table.reload('schoolList', {})
						}else{
							layer.msg(d.msg,{icon: 5});
						}
					}
				})
				layer.close(index);
			});


		}else if(obj.event==='wxEdit'){
			var sc_code = data.sc_code;
			layer.open({
				type: 2,
				title:"微信配置",
				area: ['509px', '431px'],
				content:"/page/school/wxConfig?scCode="+data.sc_code, //这里content是一个普通的String
			})
		}
	});



	//添加角色
	$(".schoolAdd_btn").click(function(){
		layer.open({
			type: 2,
			title:"添加学校",
			area: ['474px', '430px'],
			content:"/page/addSchool", //这里content是一个普通的String
			end: function(index, layero){
				//do something
				table.reload('schoolList', {})
			}
		})
	})


	//监听锁定操作
	form.on('switch(lockStatus)', function(obj){
		/*  layer.tips(this.value + '-- ' + this.name + '：'+ obj.elem.checked, obj.othis);  */
		var sc_code = obj.elem.value;
		var status = false;
		if(obj.elem.checked){
			layer.confirm('是否启用？', {
				btn: ['是','否'] //按钮
				,cancel: function(index, layero){
					//取消操作，点击右上角的X
					table.reload('schoolList', {});
				}
			}, function(){
				//是

				layer.prompt({
					formType: 2,
					title: '请输入启用原因',
				}, function(value, index, elem){
					status = true;
					$.ajax({
						url:'/school/setStatus?sc_code='+sc_code+'&status='+status+"&reason="+value,
						type : "POST",
						success : function(d){

							if(d.code==200){
								layer.msg('启用成功！',{icon: 1});
							}else{
								layer.msg(d.msg,{icon: 5});
							}
							table.reload('schoolList', {});
						}
					})
					layer.close(index);
				});
			}, function(){
				//否

				table.reload('schoolList', {});
			});



		}else{

			layer.confirm('是否禁用？', {
				btn: ['是','否'] //按钮
				,cancel: function(index, layero){
					//取消操作，点击右上角的X
					table.reload('schoolList', {});
				}
			}, function(){
				layer.prompt({
					formType: 2,
					title: '请输入禁用原因',
				}, function(value, index, elem){
					$.ajax({
						url:'/school/setStatus?sc_code='+sc_code+'&status='+status+"&reason="+value,
						type : "POST",
						success : function(d){

							if(d.code==200){
								layer.msg('禁用成功！',{icon: 1});
							}else{
								layer.msg(d.msg,{icon: 5});
							}
							//重载表格
							table.reload('schoolList', {});
						}
					})
					layer.close(index);
				});
			}, function(){
				//否
				table.reload('schoolList', {});
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

