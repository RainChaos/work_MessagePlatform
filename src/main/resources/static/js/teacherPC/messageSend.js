var $;
var $form;
var form;
layui.config({
	base: "js/"
}).use(['form', 'layer', 'jquery', 'util', 'tree','laydate','layedit'], function() {
	var layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		tree = layui.tree,
		util = layui.util,
		laydate = layui.laydate;
	$ = layui.jquery;
	form = layui.form;
	layedit = layui.layedit;

	//文本编辑器
	layedit.set({
		uploadImage: {
			url:'/file/upload' //接口url
		}
	});

	//富文本编辑器
	var index = layedit.build('content'); //建立编辑器

	//提交时把值同步到文本域中
	form.verify({
		//content富文本域中的lay-verify值
		content: function(value) {
			return layedit.sync(index);
		}
	});



	//执行一个laydate实例
	laydate.render({
			elem: '#sendDate' //指定元素
			,value: new Date()
			,type: 'datetime'
			,min:'date'
		}
	);

	//全选
	form.on('checkbox(all)', function (data) {
		var flag = data.elem.checked;
		if (flag) {
			$(".banji").prop("checked", true);
			$('#all').prop("title","取消全选");
			form.render('checkbox');
		} else {
			$(".banji").prop("checked", false);
			$('#all').prop("title","全选");
			form.render('checkbox');
		}

	});

	//有一个未选中全选取消选中
	form.on('checkbox(one)', function (data) {
		var item = $(".banji");
		for (var i = 0; i < item.length; i++) {
			if (item[i].checked == false) {
				$("#all").prop("checked", false);
				$('#all').prop("title","全选");
				form.render('checkbox');
				break;
			}
		}
		//如果都勾选了  勾上全选
		var  all=item.length;
		for (var i = 0; i < item.length; i++) {
			if (item[i].checked == true) {
				all--;
			}
		}
		if(all==0){
			$("#all").prop("checked", true);
			$('#all').prop("title","取消全选");
			form.render('checkbox');}
	});


	//监听学校选择

	var data1="";
	form.on('select(sc_code)', function(data){
		var sc_code =  data.value;
		$.ajax({
			type: "post",
			url: "/school/listGradeTree?sc_code="+sc_code+"&requestType=",
			success:function(data){

				$('#test12').empty();
				tree.render({
					elem: '#test12',
					data: data,
					showCheckbox: true //是否显示复选框
					,
					id: 'demoId1',
					isJump: true, //是否允许点击节点时弹出新窗口跳转
					click: function (obj) {
						var data = obj.data; //获取当前点击的节点数据
						var last = JSON.stringify(data);
						var obj = JSON.parse(last);
						if (!obj.hasOwnProperty("children")) {
							$('#selecttext').append("<input type='checkbox' name='banji' title=" + obj.title + " lay-skin='primary' value=" + obj.id + " checked>")
							form.render(); //更新全部

						}
					}
				});
			}
		});
	});


	//按钮事件
	util.event('lay-demo', {
		getChecked: function (othis) {
			var checkedData = tree.getChecked('demoId1'); //获取选中节点的数据
			// var data = checkedData; //获取当前点击的节点数据
			var last = JSON.stringify(checkedData);
			var obj = JSON.parse(last);
			$("#selecttext").empty();

			for (var i = 0; i < obj.length; i++) {
				for (var j = 0; j < obj[i].children.length; j++) {

					$('#selecttext').append("<input type='checkbox' name='banji' checked disabled title=" + obj[i].children[j].title + " lay-skin='primary' value=" + obj[i].children[j].id + ">")

				}
				form.render();
			}

		},
		reload: function () {
			//重载实例
			tree.reload('demoId1', {});
			$("#selecttext").empty();


		}
	});

	//<!-----------------------------------------树形组件---------------------------->


	//用户预览信息
	$("#preview").click(function () {

		var sendDate1 = $("#sendDate").val();
		if(sendDate1.length==0||sendDate1==""){
			sendDate1 = new Date().getTime();
		}
		var sendDate = formatTime(sendDate1,"yyyy-MM-dd");

		var content1 = layedit.getContent(index);
		var msgType = $("input[type='radio']:checked").val();

		//获取学校名称
		var schoolName = $("#schoolName").val();
		//获取发送人姓名
		var teName = $("#teName").val();


		var  contentURL;
		if (msgType=="手机短信"){
			contentURL='<style>#LAY_layuipro {background: gray !important;}</style><div style="padding: 25px; line-height: 22px; background-color: gray; color: #fff; font-weight: 300;">'
				+'<script type="text/html" style="display:block;background-color: gray;">'
				+ '【广西翼校通】'
				+content1
				+ '【通知人:'
				+teName
				+ '，发送时间:'
				+sendDate
				+ '】' +
				'</script></div>'
		}

		if (msgType=="微信消息"){
			contentURL='<div style="padding: 25px; line-height: 22px; background-color: gray; color: #fff; font-weight: 300;">'
				+ '您关注的学生有一条校园通知'
				+'<br>'
				+ '学校:'
				+schoolName+'<br>'
				+ '所在班级:'
				+'XXX'+'<br>'
				+ '学生姓名:'
				+'XXX'+'<br>'
				+ '通知人:'
				+teName+'<br>'
				+ '时间:'
				+sendDate+'<br>'
				+ '通知内容:'
				+content1+'<br>'
				+ '</div>'
		}
		layer.open({
			type: 1
			,title:"消息预览("+msgType+")"
			,closeBtn: false
			,area: '400px;'
			,shade: 0.8
			,id: 'LAY_layuipro' //设定一个id，防止重复弹出
			,btn: ['返回']
			,btnAlign: 'c'
			,moveType: 1 //拖拽模式，0或者1
			,content:contentURL
		});
	})





	//提交保存
	$("#sendMessage").click(function () {




		//不为空校验
		var sendDate1 = $("#sendDate").val();
		if(sendDate1.length==0||sendDate1==""){
			sendDate1 = new Date().getTime();
		}
		sendDate1=formatTime(sendDate1,"yyyy-MM-dd hh:mm:ss")

		var clCodeStr = new Array();
		$('input[name="banji"]:checked').each(function(){
			clCodeStr.push($(this).val());
		});

		if (clCodeStr.length == 0) {
			layer.msg('请先选择班级', {icon: 5});
			return false;
		}

		var teName = $("#teName").val();
		var  content = layedit.getContent(index);
		if (content.length == 0) {
			layer.msg('消息内容不能为空', {icon: 5});
			return false;
		}


		var msgType = $("input[type='radio']:checked").val();
		if(msgType=="手机短信"){
			content = ''+content+"【通知人:"+teName+"，发送时间:"+$("#sendDate").val()+"】"
			content = content.replace("&nbsp;","");

			//含有图片
			if(content.indexOf("img") != -1){
				layer.confirm('检测到手机短信内容含有图片信息，如要发送图文信息，请选择信息类型为:【微信消息】', {
					btn: ['坚持发送', '返回重新选择'] //可以无限个按钮
				}, function(index, layero){
							content = content.replace("undefined","");
							//敏感词检测
						// 	$.ajax({
						// 		type: "post",
						// 		url: "/message/checkWord?content="+content,
						// 		async: false,
						// 		dataType: "json",
						// 		success: function(d) {
						// 			layer.confirm("<div style='background-color: aliceblue;margin: -20px;min-height: 110px;margin-top: -34px;padding-top: 55px;overflow-x: auto;'>" +
						// 				"<script type='text/html' style='display:block'>" +
						// 				"【广西翼校通】"+d.content
						// 				+"</script>"
						// 				+ "</div>"
						// 				,{title:'已处理敏感词汇，是否要发送以下信息？'},
						// 				function(index){
						// 					content = content.replace("undefined","");
						// 					var UserObj =
						// 					{
						// 						content:d.content,
						// 						msgType:$("input[type='radio']:checked").val(),
						// 						sendDate:sendDate1,
						// 						clCodeStr:clCodeStr
						// 					}
						// 					$.ajax({
						// 						type: "post",
						// 						url: "/message/send?clCodeStr="+clCodeStr,
						// 						async: false,
						// 						data: UserObj,
						// 						dataType: "json",
						// 						success: function(d) {
						// 							if(d.code == 200) {
						// 								msg = "发送成功！";
						// 								layer.msg(msg);
						// 								setTimeout(function(){$("#auf")[0].reset();},1000);
						// 							} else {
						// 								msg = d.msg;
						// 								layer.msg(msg,{icon: 5});
						// 							}
                        //
						// 						}
						// 					});
                        //
						// 				});
						// 		}
						// });

					content = content.replace("undefined","");
					var UserObj =
					{
						content:d.content,
						msgType:$("input[type='radio']:checked").val(),
						sendDate:sendDate1,
						clCodeStr:clCodeStr
					}
					$.ajax({
						type: "post",
						url: "/message/send?clCodeStr="+clCodeStr,
						async: false,
						data: UserObj,
						dataType: "json",
						success: function(d) {
							if(d.code == 200) {
								msg = "发送成功！";
								layer.msg(msg);
								setTimeout(function(){$("#auf")[0].reset();},1000);
							} else {
								msg = d.msg;
								layer.msg(msg,{icon: 5});
							}

						}
					});



				}, function(index){
					//按钮【按钮二】的回调

				});

				return false;
			}
		}



		content = content.replace("undefined","");


		//如果是手机短信
		if($("input[type='radio']:checked").val()=="手机短信"){
				//不含图片将去掉所有的html标签
				content = content.replace(/<[^>]+>/g,"");
			    content = content.replace("&nbsp;","");
		}
		if(content.indexOf("img") != -1){}else
		{
			//没有图片信息
			content = content.replace(/<[^>]+>/g,"");
			content = content.replace("&nbsp;","");
		}

		//敏感词汇检测
        //
		// $.ajax({
		// 	type: "post",
		// 	url: "/message/checkWord?content="+content,
		// 	async: false,
		// 	dataType: "json",
		// 	success: function(d) {
		// 		//处理过的信息预览，提示用户是否要发送
		// 		var pptext = "";
		// 		//手机短信确认发送
		// 		if(msgType=="手机短信") {
		// 			pptext = "<div style='background-color: aliceblue;margin: -20px;min-height: 110px;margin-top: -34px;padding-top: 55px;overflow-x: auto;'>" +
		// 				"<script type='text/html' style='display:block'>" +
		// 				"【广西翼校通】" + d.content
		// 				+ "</script>"
		// 				+ "</div>";
		// 		}else {
		// 			pptext = d.content;
		// 		}
		// 		layer.confirm(pptext,{title: '已处理敏感词汇，是否要发送以下信息？'},
		// 			function (index) {
		// 				//确定发送
		// 				var UserObj =
		// 				{
		// 					content:d.content,
		// 					msgType:$("input[type='radio']:checked").val(),
		// 					sendDate:sendDate1,
		// 					clCodeStr:clCodeStr
		// 				}
		// 				$.ajax({
		// 					type: "post",
		// 					url: "/message/send?clCodeStr="+clCodeStr,
		// 					async: false,
		// 					data: UserObj,
		// 					dataType: "json",
		// 					success: function(d) {
        //
		// 						if(d.code == 200) {
		// 							msg = "发送成功！";
		// 							layer.msg(msg);
		// 							setTimeout(function(){$("#auf")[0].reset();},1000);
		// 						} else {
		// 							msg = d.msg;
		// 							layer.msg(msg,{icon: 5});
		// 						}
        //
		// 					}
		// 				});
		// 			});
        //
		// 	}
		// });


		//确定发送
		var UserObj =
		{
			content:content,
			msgType:$("input[type='radio']:checked").val(),
			sendDate:sendDate1,
			clCodeStr:clCodeStr
		}
		$.ajax({
			type: "post",
			url: "/message/send?clCodeStr="+clCodeStr,
			async: false,
			data: UserObj,
			dataType: "json",
			success: function(d) {

				if(d.code == 200) {
					msg = "发送成功！";
					layer.msg(msg);
					setTimeout(function(){$("#auf")[0].reset();},1000);
				} else {
					msg = d.msg;
					layer.msg(msg,{icon: 5});
				}

			}
		});


		return false;
	})

})
//格式化时间
function formatTime(datetime, fmt) {
	if (datetime == null || datetime == 0) {
		return "";
	}
	if (parseInt(datetime) == datetime) {
		if (datetime.length == 10) {
			datetime = parseInt(datetime) * 1000;
		} else if (datetime.length == 13) {
			datetime = parseInt(datetime);
		}
	}
	datetime = new Date(datetime);
	var o = {
		"M+" : datetime.getMonth() + 1, //月份
		"d+" : datetime.getDate(), //日
		"h+" : datetime.getHours(), //小时
		"m+" : datetime.getMinutes(), //分
		"s+" : datetime.getSeconds(), //秒
		"q+" : Math.floor((datetime.getMonth() + 3) / 3), //季度
		"S" : datetime.getMilliseconds()
		//毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (datetime.getFullYear() + "")
			.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1,
				(RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k])
					.substr(("" + o[k]).length)));
	return fmt;
}

