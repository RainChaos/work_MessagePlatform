var $;
var $form;
var form;
layui.config({
	base: "js/"
}).use(['form', 'layer', 'jquery', 'util', 'tree','laydate'], function() {
		var layer = parent.layer === undefined ? layui.layer : parent.layer,
			laypage = layui.laypage,
			tree = layui.tree,
			util = layui.util,
			laydate = layui.laydate;
		$ = layui.jquery;
		form = layui.form;

	//执行一个laydate实例
	laydate.render({
		elem: '#sendDate' //指定元素
		,type: 'datetime'
		,min:'date'
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
		//提交保存
		$("#sendMessage").click(function () {

			// form.render();
			var clCodeStr = new Array();
			$("input[name='banji']:checkbox").each(function () {
				if ($(this).attr("checked")) {
					clCodeStr.push($(this).attr('value'));
				}
			});
			if (clCodeStr.length == 0) {
				layer.msg('请先选择班级', {icon: 5});
				return false;
			}
			var UserObj =
			{
				content:$("#content").val(),
				msgType:$("input[type='radio']:checked").val(),
				sendDate:$("#sendDate").val(),
				clCodeStr:clCodeStr
			}
				$.ajax({
					type: "post",
					url: "/message/send?clCodeStr="+clCodeStr,
					async: false,
					data: UserObj,
					dataType: "json",
					success: function(d) {
						if(d.code == 2000) {
							msg = "发送成功！";
							layer.msg(msg);
							setTimeout(function(){$("#auf")[0].reset();},1000);
						} else {
							msg = "发送失败！";
							layer.msg(msg);
						}

					}
				});
		})

})

