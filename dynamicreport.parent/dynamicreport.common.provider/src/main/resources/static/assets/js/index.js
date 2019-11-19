$(function() {
	loadMenu();
	var globalPermissions = [];
	var loginUser = '';
	loadUserInfo();
});

function logout() {
	$.ajax({
		type : "POST",
		dataType : "json",
		url : "login/logout",
		success : function(msg) {
			if (msg.success) {
				location.href = "login.html";
			} else {
				layer.msg(msg.msg, {
					time : 2000
				}, function() {
					var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
					parent.layer.close(index);
				});
			}
		}
	});
}

function modifyPwdShow() {
	layer.open({
		type : 2,
		title : '修改密码',
		shadeClose : true,
		maxmin : true, // 最大化按钮
		anim : 3, // 动画
		shade : [ 0.8, '#393D49' ],// 遮罩层
		area : [ '450px', '450px' ],
		content : 'modifyPwd.html',
		end : function(index) {
			$('#table_list').bootstrapTable("refresh");
		}
	});
}

function loadUserInfo() {
	$.ajax({
		type : "POST",
		dataType : "json",
		url : "login/user-info",
		async: false,
		success : function(result) {
			if (result.success) {
				if (result.obj) {
					globalPermissions = result.obj.permissions;
					loginUser = result.obj.username;
					if (result.obj.nickName) {
						$('#userNameDisplay')
								.html(result.obj.nickName);
					} else {
						$('#userNameDisplay')
								.html(result.obj.username);
					}
					if (result.obj.roleName) {
						$('#roleNameDisplay')
								.html(result.obj.roleName+"<b class='caret'></b>");
					}
				}
			} else {
				layer.msg(result.msg, {
					time : 2000
				}, function() {
					var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
					parent.layer.close(index);
				});
			}
		}
	});
}

function loadMenu() {
	$.ajax({
		type : "GET",
		dataType : "json",
		url : "menu/user-menu",
		async : false,
		success : function(result) {
			if (result) {
				// for (var i = 0; i < result.length; i++) {
				// 	var item = result[i];
				// 	var li = "<li><a href=\"#\"><i class=\"fa " + item.icon
				// 			+ "\"></i> <span";
				// 	li += "		class=\"nav-label\">" + item.text
				// 			+ "</span><span class=\"fa arrow\"></span></a>";
				// 	var nodes = item.nodes;
				// 	if (nodes && nodes.length > 0) {
				// 		li += "	<ul class=\"nav nav-second-level\">";
				// 		for (var j = 0; j < nodes.length; j++) {
				// 			var nItem = nodes[j];
				// 			li += "		<li><a class=\"J_menuItem\" href=\""
				// 					+ nItem.url + "\">"
				// 					+ "<i class=\"fa fa-hdd-o" + nItem.icon + "\"></i>"
				// 					+ nItem.text + "</a>";
				// 			li += "		</li>";
				// 		}
				// 		li += "	</ul>";
				// 	}
				// 	li += "<li>";
				// 	$("#side-menu").append(li);
				// 	$("#side-menu").trigger("click");
				// }

				var menu_html = template('menu_template', {menu_data: result});
				$("#side-menu").append(menu_html);
				$("#side-menu").trigger("click");
			}
		}
	});
}