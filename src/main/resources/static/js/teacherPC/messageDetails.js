layui
    .config({
        base : "js/"
    })
    .use(
        [ 'form', 'layer', 'jquery', 'table', 'laydate' ],
        function() {
            var form = layui.form, table = layui.table, layer = parent.layer === undefined ? layui.layer
                : parent.layer, laydate = layui.laydate
            $ = layui.jquery;
            var logId = $('#logId').val();
            var state = "";
            var typemsg = $('#typemsg').val();

            msgtitle = "微信号";
            if(typemsg=="手机短信"){
                msgtitle = "手机号";
            }
            table
                .render({
                    id : 'classList',
                    elem : '#classList',
                    url : '/message/detailList?logId='+logId+"&state="+ state //数据接口
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
                        ,{field:'scName', title: '学校'}
                        ,{field:'graName', title: '年级'}
                        ,{field:'claName', title: '班级'}
                        ,{field:'stuName', title: '学生姓名'}
                        ,{field:'stuNo', title: '学生编号'}
                        ,{field:'guaName', title: '接收人'}
                        ,{field:'phone', title: msgtitle}
                        ,{field:'state', title: '状态'}

                    ] ],
                    page : true
                    //开启分页
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

