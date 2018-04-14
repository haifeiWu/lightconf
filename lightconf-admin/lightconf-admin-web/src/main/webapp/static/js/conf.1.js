$(function(){

    // appname change
    $('#appname').on('change', function(){
        //reload
        var appId = $('#appname').val();
        window.location.href = base_url + "/conf?appId=" + appId;
    });

	// init date tables
	var confTable = $("#conf_list").dataTable({
		"deferRender": true,
		"processing" : true,
		"serverSide": true,
		"ajax": {
			url: base_url + "/conf/pageList",
			type:"post",
			data : function ( d ) {
				var obj = {};
                obj.appId = $('#appname').val();
                obj.confKey = $('#confKey').val();
				obj.start = d.start;
				obj.length = d.length;
				return obj;
			}
		},
		"searching": false,
		"ordering": false,
		//"scrollX": true,	// X轴滚动条，取消自适应
		"columns": [
			{ "data": 'confKey', "visible" : true},
			{ "data": 'confValue', "visible" : true},
			{ "data": 'confDesc', "visible" : true},
			{ "data": '操作' ,
				"render": function ( data, type, row ) {
					return function(){
                        var html = '<p id="'+ row.id +'" '+
                            ' confKey="'+ row.confKey +'" '+
                            ' confValue="'+ row.confValue +'" '+
                            ' confDesc="'+ row.confDesc +'" '+
                            '>'+
                            '<button class="btn btn-warning btn-xs update" type="button" confDesc="'+ row.confDesc +'" confId="'+ row.id +'" confKey="'+ row.confKey +'" confValue="'+ row.confValue +'">编辑</button>  '+
                            '<button class="btn btn-danger btn-xs delete" type="button" confKey="'+ row.confKey +'" confId="'+ row.id +'">删除</button>  '+
                            '</p>';
						return html;
					};
				},"visible" : true
			}
		],
		"language" : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "每页 _MENU_ 条记录",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页 )",
			"sInfoEmpty" : "无记录",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}
	});

    $("#searchBtn").click(function(){
        confTable.fnDraw();
    });

	$("#conf_list").on('click', '.tecTips',function() {
		var tips = $(this).attr("tips");
		ComAlertTec.show(tips);
	});
	
	// 删除
	$("#conf_list").on('click', '.delete',function() {
        var confKey = $(this).attr("confKey");
        var confId = $(this).attr('confId');
        layer.confirm( "确定要删除配置：" + confKey , {
            icon: 3,
            title: '系统提示' ,
            btn: [ '确定', '取消' ]
        }, function(index){
            layer.close(index);

            $.post(
                base_url + "/conf/delete",
                {
                	"confId" : confId
				},
                function(data, status) {
                    if (data.code == 200) {
                        layer.open({
                            icon: '1',
                            content: '删除成功' ,
                            end: function(layero, index){
                                confTable.fnDraw();
                            }
                        });
                    } else {
                        layer.open({
                            icon: '2',
                            content: (data.msg||'删除失败')
                        });
                    }
                }
            );

        });
	});

    // jquery.validate 自定义校验 “英文字母开头，只含有英文字母、数字和下划线”
    jQuery.validator.addMethod("myValid01", function(value, element) {
        var length = value.length;
        var valid = /^[a-z][a-z0-9.]*$/;
        return this.optional(element) || valid.test(value);
    }, "KEY只能由小写字母、数字和.组成,须以小写字母开头");

	// 新增
	$("#add").click(function(){
		$('#addModal').modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {
        	nodeKey : {
        		required : true ,
                minlength: 4,
                maxlength: 100,
                myValid01: true
            },  
            nodeValue : {
            	required : false
            },
            nodeDesc : {
            	required : false
            }
        }, 
        messages : {
        	confKey : {
        		required :'请输入"KEY".'  ,
                minlength:'"KEY"不应低于4位',
                maxlength:'"KEY"不应超过100位'
            },
            confValue : {	},
            confDesc : {	}
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
    		$.post(base_url + "/conf/add", $("#addModal .form").serialize(), function(data, status) {
                if (data.code == 200) {
                    layer.open({
                        icon: '1',
                        content: '新增成功' ,
                        end: function(layero, index){
                            confTable.fnDraw();
                            $('#addModal').modal('hide');
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
		$("#addModal .form")[0].reset()
	});
	
	// 更新
	$("#conf_list").on('click', '.update',function() {
        $("#updateModal .form input[name='confKey']").val($(this).attr("confKey"));
        $("#updateModal .form textarea[name='confValue']").val($(this).attr("confValue"));
        $("#updateModal .form input[name='confDesc']").val($(this).attr("confDesc"));
        $("#updateModal .form textarea[name='id']").val($(this).attr("confId"));

		$('#updateModal').modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,
		rules : {
			nodeKey : {
				required : true ,
				minlength: 4,
				maxlength: 100
			},
			nodeValue : {
				required : false
			},
			nodeDesc : {
				required : false
			}
		},
		messages : {
			nodeKey : {
				required :'请输入"KEY".'  ,
				minlength:'"KEY"不应低于4位',
				maxlength:'"KEY"不应超过100位'
			},
			nodeValue : {	},
			nodeDesc : {	}
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
    		$.post(base_url + "/conf/update", $("#updateModal .form").serialize(), function(data, status) {
                if (data.code == 200) {
                    layer.open({
                        icon: '1',
                        content: '更新成功' ,
                        end: function(layero, index){
                            confTable.fnDraw();
                            $('#updateModal').modal('hide');
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
		$("#updateModal .form")[0].reset()
	});
	
});