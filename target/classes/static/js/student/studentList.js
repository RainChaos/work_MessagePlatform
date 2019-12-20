layui
    .config({
        base : "js/"
    })
    .use(
        [ 'form', 'layer', 'jquery', 'table', 'laydate', 'util', 'tree'],
        function() {
            var form = layui.form, table = layui.table, layer = parent.layer === undefined ? layui.layer
                : parent.layer, laydate = layui.laydate
            $ = layui.jquery,
                tree = layui.tree,
                util = layui.util,
                nowTime = new Date().valueOf(),
                max = null;

//树形结构图-------------








            //异步获取结构图结束
            active = {
                search : function() {
                    var stuName = $('#stuName');
                    // var cl_code = $('#cl_code');
                    var isBind = $("#isBindSelect option:selected");
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
                                    // clCode : cl_code.val(),
                                    stuName : stuName
                                        .val(),
                                    isBind:isBind.val()

                                }
                            });
                }
            };
            //加载页面数据
             table
                .render({
                    id : 'studentList',
                    elem : '#studentList',
                    url : '/student/list' //数据接口
                    ,method:"POST"
                    ,parseData: function(res){ //res 即为原始返回的数据
                        return {
                            "code": res.code, //解析接口状态
                            "msg": res.msg, //解析提示文本
                            "count": res.content.total, //解析数据长度
                            "data": res.content.data //解析数据列表
                        };
                    }
                    ,response:{
                        statusName:'code'
                        ,statusCode:200
                    }
                    ,toolbar: '#toolbarDemo'
                    ,limit : 10//每页默认数
                    , limits : [ 10, 20, 30, 40 ]
                    ,cellMinWidth: 80,
                    cols : [ [ //表头
                        {
                            type : 'checkbox'
                        }
                        // ,{title: '序号',templet:'autoAdd'}
                        ,{field:'autoAdd', width:70, title: '序号',templet:'#autoAdd',unresize:true}
                        ,{field:'schoolName', title: '学校'}
                        ,{field:'gradeName', title: '年级'}
                        ,{field:'className', title: '班级',width:100}
                        ,{field:'stuNo', width:160, title: '学生编号', sort: true,width:100}
                        ,{field:'stuName',width: 153, title: '学生姓名'}
                        ,{width: 120, title: '微信绑定',templet:'#isBind'}
                        ,{field:'sex', title: '性别',  width:80,templet: function (d) {
                            if (d.sex =='1'){
                                return '男'
                            }else {
                                return '女'
                            }
                        }}
                        ,{field:'idCode', title: '身份证号'}
                        ,{field:'className', width:80, title: '班级'}
                        ,{field:'createDate',width: 200, title: '创建时间',templet: '<div>{{ formatTime(d.createDate,"yyyy-MM-dd hh:mm:ss")}}</div>'}
                        ,{field:'statustime',width: 200, title: '更新时间',templet: '<div>{{ formatTime(d.updateDate,"yyyy-MM-dd hh:mm:ss")}}</div>'}
                        ,{title: '操作',width: 234,toolbar: '#barEdit'}

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
                        $('#gr_code').append('<option value="0">请选择年级</option>');
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



            //监听年级选择
            form.on('select(gr_code)', function(data){

                //用户已选学校
                var sc_code = $('#sc_code').val();
                //用户已选年级
                var gr_code =  data.value;
                $.ajax({
                    type: "post",
                    url: "/class/listClass?sc_code="+sc_code+"&gr_code="+gr_code,
                    success:function(data){

                        $('#cl_code').empty();
                        $('#cl_code').append('<option value="0">请选择班级</option>');
                        var t;
                        if(data.code==200){
                            for ( i in data.content.rows){
                                t="<option value="+data.content.rows[i].cl_code+">"+data.content.rows[i].name+"</option>"
                                $('#cl_code').append(t);
                            }
                        }else {
                            t='<option value="1" selected="selected">暂无相关信息</option>'
                            $('#cl_code').append(t);
                        }
                        form.render('select');
                    }
                });
            });





            //监听工具条
            table.on('tool(studentList)', function(obj) {
                var data = obj.data;
                if (obj.event === 'del') {
                    layer.confirm('删除该学生，也将会删除该学生的监护人信息，您确定要删除吗？', function(index) {
                        $.ajax({
                            url :'/student/del?stuId='
                            + data.stuId,
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
                    layer.open({
                        type : 2,
                        title : "编辑学生信息",
                        area : [ '400px', '500px' ],
                        content : "/page/editStudent?stuId="
                        + data.stuId, //这里content是一个普通的String
                        end: function(index, layero){
                            //do something
                            layer.close(index); //如果设定了yes回调，需进行手工关闭
                            table.reload('studentList', {})
                        }
                    })
                }else if (obj.event === 'editGuardian') {

                    layer.open({
                        type : 2,
                        title : "设置家长信息",
                        area : [ '700px', '500px' ],
                        content : "/page/student/guardian?stuId="
                        + data.stuId //这里content是一个普通的String
                    })

                }else  if(obj.event === 'editClass'){

                    layer.open({
                        type : 2,
                        title : "学生班级设置",
                        area : [ '700px', '500px' ],
                        content : "/page/student/editClass?stuId="
                        + data.stuId //这里content是一个普通的String
                    })

                }
            });

            //查询
            $(".search_btn").click(function() {
                var type = $(this).data('type');
                active[type] ? active[type].call(this) : '';
            })

            //学生导入文件按钮点击事件
            $(".input_btn").click(function(){
                layer.open({
                    type: 2,
                    title:"导入学生",
                    area: ['450px', '296px'],
                    content:"/page/inportSudent", //这里content是一个普通的String
                })
            })

            $(".output_img_btn").click(function(){

                var uniType = $('#uniType').val();
                if(uniType==""){
                    layer.msg("请先选择要导出的单位!", {icon : 5});
                    return;
                }
                var uniCode = $('#cl_code').val();
                var sc_code = $('#sc_code').val();
                window.location.href="/student/exportImg?flag="+uniType+"&unitCode="+uniCode+"&scCode="+sc_code;

            })

            $(".output_wxNo_btn").click(function(){

                var uniType = $('#uniType').val();
                if(uniType==""){
                    layer.msg("请先选择要导出的单位!", {icon : 5});
                    return;
                }
                var uniCode = $('#cl_code').val();
                var sc_code = $('#sc_code').val();
                var unitName = $('#unitName').val();
                window.location.href="/student/outStudentList?flag="+uniType+"&unitCode="+uniCode+"&scCode="+sc_code+"&unitName="+unitName;
            })

            //添加学生信息delete_btn
            //1.判断用户是否已经选择学校，
            $(".userAdd_btn").click(function() {
                if($('#cl_code').val()==""){
                    layer.msg('请先选择班级',{icon: 5});
                    return false;
                }
                layer.open({
                    type: 2,
                    title:"添加学生",
                    area: ['415px', '460px'],
                    content:"/page/addStudent?cl_code="+$('#cl_code').val(), //这里content是一个普通的String
                    end: function(index, layero){
                        //do something
                        layer.close(index); //如果设定了yes回调，需进行手工关闭
                        table.reload('studentList', {})
                    }
                })
            })
            $(".userDelete_btn").click(function() {
                if($('#cl_code').val()==""&&$('#gr_code').val()==""){
                    layer.msg('请先选择要删除(年级)班级的学生',{icon: 5});
                    return false;
                }


                layer.confirm('确定删除<strong>' + $('#unitName').val() + '</strong>的数据吗？',
                        function(index) {

                            var code = "";
                            if($('#uniType').val()=="grade"){
                                //删除年级
                                code = $('#gr_code').val();
                            }

                            if($('#uniType').val()=="class"){
                                //删除班级
                                code = $('#cl_code').val();
                            }

                            $.ajax({
                                url :'/student/batchDel?code='
                                + code+"&type="+$('#uniType').val(),
                                type : "post",
                                success : function(d) {
                                    if (d.code == 200) {
                                        layer.msg("删除成功！");
                                    } else {
                                        layer.msg("系统错误！", {
                                            icon : 5
                                        });
                                    }
                                }
                            })


                            table.reload('studentList', {})
                        });

            })








            //批量删除
            $(".batchDel")
                .click(
                    function() {
                        var checkStatus = table
                            .checkStatus('studentList'), data = checkStatus.data, userStr = '';
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
                            layer.msg("请选择需要删除的学生信息");
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
