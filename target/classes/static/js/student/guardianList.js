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


            //加载页面数据

            //根据学生ID获取家长信息
            var stuId = $('#stuId').val();
            table
                .render({
                    id : 'classList',
                    elem : '#classList',
                    url : '/student/listGuardian?stuId='+stuId //数据接口
                    ,method:"POST"
                    ,parseData: function(res){ //res 即为原始返回的数据
                        return {
                            "code": res.code, //解析接口状态
                            "msg": res.msg, //解析提示文本
                            "count": res.content.total, //解析数据长度
                            "data": res.content.rows //解析数据列表
                        };
                    }
                    ,response:{
                        statusName:'code'
                        ,statusCode:200
                    }
                    ,toolbar: '#toolbarDemo'
                    ,limit : 10//每页默认数
                    , limits : [ 10, 20, 30, 40 ],
                    cols : [ [ //表头
                        {
                            type : 'checkbox'
                        }
                        ,{field:'relation', title: '关系'}
                        ,{field:'guaName', title: '姓名'}
                        ,{field:'wxNo', title: '微信号'}
                        ,{field:'phone', title: '手机号'}
                        ,{title: '操作',toolbar: '#barEdit'}

                    ] ],
                    page : true
                    ,where: {timestamp: (new Date()).valueOf()}
                    //开启分页
                });


            //监听工具条
            table.on('tool(classList)', function(obj) {
                var data = obj.data;
                if (obj.event === 'del') {

                    //解除关系
                    //参数      teNo：教师编号     cl_code：班级代码
                    var stuId = $('#stuId').val();
                    layer.confirm('真的解除么', function(index) {
                        $.ajax({
                            url :'/student/delGuardian?guaId='+ data.id+"&gname="+data.guaName,
                            type : "post",
                            success : function(d) {
                                if (d.code == 200) {
                                    obj.del();
                                } else {
                                    layer.msg("系统错误！", {
                                        icon : 5
                                    });
                                }
                            }
                        })
                        layer.close(index);
                    });
                }
            });


            var docW = $(document).width();
            if(docW > 750){
                layerW = '428px';
                layerH = '410px';
            }else{
                layerW = '90%';
                layerH = '90%';
            }

            //设置班级
            //参数：teNO：教师编号  sc_code:学校代码
            //
            $(".userAdd_btn").click(function() {
                var stuNo = $("#stuNo").val();
                var index = layui.layer.open({
                    type: 2,
                    title:"添加监护人",
                    area: [layerW, layerH],
                    content:"/page/setGuardian?stuId="+stuId, //这里content是一个普通的String
                })


                // // //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
                // $(window).resize(function() {
                //     layui.layer.full(index);
                // })
                // layui.layer.full(index);
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
