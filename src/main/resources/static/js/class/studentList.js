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
                        var cl_code = $('#cl_code'),stuName = $('#stuName');
                        //执行重载
                        table
                            .reload(
                                'studentList',
                                {
                                    page : {
                                        curr : 1
                                        //重新从第 1 页开始
                                    },
                                    where : {
                                        //key: {
                                        clCode : cl_code
                                            .val(),
                                        stuName : stuName
                                            .val()
                                    }
                                });
                    }
                };
            //加载页面数据
            table
                .render({
                    id : 'studentList',
                    elem : '#studentList',
                    url : '/student/list?clCode='+$('#cl_code').val() //数据接口
                    ,method:"POST"
                    ,parseData: function(res){ //res 即为原始返回的数据
                        return {
                            "code": res.code, //解析接口状态
                            "msg": res.msg, //解析提示文本
                            "count": res.content.total, //解析数据长度
                            "data": res.content.data //解析数据列表
                        };
                    }
                    ,toolbar: '#toolbarDemo'
                    ,limit : 10//每页默认数
                    , limits : [ 10, 20, 30, 40 ],
                    cols : [ [ //表头
                        {
                            type : 'checkbox'
                        }
                        ,{field:'stuNo', title: '编号', sort: true}
                        ,{field:'stuName', title: '名称'}
                        ,{field:'sex', title: '性别',templet: function (d) {
                            if (d.sex =='1'){
                                return '男'
                            }else {
                                return '女'
                            }
                        }}
                        ,{field:'idCode', title: '身份号'}
                        ,{field:'className', title: '班级'}
                        ,{field:'guaName', title: '家长姓名'}
                        ,{field:'phone', title: '手机号'}
                        ,{field:'createDate', title: '创建时间',templet: '<div>{{ formatTime(d.createDate,"yyyy-MM-dd hh:mm:ss")}}</div>'}
                        ,{field:'statustime', title: '更新时间',templet: '<div>{{ formatTime(d.updateDate,"yyyy-MM-dd hh:mm:ss")}}</div>'}
                        ,{title: '操作',toolbar: '#barEdit'}

                    ] ],
                    page : true
                    ,where: {timestamp: (new Date()).valueOf()}
                    //开启分页
                });


            //监听工具条
            table.on('tool(studentList)', function(obj) {
                var data = obj.data;
                if (obj.event === 'del') {
                    layer.confirm('删除该学生，也将会删除该学生的监护人信息，您确定要删除吗？', function(index) {
                        $.ajax({
                            url :'/student/del?stuNo='
                            + data.stuNo,
                            type : "post",
                            success : function(d) {
                                if (d.code == 200) {
                                    obj.del();
                                    layer.msg("删除成功！");
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
                    var index = layui.layer.open({
                        title : "编辑学生信息",
                        type : 2,
                        content :  "/page/editStudent?stuno=" + data.stuNo,
                        success : function(layero, index){
                            layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                                tips: 3
                            });
                        }
                    })

                    //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
                    $(window).resize(function() {
                        layui.layer.full(index);
                    })
                    layui.layer.full(index);
                }

            });

            //查询
            $(".search_btn").click(function() {
                var type = $(this).data('type');
                active[type] ? active[type].call(this) : '';
            })

            //添加学生信息
            //1.判断用户是否已经选择学校，
            $(".userAdd_btn").click(function() {
                var index = layui.layer.open({
                    title : "添加学生",
                    type : 2,
                    content : "/page/addStudent?cl_code="+$('#cl_code').val(),
                    success : function(layero, index){
                        layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }
                })
                //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
                $(window).resize(function() {
                    layui.layer.full(index);
                })
                layui.layer.full(index);
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
