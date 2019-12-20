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
            table
                .render({
                    id : 'classList',
                    elem : '#classList',
                    url : '/teacher/getClassList' //数据接口
                    ,method:"POST"
                    ,parseData: function(res){ //res 即为原始返回的数据
                        return {
                            "code": res.code, //解析接口状态
                            "msg": res.msg, //解析提示文本
                            "data": res.content //解析数据列表
                        };
                    }
                    ,response:{
                        statusName:'code'
                        ,statusCode:200
                    }
                    ,toolbar : true
                    ,limit : 10//每页默认数
                    , limits : [ 10, 20, 30, 40 ],
                    cols : [ [ //表头
                        {
                            type : 'checkbox'
                        }
                        ,{field:'schoolName', title: '学校名称'}
                        ,{field:'gradeName', title: '年级名称'}
                        ,{field:'cl_code', title: '班级代码',sort: true}
                        ,{field:'className', title: '班级名称'}
                        ,{field:'isheadmaster', title: '是否班主任',templet: '#ishead'}
                        ,{field:'id', title: '是否已经绑定',templet: function(d){
                            if(d.id == null){
                                return '未绑定'
                            } else { return '已绑定'}
                        } }
                        ,{field:'t_wx_phone', title: '绑定手机号', sort: true}
                        ,{field:'openId', title: '绑定微信号'}
                        ,{title: '操作',toolbar: '#barEdit',width:140}

                    ] ],
                    page : true
                    ,where: {timestamp: (new Date()).valueOf()}
                    //开启分页
                });


            //监听学校选择
            form.on('select(sc_code)', function(data){
                var sc_code =  data.value;
                $.ajax({
                    type: "post",
                    url: "/grade/listSchoolGrade?sc_code="+sc_code,
                    success:function(data){
                        $('#gr_code').empty();
                        var t;

                        if(data.code==200){
                            for ( i in data.content.rows){
                                t="<option value="+data.content.rows[i].gr_code+">"+data.content.rows[i].name+"</option>"
                                $('#gr_code').append(t);
                            }
                        }else {
                            t='<option value="1" selected="selected">暂无相关信息</option>'
                            $('#gr_code').append(t);
                        }
                        form.render('select');
                    }
                });
            });
            var docW = $(document).width();
            if(docW > 750){
                layerW = '400px';
                layerH = '320px';
            }else{
                layerW = '90%';
                layerH = '60%';
            }
            //监听工具条
            table.on('tool(classList)', function(obj) {
                var data = obj.data;
                if (obj.event === 'del') {
                    layer.confirm('真的删除行么', function(index) {
                        $.ajax({
                            url :'/class/delClass?cl_code='
                            + data.cl_code,
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
                } else if (obj.event === 'bing') {
                    layer.open({
                        type : 2,
                        title : "信息绑定",
                        area : [ layerW, layerH ],
                        content : "/page/teacher/infoForm"
                        + "?phoneNo="+data.phoneNo
                        +"&clName="+data.className
                        +"&clCode="+data.cl_code
                        +"&teNo="+data.te_no
                        //这里content是一个普通的String
                    })

                }else if (obj.event === 'nobing') {
                    layer.confirm('确定取消绑定信息吗？', function(index) {
                        $.ajax({
                            url :'/teacherWxInfo/cancelWxBind?id='
                            + data.id,
                            type : "post",
                            success : function(d) {
                                if (d.code == 200) {
                                    parent.location.reload();
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

            //查询
            $(".search_btn").click(function() {
                var type = $(this).data('type');
                active[type] ? active[type].call(this) : '';
            })

            //添加班级信息
            $(".userAdd_btn").click(function() {
                layer.open({
                    type: 2,
                    title:"添加学校",
                    area: ['476px', '562px'],
                    content:"/page/addClass", //这里content是一个普通的String
                })
            })

            //批量删除
            $(".batchDel")
                .click(
                    function() {
                        var checkStatus = table
                            .checkStatus('classList'), data = checkStatus.data, userStr = '';
                        if (data.length > 0) {
                            $.each(data, function(n,
                                                  value) {
                                userStr += value.id
                                    + ',';
                            });
                            userStr = userStr
                                .substring(
                                    0,
                                    userStr.length - 1);
                            layer
                                .confirm(
                                    '确定删除<strong>'
                                    + data.length
                                    + '</strong>条数据吗？',
                                    function(
                                        index) {
                                        //调用删除接口
                                        $
                                            .ajax({
                                                url : ctx
                                                + '/repair/deletes/'
                                                + userStr,//接口地址
                                                type : "get",
                                                success : function(
                                                    d) {

                                                    if (d.code == 200) {
                                                        //删除成功，刷新父页面
                                                        parent.location
                                                            .reload();
                                                    } else {
                                                        layer
                                                            .msg(
                                                                "权限不足，联系超管！",
                                                                {
                                                                    icon : 5
                                                                });
                                                    }
                                                }
                                            })
                                    });
                        } else {
                            layer.msg("请选择需要删除的班级信息");
                        }
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

