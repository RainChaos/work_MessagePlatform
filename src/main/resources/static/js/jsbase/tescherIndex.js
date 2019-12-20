var $,tab,skyconsWeather;
layui.config({
	base : "static/js/jsbase/"
}).use(['bodyTab','form','element','layer','jquery'],function(){
	var form = layui.form,
		layer = layui.layer,
		element = layui.element;
	$ = layui.jquery;
	tab = layui.bodyTab({
		openTabNum : "50",  //最大可打开窗口数量
		// url : "/frame/getUserModuleTree" //获取菜单json地址
		url : "../../static/json/teacher.json" //获取菜单json地址

	});



	//退出
	$(".signOut").click(function(){
		window.sessionStorage.removeItem("menu");
		menu = [];
		window.sessionStorage.removeItem("curmenu");
	})

	//隐藏左侧导航
	$(".hideMenu").click(function(){
		$(".layui-layout-admin").toggleClass("showMenu");
		//渲染顶部窗口
		tab.tabMove();
	})

	//渲染左侧菜单
	tab.render();

	//手机设备的简单适配
	var treeMobile = $('.site-tree-mobile'),
		shadeMobile = $('.site-mobile-shade')

	treeMobile.on('click', function(){
		$('body').addClass('site-mobile');
	});

	shadeMobile.on('click', function(){
		$('body').removeClass('site-mobile');
	});

	// 添加新窗口
	$("body").on("click",".layui-nav .layui-nav-item a",function(){
		//如果不存在子级
		if($(this).siblings().length == 0){
			addTab($(this));
			$('body').removeClass('site-mobile');  //移动端点击菜单关闭菜单层
		}
		$(this).parent("li").siblings().removeClass("layui-nav-itemed");
	})








	//资料修改
	$("#personpc").click(function(){
		layer.open({
			type: 2,
			title:"密码修改",
			area: ['445px', '300px'],
			content:"/page/changeTeacherPwd"
		})
	})

	//资料修改
	$("#personmobile").click(function(){
		layer.open({
			type: 2,
			title:"密码修改",
			area: '100%',
			content:"/page/changeTeacherPwd"
		})
	})
	//资料修改
	$("#ziliaoPC").click(function(){
		layer.open({
			type: 2,
			title:"资料修改",
			area: ['430px', '526px'],
			content:"/page/teacher/update"
		})
	})




//微信授权
	$("#WxLogin").click(function(){

		var wxText = $('#wxText').text();
		if(wxText=="已授权"){
			//用户已经授权
			layer.msg("你已授权微信登录了");
			return ;
		}
		layer.confirm("您确定授权微信登录该平台吗", {
			btn: ['确定','取消'] //按钮
		}, function(){
			$.ajax({
				type: "post",
				url: "/teacher/WxLogin?flag=1&wxNo="+$('#UserOpenId').val(),
				dataType:"json",
				success:function(d){
					if(d.code==200){
						layer.msg("授权成功", {icon: 1});
					}else{
						layer.msg('授权失败,请用微信客户端打开授权', {icon: 5});
					}
				}
			});
		}, function(){
		});
	})

	//微信授权
	$("#WxLoginmobile").click(function(){
		var wxText = $('#wxText').text();
		if(wxText=="已授权"){
			//用户已经授权
			layer.msg("你已授权微信登录了");
			return ;
		}
		layer.confirm('您确定授权微信登录该平台吗？', {
			btn: ['确定','取消'] //按钮
		}, function(){
			$.ajax({
				type: "post",
				url: "/teacher/WxLogin?flag=1&wxNo="+$('#UserOpenId').val(),
				dataType:"json",
				success:function(d){
					if(d.code==200){
						layer.msg("授权成功", {icon: 1});
					}else{
						layer.msg('请用微信客户端打开授权', {icon: 5});
					}
				}
			});
		}, function(){
		});
	})





	//刷新后还原打开的窗口
	if(window.sessionStorage.getItem("menu") != null){
		menu = JSON.parse(window.sessionStorage.getItem("menu"));
		curmenu = window.sessionStorage.getItem("curmenu");
		var openTitle = '';
		for(var i=0;i<menu.length;i++){
			openTitle = '';
			if(menu[i].icon){
				if(menu[i].icon.split("-")[0] == 'icon'){
					openTitle += '<i class="iconfont '+menu[i].icon+'"></i>';
				}else{
					openTitle += '<i class="layui-icon">'+menu[i].icon+'</i>';
				}
			}
			openTitle += '<cite>'+menu[i].title+'</cite>';
			openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="'+menu[i].layId+'">&#x1006;</i>';
			element.tabAdd("bodyTab",{
				title : openTitle,
				content :"<iframe src='"+menu[i].href+"' data-id='"+menu[i].layId+"'></frame>",
				id : menu[i].layId
			})
			//定位到刷新前的窗口
			if(curmenu != "undefined"){
				if(curmenu == '' || curmenu == "null"){  //定位到后台首页
					element.tabChange("bodyTab",'');
				}else if(JSON.parse(curmenu).title == menu[i].title){  //定位到刷新前的页面
					element.tabChange("bodyTab",menu[i].layId);
				}
			}else{
				element.tabChange("bodyTab",menu[menu.length-1].layId);
			}
		}
		//渲染顶部窗口
		tab.tabMove();
	}

	//刷新当前
	$(".refresh").on("click",function(){  //此处添加禁止连续点击刷新一是为了降低服务器压力，另外一个就是为了防止超快点击造成chrome本身的一些js文件的报错(不过貌似这个问题还是存在，不过概率小了很多)
		if($(this).hasClass("refreshThis")){
			$(this).removeClass("refreshThis");
			$(".clildFrame .layui-tab-item.layui-show").find("iframe")[0].contentWindow.location.reload(true);
			setTimeout(function(){
				$(".refresh").addClass("refreshThis");
			},2000)
		}else{
			layer.msg("您点击的速度超过了服务器的响应速度，还是等两秒再刷新吧！");
		}
	})

	//关闭其他
	$(".closePageOther").on("click",function(){
		if($("#top_tabs li").length>2 && $("#top_tabs li.layui-this cite").text()!="后台首页"){
			var menu = JSON.parse(window.sessionStorage.getItem("menu"));
			$("#top_tabs li").each(function(){
				if($(this).attr("lay-id") != '' && !$(this).hasClass("layui-this")){
					element.tabDelete("bodyTab",$(this).attr("lay-id")).init();
					//此处将当前窗口重新获取放入session，避免一个个删除来回循环造成的不必要工作量
					for(var i=0;i<menu.length;i++){
						if($("#top_tabs li.layui-this cite").text() == menu[i].title){
							menu.splice(0,menu.length,menu[i]);
							window.sessionStorage.setItem("menu",JSON.stringify(menu));
						}
					}
				}
			})
		}else if($("#top_tabs li.layui-this cite").text()=="后台首页" && $("#top_tabs li").length>1){
			$("#top_tabs li").each(function(){
				if($(this).attr("lay-id") != '' && !$(this).hasClass("layui-this")){
					element.tabDelete("bodyTab",$(this).attr("lay-id")).init();
					window.sessionStorage.removeItem("menu");
					menu = [];
					window.sessionStorage.removeItem("curmenu");
				}
			})
		}else{
			layer.msg("没有可以关闭的窗口了!");
		}
		//渲染顶部窗口
		tab.tabMove();
	})
	//关闭全部
	$(".closePageAll").on("click",function(){
		if($("#top_tabs li").length > 1){
			$("#top_tabs li").each(function(){
				if($(this).attr("lay-id") != ''){
					element.tabDelete("bodyTab",$(this).attr("lay-id")).init();
					window.sessionStorage.removeItem("menu");
					menu = [];
					window.sessionStorage.removeItem("curmenu");
				}
			})
		}else{
			layer.msg("没有可以关闭的窗口了!");
		}
		//渲染顶部窗口
		tab.tabMove();
	})

})

//打开新窗口
function addTab(_this){
	tab.tabAdd(_this);
}
