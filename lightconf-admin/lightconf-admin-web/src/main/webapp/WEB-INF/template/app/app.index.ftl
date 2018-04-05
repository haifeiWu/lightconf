<!DOCTYPE html>
<html>
<head>
    <title>任务调度中心</title>
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">
    <!-- daterangepicker -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/daterangepicker/daterangepicker-bs3.css">
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if> ">
<div class="wrapper">
    <!-- header -->
	<@netCommon.commonHeader />
    <!-- left -->
	<@netCommon.commonLeft />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>应用管理</h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">应用列表</h3>&nbsp;&nbsp;
                            <button class="btn btn-info btn-xs pull-left2 add" >+新增应用</button>
                        </div>
                        <div class="box-body">
                            <table id="joblog_list" class="table table-bordered table-striped display" width="100%" >
                                <thead>
                                <tr>
                                    <th name="appName" >应用名</th>
                                    <th name="appDesc" >应用描述</th>
                                    <th name="uuid" >UUID</th>
                                    <th name="publicKey" >publicKey</th>
                                </tr>
                                </thead>
                                <tbody>
								<#if list?exists && list?size gt 0>
								<#list list as app>
									<tr>
                                        <td>${app.appName}</td>
                                        <td>${app.appDesc}</td>
                                        <td>${app.uuid}</td>
                                        <td>${app.publicKey}</td>
                                        <td>
                                            <button class="btn btn-warning btn-xs update" id="${app.id}" appName = "${app.appName}" appDesc = "${app.appDesc}" >编辑</button>
                                            <button class="btn btn-danger btn-xs remove" id="${app.id}" >删除</button>
                                            <button class="btn btn-info btn-xs getAppConf" id="${app.id}" >应用配置信息</button>
                                        </td>
                                    </tr>
								</#list>
								</#if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

    <!-- 新增.模态框 -->
    <div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" >新增分组</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal form" role="form" >
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">应用名<font color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="appName" placeholder="请输入“应用名”" maxlength="64" ></div>
                        </div>
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">应用描述<font color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="appDesc" placeholder="请输入“应用描述”" maxlength="12" ></div>
                        </div>
                        <hr>
                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-6">
                                <button type="submit" class="btn btn-primary"  >保存</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- 更新.模态框 -->
    <div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" >编辑分组</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal form" role="form" >
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">应用名<font color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="appName" placeholder="请输入“应用名”" maxlength="64" readonly></div>
                        </div>
                        <div class="form-group">
                            <label for="lastname" class="col-sm-2 control-label">应用描述<font color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="appDesc" placeholder="请输入“应用描述”" maxlength="12" ></div>
                        </div>

                        <div class="form-group" style="display: none">
                            <label for="lastname" class="col-sm-2 control-label">appId<font color="red">*</font></label>
                            <div class="col-sm-10"><input type="text" class="form-control" name="id" placeholder="请输入“appId”" maxlength="12" ></div>
                        </div>
                        <hr>
                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-6">
                                <button type="submit" class="btn btn-primary"  >保存</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- footer -->
	<@netCommon.commonFooter />
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<script src="${request.contextPath}/static/js/app.1.js"></script>
</body>
</html>
