layui
    .config({
        base : "js/"
    })
    .use(
        [ 'form', 'layer', 'jquery', 'table', 'laydate','upload' ],
        function() {
            var form = layui.form, table = layui.table, layer = parent.layer === undefined ? layui.layer
                : parent.layer, laydate = layui.laydate
                $ = layui.jquery,
                nowTime = new Date().valueOf(),
                max = null,
                upload = layui.upload;
            upload.render({
                method: 'post'
                ,elem: '#test8'
                ,accept:'file'
                ,exts: 'xls|xlsx'
                ,url: '/class/impClass'
                ,auto: false
                ,bindAction: '#test9'
                ,choose: function(obj){
                    form.render();
                    var sc_code = $('#sc_code option:selected').val();
                    if (sc_code =='null'|| sc_code ==''){
                        layer.msg('请先选择要导入的班级学校！');
                        return false;
                    }
                    var data = {};
                    data.sc_code = sc_code;
                    this.data=data;
                    //回显文件名
                    obj.preview(function(index, file, result){
                        $('#filename').text(file.name);//显示文件名

                    });
                }
                , done: function (res) {
                    var code = res.code;
                    if(code && code==200){
                        //上传完毕回调
                        layer.msg("文件导入成功！");
                    }else{
                        layer.msg("文件导入失败！原因:"+res.msg);
                        return;
                    }
                }
                , error: function () {
                    //请求异常回调
                }
            });


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
                    url : '/class/listClass' //数据接口
                    ,method:"POST"
                    ,parseData: function(res){ //res 即为原始返回的数据
                        return {
                            "code": res.code, //解析接口状态
                            "msg": res.msg, //解析提示文本
                            "count": res.content.total, //解析数据长度
                            "data": res.content.rows //解析数据列表
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
                        ,{field:'graName', title: '年级',sort: true}
                        ,{field:'cl_code', title: '班级代码',sort: true}
                        // ,{field:'cl_number', title: '班级编号', sort: true}
                        ,{field:'name', title: '班级名称'}
                        // ,{field:'sc_code', title: '学校代码',sort: true}

                        ,{title: '操作',toolbar: '#barEdit'}

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
                            var t,t1;
                            t1 ='<option value="allClassData">全部</option>';
                            if(data.code==200){
                                $('#gr_code').append(t1);
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
                                    var jsonObj = $.parseJSON(d);
                                    layer.msg(jsonObj.msg, {
                                        icon : 5
                                    });
                                }
                            }
                        })
                        layer.close(index);
                    });
                } else if (obj.event === 'edit') {
                    layer.open({
                        type: 1 //Page层类型
                        //,area: ['500px', '300px']
                        ,btn:["修改","取消"]
                        ,title: '班级修改'
                        ,skin: 'layui-layer-prompt'
                        ,content:
                        "<input type='hidden' name='cl_code' value="+data.cl_code+">"+
                        "<input type='hidden' name='gr_code' value="+data.gr_code+">"+
                        "<div class='layui-form-item'><label class='layui-form-label'>班级编号</label><div class='layui-input-block'>"+
                        "<input type='text' class='layui-input userName' lay-verify='required' placeholder='' name='cl_number' value="+data.cl_number+">" +"</div></div>" +
                        "<div class='layui-form-item'><label class='layui-form-label'>班级名称</label><div class='layui-input-block'>"+
                        "<input type='text' class='layui-input userName' lay-verify='required' placeholder='' name='name' value="+data.name+">" +"</div></div>"

                        ,yes: function(index, layero){
                            var cl_code= $(layero).find("input[name='cl_code']").val();
                            var gr_code= $(layero).find("input[name='gr_code']").val();
                            var name= $(layero).find("input[name='name']").val();
                            var cl_number= $(layero).find("input[name='cl_number']").val();
                            data.cl_code=cl_code;
                            data.gr_code=gr_code;
                            data.name=name;
                            data.cl_number=cl_number;
                            if(cl_number == ''){
                                layer.msg('班级编号不能为空',{icon:5});
                            }else if(name == ''){
                                layer.msg('班级名称不能为空',{icon:5});
                            }else{
                                $.ajax({
                                    type:"post",
                                    url:"/class/updateClass",
                                    data:data,
                                    // type:'json',
                                    success : function(d){
                                        if(d.code==200){
                                            //obj.del();
                                            layer.msg("修改成功！");
                                            table.reload('classList', {})
                                        }else{
                                            layer.msg("系统错误！",{icon: 5});
                                        }
                                    }
                                });
                                layer.close(index);
                            }
                        }

                    });
                }else if (obj.event === 'look') {
                    //查看班级所有的学生
                    //1.获取学校代码
                    //2.获取年级代码
                    //3.获取班级代码
                    //4.查找学生
                    // var sc_code = data.sc_code;
                    // var gr_code = data.gr_code;
                    var cl_code = data.cl_code;;
                    layer.open({
                        type : 2,
                        title : data.name+"学生列表",
                        area : [ '1280px', '700px' ],
                        content : "/page/class/studentList?cl_code="+cl_code //这里content是一个普通的String
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
                    title:"添加班级",
                    area: ['470px', '407px'],
                    content:"/page/addClass", //这里content是一个普通的String
                    end: function(index, layero){
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


