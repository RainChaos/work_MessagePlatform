var $;
layui.config({
	base : "/static/js/"
}).use(['form','layer','jquery'],function(){
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage;
	$ = layui.jquery;
	form.on("submit(editRoleMenu)",function(data){

		//获取班级节点数据
		var treeObj = $.fn.zTree.getZTreeObj("xtree1");
		var list = treeObj.getCheckedNodes(true);
		//菜单id
		var mStr="";
		for(var i=0;i<list.length;i++){
			if(list[i].isParent!=true){
				mStr+=list[i].id+",";
			}
		}
		//去除字符串末尾的‘,’【班级的数据，格式：12,13,36,58】
		mStr=mStr.substring(0,mStr.length-1)

		//获取班主任节点数据
		var treeHeaderObj = $.fn.zTree.getZTreeObj("headerXtree");
		var headerlist = treeHeaderObj.getCheckedNodes(true);
		var headerStr="";
		for(var i=0;i<headerlist.length;i++){
			if(headerlist[i].isParent!=true){
				headerStr+=headerlist[i].id+",";
			}
		}
		//去除字符串末尾的‘,’【班主任的数据，格式：12,13,36】
		headerStr=headerStr.substring(0,headerStr.length-1)

		//遍历两个字符串中是否含有相等值，如果有，则向mStr对应的值后加@true，否则加@false
		var mlist = mStr.split(",");
		var headList = headerStr.split(",");

		if(mlist!=""){
			for(var k=0;k<headList.length;k++){
				for(var h=0;h<mlist.length;h++){
					if (headList[k]==mlist[h]){
						mlist[h]+="@true";
					}
				}
			}
		}





		//弹出loading
		var index = layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
		var teNo =$("#teNo").val();
		$.ajax({
			type: "POST",
			async:false,
			url: "/teacher/setTeacherClass?clCodeList="+mlist+"&teNo="+teNo,
			success:function(data){
				layer.msg("班级设置成功！",{icon: 1});
			}
		});

		setTimeout(function(){
			layer.close(index);
			layer.closeAll("iframe");
			// 刷新父页面
			// parent.location.reload();
		},2000);
		return false;
	 })

})


//班级设置
var roleId=$("#roleId").val();
var scCode=$("#scCode").val();

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

	//获取该老师所在的学校的结构树形图展示给用户
	loadMenuTree:function(){
		$.ajax({
			type: "post",
			url: '/school/listGradeTree?sc_code='+scCode+"&teNo="+$("#teNo").val()+"&requestType=",
			// data:{rid:roleId},
			dataType:"json",
			success:function(data){
				$.fn.zTree.init($("#xtree1"), menu.setting, data);

			}
		})
	},
};
var headmenu = {
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

	//获取该老师所在的学校的结构树形图展示给用户
	loadMenuTree1:function(type){
		$.ajax({
			type: "post",
			url:'/school/listGradeTree?sc_code='+scCode+"&teNo="+$("#teNo").val()+"&requestType=classHeader",
			// data:{rid:roleId},
			dataType:"json",
			success:function(data){
				$.fn.zTree.init($("#headerXtree"), headmenu.setting, data);

			}
		})
	},
};


function checkNode(e) {
	var zTree = $.fn.zTree.getZTreeObj("xtree1"),
		type = e.data.type,
		nodes = zTree.getSelectedNodes();
	// console.log(type.indexOf("All"));
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

function checkNode1(e) {
	var zTree = $.fn.zTree.getZTreeObj("headerXtree"),
		type = e.data.type,
		nodes = zTree.getSelectedNodes();
	// console.log(type.indexOf("All"));
	if (type.indexOf("All") < 0 && nodes.length == 0) {
		alert("请先选择一个节点");
	}
	if (type == "checkAllTrue") {
		zTree.checkAllNodes(true);
	} else if (type == "checkAllFalse") {
		zTree.checkAllNodes(false);
	}
}
$("#checkHeaderAllTrue").bind("click", { type: "checkAllTrue" }, checkNode1);
$("#checkHeaderAllFalse").bind("click", { type: "checkAllFalse" }, checkNode1);


//初始化树

$().ready(function(data){
	menu.loadMenuTree();
	headmenu.loadMenuTree1();
});

