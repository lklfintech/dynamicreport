
/**
 * 系统公共CSS文件
 */
var common_css_files = [
                        '../../assets/css/bootstrap.min.css?v=3.3.6',
                        '../../assets/css/font-awesome.css?v=4.4.0',
                        '../../assets/css/bootstrap-dialog/bootstrap-dialog.min.css',
                        '../../assets/css/animate.css',
                        '../../assets/css/style.css?v=4.1.0',
                        '../../assets/css/plugins/bootstrap-table/bootstrap-table.min.css',
                        '../../assets/css/bootstrap-select.min.css'
                    ];
 
/**
 * 导入CSS文件
 */
for(var i = 0; i < common_css_files.length; i++){
	document.write("<link rel='stylesheet' type='text/css' href='"+common_css_files[i]+"'>");
}
 

