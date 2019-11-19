
/**
 * 系统公共JS文件
 */
var common_js_files = [
                       '../../assets/js/jquery.min.js?v=2.1.4',
                       '../../assets/js/bootstrap.min.js?v=3.3.6',
                       '../../assets/js/content.js?v=1.0.0',
                       '../../assets/js/plugins/validate/jquery.validate.min.js',
                       '../../assets/js/plugins/validate/additional-methods.min.js',
                       '../../assets/js/plugins/validate/messages_zh.min.js',
                       '../../assets/js/plugins/layer/layer.min.js',
                       '../../assets/js/plugins/layer/laydate/laydate.js',
                       '../../assets/js/plugins/bootstrap-table/bootstrap-table.min.js',
                        '../../assets/common/formUtils.js',
                       '../../assets/js/plugins/treeview/dialogMsg.js',
                       '../../assets/js/plugins/bootstrap-dialog/bootstrap-dialog.min.js',
                       '../../assets/common/PermissionUtils.js',
                       '../../assets/js/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js',
                       '../../assets/js/bootstrap-select.min.js'
                     ];
 /**
 * 导入JS文件
 */
for(var i = 0; i < common_js_files.length; i++){
	document.write("<script type='text/javascript' src='"+common_js_files[i]+"'></script>");
}

