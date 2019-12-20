layui.config({
    base : "js/"
}).use(['form','layer','jquery','laypage','table','laydate','tree'],function(){
    var form = layui.form,table = layui.table;
    layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        laydate = layui.laydate,
        tree = layui.tree,
        $ = layui.$;
    //查询条件
    var flag = 1 ;
    //获取当前时间
    var day2 = new Date();
    day2.setTime(day2.getTime());
    var day1 = day2.getFullYear()-1+"-" + (day2.getMonth()+1) + "-" + day2.getDate();
    var endDate = formatTime(day2,"yyyy-MM-dd hh:mm:ss");
    var startDate = formatTime(day1,"yyyy-MM-dd hh:mm:ss");

    var substartDate,suendDate;

    clCode=$('#cl_code').val();
    substartDate = $('#startDate').val();
    suendDate = $('#endDate').val();
    flag = $('#flag').val();

    var url="/page/getAscPieCountGroupByState?startDate="+startDate+"&endDate="+endDate+"&clCode="+clCode;
    var url2="/page/getAscLineCountGroupByState?startDate="+startDate+"&endDate="+endDate+"&clCode="+clCode;
    //时间选择查询
    laydate.render({
        elem: '#test13'
        ,type: 'month'
        ,range: true
        ,format: 'yyyy-MM'
        ,done: function(value, date, endDate){

            startDate = formatTime(new Date(date.year, date.month-1, 1, 0, 0, 0),"yyyy-MM-dd hh:mm:ss");
            endDate =  formatTime(new Date(endDate.year, endDate.month, 1, 0, 0, 0),"yyyy-MM-dd hh:mm:ss");
            clCode=$('#cl_code').val();
            url="/page/getAscPieCountGroupByState?startDate="+startDate+"&endDate="+endDate+"&clCode="+clCode;
            url2="/page/getAscLineCountGroupByState?startDate="+startDate+"&endDate="+endDate+"&clCode="+clCode;

            flag = $('#flag').val();
            substartDate = startDate;
            suendDate =endDate;

            //监听指定开关
            pie(substartDate,suendDate,flag,url);
            line(substartDate,suendDate,flag,url2);
            $('#cl_code').val(clCode);
            $('#startDate').val(startDate);
            $('#endDate').val(endDate);
            $('#flag').val(flag);

        }
    });

    //页面初始化数据展示
    pie(startDate,endDate,flag,url);
    line(startDate,endDate,flag,url2);
    $('#cl_code').val(clCode);
    $('#startDate').val(startDate);
    $('#endDate').val(endDate);
    $('#flag').val(flag);

    //监听指定开关
    form.on('switch(switchPie)', function(data){
        flag = this.checked ? 1 : 0;
        substartDate = $('#startDate').val();
        suendDate = $('#endDate').val();
        pie(substartDate,suendDate,flag,url);
        $('#flag').val(flag);
    });

    //监听指定开关
    form.on('switch(switchLine)', function(data){
        flag = this.checked ? 1 :0;
        substartDate = $('#startDate').val();
        suendDate = $('#endDate').val();
        line(substartDate,suendDate,flag,url2);
        $('#flag').val(flag);
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

//饼图方法
function pie(startDate,endDate,flag,url) {
    var subStr = "全部记录";

    if(startDate){
        startDate=formatTime(startDate,"yyyy-MM-dd");
        endDate=formatTime(endDate,"yyyy-MM-dd");
        subStr = startDate+"至"+ endDate;
    }
    if(flag){
        titleText="校门进入次数统计";
    } else {
        titleText="校门出入次数统计";
    }
    $("#tablesubText").text(subStr);

    //获取数据
    var categories,values;
    $.ajax({
        type: "POST",
        async : false,   //改这里
        url: url,
        success:function(data){
            categories = data.content.itemNameData;
            var dataStr;
            if(flag){
                dataStr=JSON.stringify(data.content.inData)
            }else {
                dataStr=JSON.stringify(data.content.outData);
            }
            values = dataStr;
            values =  JSON.parse(dataStr);//转换为json对象

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
            text: titleText,
            subtext: subStr,
            left: 'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{b} : {c}次"
        },
        legend: {
            // orient: 'vertical',
            // top: 'middle',
            bottom: 10,
            left: 'center',
            data: categories,
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
}

function line(startDate,endDate,flag,url) {
    var subStr = "全部记录";
    if(startDate){
        startDate=formatTime(startDate,"yyyy-MM-dd");
        endDate=formatTime(endDate,"yyyy-MM-dd");
        subStr = startDate+"至"+ endDate;
    }
    if(flag){
        titleText="每月校门进入次数统计";
    } else {
        titleText="每月校门出入次数统计";
    }

    $("#tablesubText").text(subStr);
    //获取数据
    var categories1,values1,time1;
    $.ajax({
        type: "POST",
        async : false,   //改这里
        url: url,
        success:function(data){
            categories1 = data.content.itemNameData;
            time1 = data.content.timeData;
            var dataStr;
            if(flag){
                dataStr=JSON.stringify(data.content.inData)
            }else {
                dataStr=JSON.stringify(data.content.outData);
            }
            var data =  JSON.parse(dataStr);//转换为json对象

            for(var i = 0 ; i <  data.length;i++ ){
                data[i].name=data[i].name
                data[i].value=data[i].value
                data[i].type='line'
                data[i].stack='总量'
            }
            var dataStr=JSON.stringify(data);
            values1 = dataStr.replace(/value/g, "data");
            var jsonObj =  JSON.parse(values1);//转换为json对象
            var array=new Array();
            for(var i=0;i<jsonObj.length;i++){
                array.push(jsonObj[i]);
            }
            values1 =  array;
        }
    });

    //折线图
    var myChart1 = echarts.init(document.getElementById('main1'));
    var option1 = {
        title: {
            text: titleText,
            subtext: subStr,
            left: 'center'
        },
        tooltip : {
            trigger: 'axis',
        },
        legend: {
            bottom: 10,
            data:categories1
        },

        toolbox: {
            show : true,
            feature : {
                mark : {show: true},
            }
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                boundaryGap : false,
                data:time1
            }
        ],
        yAxis : [
            {
                name: '次数',
                type : 'value'
            }
        ],
        series :values1
    };
    // 为echarts对象加载数据
    myChart1.setOption(option1);
}



