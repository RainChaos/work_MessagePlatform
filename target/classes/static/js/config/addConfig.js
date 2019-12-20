var $;
var $form;
var form;
layui.config({
	base : "js/"
}).use(['form','layer','jquery','laydate'],function(){
	var layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,laydate = layui.laydate;
		$ = layui.jquery;
		form = layui.form;
		laydate.render({
			elem: '#test5'
			,type: 'datetime'
		});
		//监听学校选择
		form.on('select(cfName)', function(data){
			var typeValue = $("#cfName").find("option:selected").attr("title");
			var Str = "";
			if (typeValue=="Select"){
				$.ajax({
					type: "post",
					url: "/configDefine/selectValueList?id="+data.value,
					success:function(d){
						$("#valueDiv").empty();
						Str = '<select id="value" name="value"></select>';
						$("#valueDiv").append(Str);
						for ( i in d.content){
							Str="<option value="+"'"+d.content[i].value+"'"+">"+d.content[i].value+"</option>";
							$("#value").append(Str);
						}
						form.render();
					}
				});
				}

			if (typeValue=="Date"){
				Str='<input id="value"  type="text" class="layui-input">';
				$("#valueDiv").empty();
				$("#valueDiv").append(Str);
				laydate.render({
					elem: '#value'       //指定元素
					,type: 'datetime'
				});
				form.render();
			}
			if (typeValue=="Boolean"){
				Str='<input type="radio" id="value" name="value" value="true" title="true" checked=""><input type="radio" name="value" value="false" title="false">';
				$("#valueDiv").empty();
				$("#valueDiv").append(Str);
				form.render();
			}
			});

		$("#addConfig").click(function() {
			var text = $("#value").val();
			var scCode =$("#scCode option:selected").val();
			var cfDeId = $("#cfName option:selected").val();
			var cfCode = $("#cfName option:selected").attr("id");
			if (scCode==""){
				layer.msg("请选择学校",{icon:5})
				return;
			}
			if (cfDeId==""){
				layer.msg("请选择配置项",{icon:5})
				return;
			}

			if (text==null){
				layer.msg("值不能为空",{icon:5})
				return;
				}

			//弹出loading
			var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
			var msg;
 		$.ajax({
    		type: "post",
            url: "/config/add?value="+text
			+"&cfDeId="+cfDeId
			+"&scCode="+scCode
			+"&cfCode="+cfCode,
			dataType:"json",
			success:function(d){
				if(d.code==200){
		        	msg="添加成功！";
					layer.msg(msg,{icon: 1});
				}else{
					layer.msg(d.msg,{icon: 5});
				}
			}
        });
		setTimeout(function(){
			layer.close(index);
		},2000);
 		return false;
 	})
}
)
