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
                        var scCode = $('#scCode'),cfDeId = $('#cfDeId');
                        //执行重载
                        table
                            .reload(
                                'classList',
                                {
                                    where : {
                                        //key: {
                                        scCode : scCode
                                            .val(),
                                        cfDeId : cfDeId
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
                    url : '/config/list' //数据接口
                    ,method:"POST"
                    ,parseData: function(res){ //res 即为原始返回的数据
                        return {
                            "code": res.code, //解析接口状态
                            "msg": res.msg, //解析提示文本
                            "data": res.content //解析数据列表
                        };
                    }
                    ,toolbar: '#toolbarDemo'
                    ,limit : 10//每页默认数
                    , limits : [ 10, 20, 30, 40 ]
                    ,response:{
                        statusName:'code'
                        ,statusCode:200
                    },
                    cols : [ [ //表头
                        {
                            type : 'checkbox'
                        }
                        ,{field:'autoAdd', width:70, title: '序号',templet:'#autoAdd',unresize:true}
                        ,{field:'scName', title: '学校名称'}
                        ,{field:'cfName', title: '配置名称'}
                        ,{field:'value', title: '值'}

                        ,{title: '操作',toolbar: '#barEdit'}

                    ] ],
                });

            //监听工具条
            table.on('tool(classList)', function(obj) {
                var data = obj.data;
                if (obj.event === 'del') {
                    layer.confirm('真的删除行么', function(index) {
                        $.ajax({
                            url :'/config/del?configId='
                            + data.id,
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
                } else if (obj.event === 'edit') {

                    layer.open({
                        type: 2,
                        title:"配置设置",
                        area: ['493px', '430px'],
                        content:"/page/editConfig"
                        + "?scName="+data.scName
                        +"&configId="+data.id
                        +"&scCode="+data.scCode
                        +"&cfCode="+data.cfCode
                        +"&value="+data.value,
                        end: function(index, layero){
                            //do something
                            layer.close(index); //如果设定了yes回调，需进行手工关闭
                            table.reload('classList', {})
                        }
                    })
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
                    title:"添加配置",
                    area: ['493px', '430px'],
                    content:"/page/addConfig", //这里content是一个普通的String
                    end: function(index, layero){
                        //do something
                        layer.close(index); //如果设定了yes回调，需进行手工关闭
                        table.reload('classList', {})
                    }
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