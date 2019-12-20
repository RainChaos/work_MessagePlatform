layui
    .config({
        base : "js/"
    })
    .use(
        [ 'form', 'layer', 'jquery', 'table', 'laydate' ],
        function() {
            var form = layui.form, table = layui.table, layer = parent.layer === undefined ? layui.layer
                : parent.layer, laydate = layui.laydate
            $ = layui.jquery,
                nowTime = new Date().valueOf(),
                max = null,
                active = {
                    search : function() {
                        var sc_code = $('#sc_code'),gr_code = $('#gr_code');
                        //执行重载
                        table
                            .reload(
                                'classList',
                                {
                                    page : {
                                        curr : 1
                                        //重新从第 1 页开始
                                    },
                                    where : {
                                        //key: {
                                        sc_code : sc_code
                                            .val(),
                                        gr_code : gr_code
                                            .val()

                                    }
                                });
                    }
                };

            var data = [{"stuNo": "10001","stuName": "杜甫","reason": "xianxin@layui.com","startDate": "男","endDate": "浙江杭州"}];

            var data = [];
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
                var $table = $('table tbody');
                $table.append($tr);




            }




            //展示已知数据
            // table.render({
            //     elem: '#classList'
            //     ,cols: [[ //标题栏
            //         {field: 'stuNo', title: 'ID', width: 80, sort: true}
            //         ,{field: 'stuName', title: '学生姓名', width: 120}
            //         ,{field: 'reason', title: '请假理由', minWidth: 150}
            //         ,{field: 'startDate', title: '请假时间（开始）', minWidth: 160}
            //         ,{field: 'endDate', title: '请假事假（结束）', width: 80}
            //     ]]
            //      ,data:data
            //         //[{
            //     //     "id": "10001"
            //     //     ,"username": "杜甫"
            //     //     ,"email": "xianxin@layui.com"
            //     //     ,"sex": "男"
            //     //     ,"city": "浙江杭州"
            //     //     ,"sign": "人生恰似一场修行"
            //     //     ,"experience": "116"
            //     //     ,"ip": "192.168.0.8"
            //     //     ,"logins": "108"
            //     // }, {
            //     //     "id": "10002"
            //     //     ,"username": "李白"
            //     //     ,"email": "xianxin@layui.com"
            //     //     ,"sex": "男"
            //     //     ,"city": "浙江杭州"
            //     //     ,"sign": "人生恰似一场修行"
            //     //     ,"experience": "12"
            //     //     ,"ip": "192.168.0.8"
            //     //     ,"logins": "106"
            //     //     ,"joinTime": "2016-10-14"
            //     // }]
            //     //,skin: 'line' //表格风格
            //     ,even: true
            //     //,page: true //是否显示分页
            //     //,limits: [5, 7, 10]
            //     //,limit: 5 //每页默认显示的数量
            // });

            // //加载页面数据
            //
            // //根据学生ID获取家长信息
            // var stuNoList = $('#stuNoList').val();
            // table
            //     .render({
            //         id : 'classList',
            //         elem : '#classList',
            //         url : '/l/listGuardian?stuNoList='+stuNoList //数据接口
            //         ,method:"POST"
            //         ,parseData: function(res){ //res 即为原始返回的数据
            //             return {
            //                 "code": res.code, //解析接口状态
            //                 "msg": res.msg, //解析提示文本
            //                 "count": res.content.total, //解析数据长度
            //                 "data": res.content.rows //解析数据列表
            //             };
            //         }
            //         ,response:{
            //             statusName:'code'
            //             ,statusCode:200
            //         }
            //         ,toolbar: '#toolbarDemo'
            //         ,limit : 10//每页默认数
            //         , limits : [ 10, 20, 30, 40 ],
            //         cols : [ [ //表头
            //             {
            //                 type : 'checkbox'
            //             }
            //             ,{field:'autoAdd', width:70, title: '序号',templet:'#autoAdd',unresize:true}
            //             ,{field:'relation', title: '关系'}
            //             ,{field:'guaName', title: '姓名'}
            //             ,{field:'wxNo', title: '微信号'}
            //             ,{field:'phone', title: '手机号'}
            //             ,{title: '操作',toolbar: '#barEdit'}
            //
            //         ] ],
            //         page : true
            //         ,where: {timestamp: (new Date()).valueOf()}
            //         //开启分页
            //     });
            //
            //
            // //监听工具条
            // table.on('tool(classList)', function(obj) {
            //     var data = obj.data;
            //     if (obj.event === 'del') {
            //
            //         //解除关系
            //         //参数      teNo：教师编号     cl_code：班级代码
            //         var stuId = $('#stuId').val();
            //         layer.confirm('真的解除么', function(index) {
            //             $.ajax({
            //                 url :'/student/delGuardian?guaId='+ data.id+"&gname="+data.guaName,
            //                 type : "post",
            //                 success : function(d) {
            //                     if (d.code == 200) {
            //                         obj.del();
            //                     } else {
            //                         layer.msg("系统错误！", {
            //                             icon : 5
            //                         });
            //                     }
            //                 }
            //             })
            //             layer.close(index);
            //         });
            //     }
            // });
            //
            //
            // //设置班级
            // //参数：teNO：教师编号  sc_code:学校代码
            // //
            // $(".userAdd_btn").click(function() {
            //     var stuNo = $("#stuNo").val();
            //     var index = layui.layer.open({
            //         type: 2,
            //         title:"添加监护人",
            //         area: ['415px', '400px'],
            //         content:"/page/setGuardian?stuId="+stuId, //这里content是一个普通的String
            //     })
            //
            //
            //     // // //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
            //     // $(window).resize(function() {
            //     //     layui.layer.full(index);
            //     // })
            //     // layui.layer.full(index);
            // })


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
