var $;
layui.config({
	base : "/static/js/"
}).use(['form','layer','jquery'],function(){
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage;
	$ = layui.jquery;

	form.on("submit(editRoleMenu)",function(data){
		var treeObj = $.fn.zTree.getZTreeObj("xtree1");
		var list = treeObj.getCheckedNodes(true);
		//菜单id
		var mStr="";
		for(var i=0;i<list.length;i++){
			mStr+=list[i].id+",";
		}
		//去除字符串末尾的‘,’
		mStr=mStr.substring(0,mStr.length-1)
		var m=$("#m");
		//将权限字符串写进隐藏域
		m.val(mStr)

		//弹出loading
		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
		var rid =$("#roleId").val();
		$.ajax({
			type: "POST",
			async:false,
			url: "/frame/setRoleMoudle?mids="+mStr+"&rid="+rid,
			success:function(data){
				layer.msg("操作成功！",{icon: 1});

			}
		});

		setTimeout(function(){
			layer.close(index);
			layer.closeAll("iframe");
		},2000);
		return false;
	})
})
var roleId=$("#roleId").val();
var menu = {
	setting: {
		view : {
			showIcon : false,
		},

		data:{
			simpleData: {
				enable: true,
				idKey: "id",
				pIdKey: "parentId",
			},
			key:{
				name:"name",
			}
		},
		check:{
			enable:true
		}
	},
	//菜单数据初始化
	//获取所有的菜单信息以树形形式展示给用户选择
	loadMenuTree:function(){
		$.ajax({
			type: "post",
			url: '/frame/getModuleTree1?uid='+roleId,
			// data:{rid:roleId},
			dataType:"json",
			success:function(data){
				$.fn.zTree.init($("#xtree1"), menu.setting, data);

			}
		})
	}
};

$().ready(function(data){
	menu.loadMenuTree();
});
function checkNode(e) {
	var zTree = $.fn.zTree.getZTreeObj("xtree1"),
		type = e.data.type,
		nodes = zTree.getSelectedNodes();
	if (type.indexOf("All") < 0 && nodes.length == 0) {
		alert("请先选择一个节点");
	}
	if (type == "checkAllTrue") {
		zTree.checkAllNodes(true);
	} else if (type == "checkAllFalse") {
		zTree.checkAllNodes(false);
	}
}
$("#checkAllTrue").bind("click", { type: "checkAllTrue" }, checkNode);
$("#checkAllFalse").bind("click", { type: "checkAllFalse" }, checkNode);
