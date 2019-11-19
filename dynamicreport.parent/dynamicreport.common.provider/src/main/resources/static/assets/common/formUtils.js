$.fn.formEdit = function(data) {
	if (typeof data != 'object') {
		var search = '';
		if (typeof data == 'undefined') {
			var data = {};
			search = decodeURIComponent(location.search).slice(1);
		} else {
			search = data;
			data = {};
		}
		var search = search.split('&');
		var row = [];
		for ( var i in search) {
			row = search[i].split('=');
			data[row[0]] = row[1];
		}
		// console.log(data);
	}

	this.each(function() {
				var input, name;
				if (data == null || data instanceof Array) {
					this.reset();
					return;
				}
				for (var i = 0; i < this.length; i++) {
					input = this.elements[i];
					// checkbox的name可能是name[]数组形式
					name = (input.type == "checkbox") ? input.name.replace(
							/(.+)\[\]$/, "$1") : input.name;
					if (typeof data[name] == 'undefined')
						continue;
					switch (input.type) {
					case "checkbox":
						if (data[name] == ""||data[name] ==null) {
							input.checked = false;
						} else {
 							//input.checked = (data[name].indexOf(input.value) > -1) ? true: false;
							input.checked = data[name]; 
						}
						break;
					case "radio":
						if (data[name] === "") {
							input.checked = false;
						} else if (input.value == data[name]) {
							input.checked = true;
						}
					case "file":
						$('img[src=' + name + ']').attr('src', data[name]);
						break;
					default:
						input.value = data[name];
					}
				}
			});
	return this;
}

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

function GetQueryString(name) {

	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");

	var r = window.location.search.substr(1).match(reg);

	if (r != null)
		return (r[2]);
	return null;

}


