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
                        var classCode = $('#classCode'),stuNoOrName = $('#stuNoOrName');
                        createTimeStart = $("#createTimeStart"),
                            createTimeEnd = $("#createTimeEnd");
                        //执行重载
                        table
                            .reload(
                                'userList',
                                {
                                    page : {
                                        curr : 1
                                        //重新从第 1 页开始
                                    },
                                    where : {
                                      //  String stuNoAndName, String startDate, String endDate,String cl_code
                                        //key: {
                                        stuNoAndName : stuNoOrName
                                            .val(),
                                        cl_code : classCode
                                            .val(),
                                        startDate : createTimeStart
                                            .val(),
                                        endDate : createTimeEnd
                                            .val()
                                        //}
                                    }
                                });
                    }
                };

            var start = laydate.render({
                elem : '#createTimeStart',
                type : 'datetime',
                // max : nowTime,
                btns : [ 'clear', 'confirm' ],
                done : function(value, date) {
                    endMax = end.config.max;
                    end.config.min = date;
                    end.config.min.month = date.month - 1;
                }
            });
            var end = laydate.render({
                elem : '#createTimeEnd',
                type : 'datetime',
                // max : nowTime,
                done : function(value, date) {
                    if ($.trim(value) == '') {
                        var curDate = new Date();
                        date = {
                            'date' : curDate.getDate(),
                            'month' : curDate.getMonth() + 1,
                            'year' : curDate.getFullYear()
                        };
                    }
                    start.config.max = date;
                    start.config.max.month = date.month - 1;
                }
            })

            //加载页面数据
            table
                .render({
                    id : 'userList',
                    elem : '#userList',
                    method:'POST',
                    url :  '/leave/list' //数据接口
                    ,parseData: function(res){ //res 即为原始返回的数据
                        return {
                            "code": res.code, //解析接口状态
                            "data": res.content.data //解析数据列表
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
                        },
                        {
                            field : 'id',
                            title : 'ID',
                            width : 60
                        },

                        {
                            field : 'cl_code',
                            title : '班级代码',
                            width : 120
                        },
                        {
                            field : 'name',
                            title : '班级名称'
                        },
                        {
                            field : 'stuNo',
                            title : '学生编号',

                        },
                        {
                            field : 'stuName',
                            title : '学生姓名',

                        },
                        {field:'sex', title: '性别',width:80,templet: function (d) {
                            if (d.sex =='1'){
                                return '男'
                            }else {
                                return '女'
                            }
                        }}
                        ,
                        {
                            field : 'reason',
                            title : '请假事由',

                        },
                        {
                            field : 'startDate',
                            title : '请假时间',
                            templet: '<div>{{ formatTime(d.startDate,"yyyy-MM-dd hh:mm:ss")}}</div>'

                        },
                        {
                            field : 'endDate',
                            title : '结束时间',
                            templet: '<div>{{ formatTime(d.endDate,"yyyy-MM-dd hh:mm:ss")}}</div>'

                        },  {
                            field : 'createDate',
                            title : '创建时间',
                            templet: '<div>{{ formatTime(d.createDate,"yyyy-MM-dd hh:mm:ss")}}</div>'

                        },
                        {
                            title : '操作',
                            width:120,
                            toolbar : '#barEdit'
                        } ] ],
                    page : true
                    ,where: {timestamp: (new Date()).valueOf()}
                    //开启分页
                });

            var docW = $(document).width();
            if(docW > 750){
                layerW = '587px';
                layerH = '530px';
            }else{
                layerW = '90%';
                layerH = '60%';
            }


            //监听工具条
            table.on('tool(userList)', function(obj) {
                var data = obj.data;
                if (obj.event === 'del') {
                    layer.confirm('真的删除行么', function(index) {
                        $.ajax({

                            url : '/leave/del?leaveId='+data.id,
                            type : "POST",
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
                } else if (obj.event === 'edit') {
                    layer.open({
                        type : 2,
                        title : "编辑请假信息",
                        area : [ layerW, layerH ],
                        content : "/page/teacher/editLeave?id="+data.id+"&stuName="+data.stuName
                        //这里content是一个普通的String
                    })
                }

            });

            //查询
            $(".search_btn").click(function() {
                var type = $(this).data('type');
                active[type] ? active[type].call(this) : '';
            })

            //添加维修信息
            $(".userAdd_btn").click(function() {
                var index = layui.layer.open({
                    type: 2,
                    title:"添加信息",
                    area: ['415px', '460px'],
                    content:ctx+"/repair/addRepair", //这里content是一个普通的String
                })
                //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
                $(window).resize(function() {
                    layui.layer.full(index);
                })
                layui.layer.full(index);
            })

            //批量删除
            $(".batchDel")
                .click(
                    function() {
                        var checkStatus = table
                            .checkStatus('userList'), data = checkStatus.data, userStr = '';
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

                                                    if (d.code == 0) {
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
                            layer.msg("请选择需要删除的维修信息");
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
