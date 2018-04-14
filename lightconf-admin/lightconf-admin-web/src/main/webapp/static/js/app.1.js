$(function() {

    // remove
    $('.remove').on('click', function(){
        var id = $(this).attr('id');
        layer.confirm( "确认删除该项目?" , {
            icon: 3,
            title: '系统提示' ,
            btn: [ '确定', '取消' ]
        }, function(index){
            layer.close(index);

            $.ajax({
                type : 'POST',
                url : base_url + '/app/delete_app',
                data : {"appId":id},
                dataType : "json",
                success : function(data){
                    if (data.code == 200) {
                        layer.open({
                            icon: '1',
                            content: '删除成功' ,
                            end: function(layero, index){
                                window.location.reload();
                            }
                        });

                    } else {
                        layer.open({
                            icon: '2',
                            content: (data.msg||'删除失败')
                        });
                    }
                },
            });

        });
    });

    // jquery.validate 自定义校验 “英文字母开头，只含有英文字母、数字和下划线”
    jQuery.validator.addMethod("myValid01", function(value, element) {
        var length = value.length;
        var valid = /^[a-z][a-zA-Z0-9-]*$/;
        return this.optional(element) || valid.test(value);
    }, "限制以小写字母开头，由小写字母、数字和中划线组成");

    $('.add').on('click', function(){
        $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var addModalValidate = $("#addModal .form").validate({
        errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
            groupName : {
                required : true,
                rangelength:[4,100],
                myValid01 : true
            },
            groupTitle : {
                required : true,
                rangelength:[4, 12]
            }
        },
        messages : {
            appName : {
                required :"请输入“应用名”",
                rangelength:"应用名长度限制为4~100",
                myValid01: "限制以小写字母开头，由小写字母、数字和中划线组成"
            },
            appDesc : {
                required :"请输入“分组名”",
                rangelength:"长度限制为4~12"
            },
            order : {
                required :"请输入“排序”",
                digits: "请输入整数",
                range: "取值范围为1~1000"
            }
        },
        highlight : function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success : function(label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement : function(error, element) {
            element.parent('div').append(error);
        },
        submitHandler : function(form) {
            $.post(base_url + "/app/add_app",  $("#addModal .form").serialize(), function(data, status) {
                // if (data.code == "200") {
                //     $('#addModal').modal('hide');
                //     setTimeout(function () {
                //         ComAlert.show(1, "新增成功", function(){
                //             window.location.reload();
                //         });
                //     }, 315);
                // } else {
                //     if (data.msg) {
                //         ComAlert.show(2, data.msg);
                //     } else {
                //         ComAlert.show(2, "新增失败");
                //     }
                // }
                if (data.code == 200) {
                    $('#addModal').modal('hide');

                    layer.open({
                        icon: '1',
                        content: '新增成功' ,
                        end: function(layero, index){
                            window.location.reload();
                        }
                    });

                } else {
                    layer.open({
                        icon: '2',
                        content: (data.msg||'新增失败')
                    });
                }
            });
        }
    });
    $("#addModal").on('hide.bs.modal', function () {
        $("#addModal .form")[0].reset();
        addModalValidate.resetForm();
        $("#addModal .form .form-group").removeClass("has-error");
    });

    $('.update').on('click', function(){
        $("#updateModal .form input[name='appName']").val($(this).attr("appName"));
        $("#updateModal .form input[name='appDesc']").val($(this).attr("appDesc"));
        $("#updateModal .form input[name='id']").val($(this).attr("id"));

        $('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
    });
    var updateModalValidate = $("#updateModal .form").validate({
        errorElement : 'span',
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
            groupName : {
                required : true,
                rangelength:[4,100],
                myValid01 : true
            },
            groupTitle : {
                required : true,
                rangelength:[4, 12]
            }
        },
        messages : {
            groupName : {
                required :"请输入“appName”",
                rangelength:"appName长度限制为4~100",
                myValid01: "限制以小写字母开头，由小写字母、数字和中划线组成"
            },
            groupTitle : {
                required :"请输入“appDesc”",
                rangelength:"长度限制为4~12"
            }
        },
        highlight : function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        success : function(label) {
            label.closest('.form-group').removeClass('has-error');
            label.remove();
        },
        errorPlacement : function(error, element) {
            element.parent('div').append(error);
        },
        submitHandler : function(form) {
            $.post(base_url + "/app/update_app",  $("#updateModal .form").serialize(), function(data, status) {
                // if (data.code == "200") {
                //     $('#addModal').modal('hide');
                //     setTimeout(function () {
                //         ComAlert.show(1, "更新成功", function(){
                //             window.location.reload();
                //         });
                //     }, 315);
                // } else {
                //     if (data.msg) {
                //         ComAlert.show(2, data.msg);
                //     } else {
                //         ComAlert.show(2, "更新失败");
                //     }
                // }
                if (data.code == 200) {
                    $('#addModal').modal('hide');

                    layer.open({
                        icon: '1',
                        content: '更新成功' ,
                        end: function(layero, index) {
                            window.location.reload();
                        }
                    });
                } else {
                    layer.open({
                        icon: '2',
                        content: (data.msg||'更新失败')
                    });
                }
            });
        }
    });
    $("#updateModal").on('hide.bs.modal', function () {
        $("#updateModal .form")[0].reset();
        addModalValidate.resetForm();
        $("#updateModal .form .form-group").removeClass("has-error");
    });

    $('.getAppConf').on('click', function(){
        var appId = $(this).attr('id');

        $.ajax({
            url : base_url + "/app/get_app_conf",
            type : "post",
            data : {"appId":appId},
            dataType : "json",
            async: false,
            success : function(data){
                if (data.code == 200) {
                    // 跳转到A应用的配置信息界面。
                    window.location.href = base_url + "/conf?appId=" + appId;
                } else {
                    if (data.msg) {
                        ComAlert.show(2, data.msg);
                    } else {
                        ComAlert.show(2, '信息获取失败！');
                    }
                }
            },
        })
    });
});