function isDate(dateString){
	 var r=dateString.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/); 
	 if(r==null){
		return false;
	 }
	var d=new Date(r[1],r[3]-1,r[4]);   
	var num = (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);
	if(num==0){
	   return false;
	}
    return true;	   	   
 } 
 
 
 function  closeChildWindow(){
     var index = parent.layer.getFrameIndex(window.name);  
		parent.layer.close(index); 
 }
 
 function stateFormatter(value, row, index) {
     if (row.selected == true)
         return {
             checked : true//设置选中
         };
     return value;
 }
 
 //公共 table_list 初始化  url:请求的URL.columns:栏目
 function inintTableList(table_list, url, columns) {
		//初始化表格,动态从服务器加载数据  
		$('#'+table_list).bootstrapTable({
		    //使用get请求到服务器获取数据  
		    method: "GET",
		    //必须设置，不然request.getParameter获取不到请求参数
		    contentType: "application/json",
		    toolbar: '#tableToolbar',
		    //获取数据的Servlet地址  
		    url: url,
		    //表格显示条纹  
		    striped: true,
		    showRefresh: true,
		    //启动分页  
		    pagination: true,
		    //每页显示的记录数  
		    pageSize: 10,
		    //当前第几页  
		    pageNumber: 1,
		    //记录数可选列表  
		    pageList: [5, 10, 15, 20, 25],
		    //是否启用查询  
		    search: true,
		    //是否启用详细信息视图
		    detailView:true,
		    detailFormatter:detailFormatter,
		    //表示服务端请求  
		    sidePagination: "server",
		    //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder  
		    //设置为limit可以获取limit, offset, search, sort, order  
		    queryParamsType: "undefined",
		    //json数据解析
		    responseHandler: function(res) {
		        return {
		            "rows": res.data,
		            "total": res.rowsCount
		        };
		    },
		    //数据列
		    columns:columns  
		});
 }
 
 
 //每个页面+的详细
 function detailFormatter(index, row) {
     var html = [];
     if(row.id){
	        html.push('<p><b>ID:</b> ' + row.id + '</p>');
     }
     if(row.description){
	        html.push('<p><b>描述:</b> ' + row.description + '</p>');
     }
 	 if(row.createdDate){
	        html.push('<p><b>创建日期:</b> ' + row.createdDate + '</p>');
	     }
     if(row.createdUser){
	        html.push('<p><b>创建人:</b> ' + row.createdUser + '</p>');
	    }
     if(row.modifiedDate){
	        html.push('<p><b>修改日期:</b> ' + row.modifiedDate + '</p>');
	    }
     if(row.modifiedUser){
	        html.push('<p><b>修改人:</b> ' + row.modifiedUser + '</p>');
	    }
     return html.join('');
 }
 
 //公共添加> url:请求的URL.width,height:窗体的宽和高，title：窗体的标题 
 function add(url,width,height,title){
 	layer.open({
 	      type: 2,
 	      title: title,
 	      shadeClose: true,
 	      maxmin: true,           
 	      anim:3,                  
 	      shade: [0.8, '#393D49'],
 	      area: [width, height],
 	      content: url,
 	      end: function(index){
 	    	  $('#table_list').bootstrapTable("refresh");
	    	  }
 	    });
 }
 
 //公共修改
 function edit(url,width,hight,title){
  		layer.open({
   	      type: 2,
   	      title: title,
   	      shadeClose: true,
 	      maxmin: true,           
 	      anim:3,                   
 	      shade: [0.8, '#393D49'], 
   	      area: [width,hight],
   	      content: url,
   	      end: function(index){
   	    	  $('#table_list').bootstrapTable("refresh");
  	    	  }
   	    });
  }
 
 //公共删除
 function del(id,url){
 	  		layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
	     		$.ajax({
	 	    		   type: "DELETE",
	 	    		   dataType: "json",
	 	    		   url:url,
	 	    		   success: function(msg){
		 	   	    			layer.msg(msg.msg, {time: 2000},function(){
		 	   	    			$('#table_list').bootstrapTable("refresh");
		 	   	    		    layer.close(index);
		 	   			 });
	 	    		   }
	 	    	});
	    		});
 	 }
 
 //公共系统管理修改
 function systemEdit(id,msg,url,width,height,title){
  	var flag=confirmIsAdmin(id);
  	if(flag==false){
 		layer.open({
   	      type: 2,
   	      title: title,
   	      shadeClose: true,
 	      maxmin: true,             
 	      anim:3,                   
 	      shade: [0.8, '#393D49'], 
   	      area: [width, height],
   	      content:url ,
   	      end: function(index){
   	    	  $('#table_list').bootstrapTable("refresh");
  	    	  }
   	    });
 	}else{
  		$.showMsgText(msg);
  	}
   }

 
 //公共系统管理删除
 function systemDel(id,url,msg){
 	var flag=confirmIsAdmin(id);
  	if(flag==false){
  		layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
     		$.ajax({
 	    		   type: "DELETE",
 	    		   dataType: "json",
 	    		   url: url,
 	    		   success: function(msg){
	 	   	    			layer.msg(msg.msg, {time: 2000},function(){
	 	   	    				$('#table_list').bootstrapTable("refresh");
	 	   	    				layer.close(index);
	 	   					});
 	    		   }
 	    	});
    		});
 	}else{
  		$.showMsgText(msg);
 	}
 	
 }
 
 
 //添加修改页面 保存时 rules：页面要检查的参数，比如是否为空。url 要访问的URL
 function  fromValidate(rules,url){
	 $("#frm").validate({
	    rules: rules,
 	    messages: {},
 	    submitHandler:function(form){
 	    	$("#submitBut").attr("disabled","disabled");
 	    	$.ajax({
	    		   type: "POST",
	    		   dataType: "json",
	    		   url: url,
	    		   data: $(form).serialize(),
	    		   success: function(msg){
	   	    			layer.msg(msg.msg, {time: 2000},function(){
	   	    			 $("#submitBut").removeAttr("disabled");
	   	    				if(msg.success){
		   						var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		   						parent.layer.close(index); 
	   	    				}
	   					});
	    		   }
	    		});
         } 
 	});
 }
 
 //批量删除 
 function batchDelete(url){
		layer.confirm('确定删除吗?', {icon: 3, title:'提示'}, function(index){
			//获取所有被选中的记录
	        var rows = $("#table_list").bootstrapTable('getSelections');
			if(rows.length==0){
				layer.msg("请选中要删除的记录！", {time: 2000},function(){
    				return;
				});
				return;
			}
	        var ids = '';
	        for (var i = 0; i < rows.length; i++) {
	            ids += rows[i]['id'] + ",";
	        }
	        ids = ids.substring(0, ids.length - 1);
    		$.ajax({
	    		   type: "POST",
	    		   dataType: "json",
	    		   url: url + ids,
	    		   success: function(msg){
 	   	    			layer.msg(msg.msg, {time: 2000},function(){
 	   	    				$('#table_list').bootstrapTable("refresh");
 	   	    				layer.close(index);
 	   					});
	    		   }
	    	});
   	});
 }
 
 
 function stateFormatter(value, row, index) {
     if (row.selected == true)
         return {
             checked : true//设置选中
         };
     return value;
 }

 
 	
 