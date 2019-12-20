layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage','table','laydate'],function(){
	var form = layui.form,table = layui.table;
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
            laydate = layui.laydate,
		$ = layui.jquery;

    //时间选择查询
    var startDate, endDate;
    var url="/leave/totals";
    laydate.render({
        elem: '#test13'
        ,type: 'month'
        ,range: true
        ,format: 'yyyy-MM'
        ,done: function(value, date, endDate){
            startDate = formatTime(new Date(date.year, date.month-1, 1, 0, 0, 0),"yyyy-MM-dd hh:mm:ss");
            endDate =  formatTime(new Date(endDate.year, endDate.month, 1, 0, 0, 0),"yyyy-MM-dd hh:mm:ss");

            // alert(startDate);
            // alert(endDate);

            url="/leave/totals?startDate="+startDate+"&endDate="+endDate;

            test(startDate,endDate);
            table.render({
                id:'bj_total',
                elem: '#bj_total'
                ,url: url
                ,method:'GET'
                ,parseData: function(res){ //res 即为原始返回的数据
                    return {
                        "code": "0", //解析接口状态
                        "data": res[0].values //解析数据列表
                    };
                }
                ,cellMinWidth:10
                ,limit:10//每页默认数
                ,limits:[10,20,30,40]
                ,cols: [[ //表头
                    {type:'checkbox'}
                    ,{field:'autoAdd', width:70, title: '序号',templet:'#autoAdd',unresize:true}
                    ,{field:'name', title: '班级名称'}
                    ,{field:'value', title: '请假次数',sort: true}
                    ,{title: '操作',toolbar: '#barEdit'}
                ]]

            });

        }
    });

    //页面初始化数据展示
    test(startDate,endDate);
function test(startDate,endDate) {

    var subStr = "全部记录";
    if(startDate){
        startDate=formatTime(startDate,"yyyy-MM-dd");
        endDate=formatTime(endDate,"yyyy-MM-dd");
        subStr = startDate+"至"+ endDate;
    }
    $("#tableTitle").text("班级请假次数情况统计表格");
    $("#tablesubText").text(subStr);



    //获取数据
    var categories,values,tabledData;
    $.ajax({
        type: "GET",
        async : false,   //改这里
        url: url,
        success:function(data){
            categories = data[0].categories;
            values = data[0].values;
            tabledData = data[0].values;
        }
    });

    // 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main'));
	myChart.showLoading({
		text: '正在努力加载中...'
	});

    //饼图
    option = {
        title: {
            text: '班级请假次数情况统计饼图',
             subtext: subStr,
            left: 'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "请假次数:<br/>{b} : {c}次"
        },
        legend: {
            // orient: 'vertical',
            // top: 'middle',
            bottom: 10,
            left: 'center',
            data: categories
        },
        series : [
            {
                type: 'pie',
                radius : '65%',
                center: ['50%', '50%'],
                selectedMode: 'single',
                data:values,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    myChart.hideLoading();

    //柱状图
    var myChart1 = echarts.init(document.getElementById('main1'));

    var option1 = {
        title: {
            text: '班级请假次数情况统计柱状图',
            subtext: subStr,
            left: 'center'
        },
        tooltip: {
            show: true,
            formatter: "请假次数:<br/>{b} : {c}次"
        },
        legend: {
            // data:['请假次数']
            bottom: 10,
            left: 'center',
            data:['请假次数']
        },

        xAxis : [
            {
                type : 'category',
                data : categories
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [

            {
                barWidth: 40,
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                            offset: 0,
                            color: "#1268f3" // 0% 处的颜色
                        }, {
                            offset: 0.6,
                            color: "#08a4fa" // 60% 处的颜色
                        }, {
                            offset: 1,
                            color: "#01ccfe" // 100% 处的颜色
                        }], false)
                    }
                },
                name:"请假次数",
                type:"bar",
                data:values

            },//折线图
            {
                name:"销量",
                type:"line",
                data:values,
                //绘制平均线
                markLine : {
                    data : [
                        {
                            type : 'average',
                            name: '平均值'
                        }
                    ]
                },
                //绘制最高最低点
                markPoint : {
                    data : [
                        {
                            type : 'max',
                            name: '最大值'
                        },
                        {
                            type : 'min',
                            name: '最小值'
                        }
                    ]
                }
            }
        ]
    };

    // 为echarts对象加载数据
    myChart1.setOption(option1);
    



//
//
// 	//圆柱图上添加点击事件
// 	myChart.on("click", pieConsole);
// 	function pieConsole(param) {
// 		alert(option.series[param.seriesIndex].data[param.dataIndex].value);
// 		alert(option.series[param.seriesIndex].data[param.dataIndex].name);
// 		alert(option.series[param.seriesIndex].data[param.dataIndex].code);
//
//
// 		//刷新页面
// 		// location.reload();
// 		// window.location.reload();
// 	}
}


	// myChart.showLoading();    //数据加载完之前先显示一段简单的loading动画



	//数据表格

		table.render({
			id:'bj_total',
		    elem: '#bj_total'
			,url: url
			,method:'GET'
			,parseData: function(res){ //res 即为原始返回的数据
				return {
					"code": "0", //解析接口状态
					"data": res[0].values //解析数据列表
				};
			}
		    ,cellMinWidth:10
		    ,limit:10//每页默认数
		    ,limits:[10,20,30,40]
		    ,cols: [[ //表头
              {type:'checkbox'}
				,{field:'autoAdd', width:70, title: '序号',templet:'#autoAdd',unresize:true}
              ,{field:'name', title: '班级名称'}
				,{field:'value', title: '请假次数',sort: true}
              ,{title: '操作',toolbar: '#barEdit'}
		    ]]

		  });





		//监听工具条
		  table.on('tool(bj_total)', function(obj){
		    var data = obj.data;
		    if(obj.event === 'look'){
				var arr = [];
                for(var i = 0; i < data.dataList.length;i++ ){
					arr.push(data.dataList[i].stuNo)
				}
                var docW = window.screen.width;
                if(docW < 750){
                    layerW = '90%';
                    layerH = '60%';
                }else{
                    layerW = '50%';
                    layerH = '50%';
                }

				layer.open({
					type : 2,
					title : data.name+"请假情况",
                    area:[layerW, layerH],
					content : "/page/totals/stuLeaveTotals" //这里content是一个普通的String
					,success:function (layero,index) {
						data.dataList.code=data.code;;
                        framedata = data.dataList;
                        for(var i = 0; i < framedata.length;i++){

                            data.stuNo=framedata[i].stuNo;
                            data.stuName=framedata[i].stuName;
                            data.sex="女";
                            if(framedata[i].sex=="1"){
                                data.sex = "男"
                            }

                            data.reason=framedata[i].reason;
                            data.startDate=framedata[i].startDate;
                            data.endDate=framedata[i].endDate;

                            var $tr = $("<tr>"
                                + "<td>"+data.stuNo+"</td>"
                                + "<td>"+data.stuName+"</td>"
                                + "<td>"+data.sex+"</td>"
                                + "<td>"+data.reason+"</td>"
                                + "<td>"+formatTime(data.startDate,"yyyy-MM-dd hh:mm:ss")+"至"+formatTime(data.endDate,"yyyy-MM-dd hh:mm:ss")+"</td>"
                                + "</tr>");
                            // var $table = $('iframe tbody');
                            var body = layer.getChildFrame('body', index);
                            var $table = body.find("#classList tbody");
                            $table.append($tr);




                        }



                    }
				})


		    }

		  });







	
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


