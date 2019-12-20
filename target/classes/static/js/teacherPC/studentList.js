layui.config({
    base : "js/"
}).use(['form','layer','jquery','laypage','table','tree','util'],function(){
    var form = layui.form,table = layui.table;
    layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;
    var tree = layui.tree,
        util = layui.util;

    active = {
        search : function() {
            stuName = $('#stuName');
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
                            stuName : stuName
                                .val()
                        }
                    });
        }
    };

    var glourl = "/teacher/getStuList";
    var unitName = "";
    //获取数据
    layer.ready(function(){
        $.ajax({
            url :'/teacher/getUnitTree',
            type : "post",
            success : function(d) {
                if (d.code == 200) {
                    var dateww = JSON.stringify(d.content);
                    var obj1 = JSON.parse(dateww);
                    //仅节点左侧图标控制收缩
                    tree.render({
                        elem: '#test2'
                        ,data: obj1 //数据源
                        ,id: 'id' //定义索引
                        ,spread: true
                        ,click: function(obj){
                            console.log("元素"+JSON.stringify(obj));
                            console.log("数据"+JSON.stringify(obj.data));
                            //获取用户点击的树节点并向后台获取对应的表格数据
                            var data = obj.data; //获取当前点击的节点数据
                            var last = JSON.stringify(data);
                            var obj = JSON.parse(last);
                            unitName = obj.title;

                            $(".layui-tree-txt").css("background-color: red;");

                            glourl='/teacher/getStuList?clCode='+obj.id+"&flag="+obj.unitType;
                                //数据表格
                               $("#cl_code").val("");
                                $("#cl_code").val(obj.id);


                            if(obj.unitType=="class"){
                                $("#teaClass").val("");
                                $("#teaClass").val(obj.id);
                            }

                                //数据表格【用户点击帅选。。。。。。】
                                table.render({
                                    id:'studentList',
                                    elem: '#studentList'
                                    ,method:'POST'
                                    ,url: glourl //数据接口
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
                                    ,cellMinWidth: 80
                                    ,toolbar : true
                                    ,limit:10//每页默认数
                                    ,limits:[10,20,30,40]
                                    ,cols: [[ //表头
                                        {type:'checkbox'}
                                        ,{field:'schoolName', title: '所在学校'}
                                        ,{field:'gradeName', title: '所在年级'}
                                        ,{field:'className', title: '班级名称',width:100}
                                        ,{field:'stuNo', title: '学生编号', sort: true}
                                        ,{field:'stuName', title: '学生姓名'}
                                        ,{width: 90, title: '是否绑定',templet:'#isBind'}
                                        ,{field:'sex', title: '性别',templet: '#statusTpl',width:80}
                                        ,{field:'idCode', title: '身份证号'}
                                        ,{field:'createDate', title: '创建时间',templet: '<div>{{ formatTime(d.createDate,"yyyy-MM-dd hh:mm:ss")}}</div>'}
                                        ,{field:'updateDate', title: '更新时间',templet: '<div>{{ formatTime(d.updateDate,"yyyy-MM-dd hh:mm:ss")}}</div>'}
                                        ,{title: '操作',width:260,toolbar: '#barEdit'}
                                    ]]
                                    ,page: true //开启分页
                                    ,where: {timestamp: (new Date()).valueOf()}
                                });
                        }
                    });

                }
            }
        })
    });

    //数据表格【初始化。。。。。。】
    table.render({
        id:'studentList',
        elem: '#studentList'
        ,method:'POST'
        ,url: glourl //数据接口
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
        ,cellMinWidth: 80
        ,toolbar : true
        ,limit:10//每页默认数
        ,limits:[10,20,30,40]
        ,cols: [[ //表头
            {type:'checkbox'}
            ,{field:'autoAdd', width:70, title: '序号',templet:'#autoAdd',unresize:true}
            ,{field:'schoolName', title: '所在学校'}
            ,{field:'gradeName', title: '所在年级'}
            ,{field:'className', title: '班级名称',width:100}
            ,{field:'stuNo', title: '学生编号', sort: true}
            ,{field:'stuName', title: '学生姓名'}
            ,{width: 90, title: '是否绑定',templet:'#isBind'}
            ,{field:'sex', title: '性别',templet: '#statusTpl',width:80}
            ,{field:'idCode', title: '身份证号'}
            ,{field:'createDate', title: '创建时间',templet: '<div>{{ formatTime(d.createDate,"yyyy-MM-dd hh:mm:ss")}}</div>'}
            ,{field:'updateDate', title: '更新时间',templet: '<div>{{ formatTime(d.updateDate,"yyyy-MM-dd hh:mm:ss")}}</div>'}
            ,{title: '操作',width:260,toolbar: '#barEdit'}
        ]]
        ,page: true //开启分页
    });


//手机屏幕自适应
    var docW = $(document).width();
    if(docW > 750){
        layerW = '428px';
        layerH = '410px';
        layerW1 = '600px';
        layerH1 = '500px';

    }else{
        layerW = '90%';
        layerH = '60%';
        layerW1 = '90%';
        layerH1 = '70%';
    }


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
                area : [ layerW, layerH ],
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
                title : "家长信息",
                area : [ layerW1, layerH1 ],
                content : "/page/student/guardian?stuId="
                + data.stuId //这里content是一个普通的String
            })
        }else  if(obj.event === 'editClass'){

            layer.open({
                type : 2,
                title : "班级设置",
                area : [ layerW,layerH],
                content : "/page/teacher/setClass?stuId="
                + data.stuId
                +"&stuNo="
                +data.stuNo
                +"&classCode="
                +data.clCode
                //这里content是一个普通的String
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
            title:"学生导入",
            area: [layerW, layerH],
            content:"/page/inportSudent", //这里content是一个普通的String
        })
    })

    $(".output_wxNo_btn").click(function(){
        if( $("#teaClass").val()==""){
            layer.msg("请先选择要导出的班级!", {icon : 5});
            return;
        }
        var uniCode = $('#teaClass').val();
        var sc_code = $('#sc_code').val();
        window.location.href="/student/outStudentList?flag=class"+"&unitCode="+uniCode+"&scCode="+sc_code+"&unitName="+unitName;
    })


    //添加学生
    $(".userAdd_btn").click(function(){

        var  yy = $("#cl_code").val();
        if($('#cl_code').val()==""){
            layer.msg('请先选择班级',{icon: 5});
            return false;
        }
        layer.open({
            type: 2,
            title:"添加学生",
            // area: ['428px', '410px'],
            area: [layerW, layerH],
            content:"/page/addStudent?cl_code="+yy, //这里content是一个普通的String
            end: function(index, layero){
                table.reload('studentList', {})
            }
        })
    })
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


