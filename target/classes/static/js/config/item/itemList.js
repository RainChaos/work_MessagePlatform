

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
		,method:'POST'
		,url: '/configDefine/list' //数据接口
		,parseData: function(res){ //res 即为原始返回的数据
			return {
				"code": res.code, //解析接口状态
				"msg": res.msg, //解析提示文本
				"data": res.content //解析数据列表
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
			// ,{field:'pimg', title: '图片',templet:'#tableImg'}
			,{field:'name', title: '配置名'}
			,{field:'code', title: '配置代码'}
			,{field:'type', title: '值类型'}
			,{field:'selectValue', title: '选择值'}
			,{field:'remark', title: '备注'}
			,{title: '操作',toolbar: '#barEdit',width:280}
		]]
		,page: true //开启分页
	});
	//监听工具条
	table.on('tool(test)', function(obj){
		var data = obj.data;
		if(obj.event === 'del'){
			layer.confirm('真的删除行么?', function(index){
				$.ajax({
					url:'/configDefine/del?id='+data.id,
					type : "POST",
					success : function(d){
						var obj1 = JSON.parse(d);
						if(d.code==200){
							obj.del();
						}else{
							layer.msg(obj1.msg,{icon: 5});
						}
					}
				})
				layer.close(index);
			});
		} else if(obj.event === 'edit'){
			layer.open({
				type: 2,
				title:"配置项修改",
				area: ['536px', '568px'],
				content:"/page/editItemConfig?id="+data.id, //这里content是一个普通的String
				end: function(index, layero){
					//do something
					layer.close(index); //如果设定了yes回调，需进行手工关闭
					table.reload('schoolList', {})
				}
			})
		}
	});

	//添加角色
	$(".schoolAdd_btn").click(function(){
		layer.open({
			type: 2,
			title:"添加配置项",
			area: ['536px', '568px'],
			content:"/page/addItemConfig", //这里content是一个普通的String
			end: function(index, layero){
				//do something
				layer.close(index); //如果设定了yes回调，需进行手工关闭
				table.reload('schoolList', {})
			}
		})
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

