/**
 * aif.js
 * 
 * @auth daiwk
 * @description 框架基础js
 */

/* 渲染菜单 */
$(function() {
	$('#side-menu').metisMenu();
});

function getJsonLength(jsonData){
	var jsonLength = 0;
	for(var item in jsonData){
     if(jsonData[item]){
    	 jsonLength++;
     }
	}
	return jsonLength;
}

/* 载入主要内容区域 */
$(function() {
	function pageload(that, title) { // that=this
		var t = $(that).attr("href");

		/*var r = '<iframe class="J_iframe" id="J_iframe" name="iframe0" width="100%" scrolling="no" src="'
				+ t + '" frameborder="0"></iframe>';*/
		var r = '<iframe class="J_iframe" id="J_iframe" name="iframe0" width="100%" height="100%" src="'+ 
				t+'" frameborder="0" seamless=""></iframe>'
		$("#page-primary").empty().append(r);
		$("#page-header-title").empty().append(title);

		return false;
	}
	$(".J_menuItem").on("click", function() {
		$this = $(this);
		var title = "";

		var lis = $(this, $('#side-menu')).parents("ul");
		for (var i = lis.length - 1; i >= 0; i--) {
			var ul = lis[i];
			var a = $(ul).prev("a");
			if($.trim(a.html())!=""){
				title += "<li>" + $.trim(a.html()) + "</li>";
			}
		}
		title += "<li>" + $.trim($this.html()) + "</li>";
		title = title.replace(/icon-angle-left/g,"");
		return pageload(this, title);
	});

	$(".J_setItem").on("click", function() {
		$this = $(this);
		var title = "<li>" + $.trim($this.html()) + "</li>";
		var res =  pageload(this, title);
		$("#_topHideClick").click();
		return res;
	});
});

// Loads the correct sidebar on window load,
// collapses the sidebar on window resize.
// Sets the min-height of #page-wrapper to window size
$(function() {
	$(window)
			.bind(
					"load resize",
					function() {
						topOffset = 50;
						width = (this.window.innerWidth > 0) ? this.window.innerWidth
								: this.screen.width;
						if (width < 768) {
							$('div.navbar-collapse').addClass('collapse');
							topOffset = 100; // 2-row-menu
						} else {
							$('div.navbar-collapse').removeClass('collapse');
						}

						height = ((this.window.innerHeight > 0) ? this.window.innerHeight
								: this.screen.height) - 1;
						height = height - topOffset;
						if (height < 1)
							height = 1;
						if (height > topOffset) {
							$("#page-wrapper").css("min-height",
									(height) + "px");
						}
					});

	var url = window.location;
	var element = $('ul.nav a').filter(function() {
		return this.href == url || url.href.indexOf(this.href) == 0;
	}).addClass('active').parent().parent().addClass('in').parent();
	if (element.is('li')) {
		element.addClass('active');
	}
});

/**
 * 载入lay皮肤
 */
$(function() {
	layer.config({
		skin : 'layer-ext-moon',
		extend : 'skin/moon/style.css'
	});
});

/**
 * 将表单序列化成json对象,同名的会组成以逗号为分隔符在字符串
 
(function($) {
	var splic_char = ','; //多值的分隔字符
	$.fn.serializeObject = function() {
		var obj = {};
		var count = 0;
		$.each(this.serializeArray(), function(i, o) {
			var n = o.name, v = o.value;
			if(obj[n] === undefined){
				obj[n] = v;
			}else{
				obj[n] = obj[n] + splic_char + v;
			}
		});
		return obj;
	};
})($);
*/
/**
 * 将表单序列化成json对象 
 */
(function($) {
	$.fn.serializeObject = function() {
	    var json = {};
	    var arrObj = this.serializeArray();
	    $.each(arrObj, function() {
	      if (json[this.name]) {
	           if (!json[this.name].push) {
	            json[this.name] = [ json[this.name] ];
	           }
	           json[this.name].push(this.value || '');
	      } else {
	           json[this.name] = this.value || '';
	      }
	    });
	    return json;
	};
})($);

/**
 * ares内置的一些方法
 */
var ar_ = {};
//DOM id检查
ar_.idCheck = function(id,msg){
	if(id==null||id==undefined||id==""){
		alert(msg);
		return false;
	}
};
ar_.back = function(){
	history.back(-1);//直接返回当前页的上一页，数据全部消息，是个新页面
};
ar_.goback = function(){
	history.go(-1);//也是返回当前页的上一页，不过表单里的数据全部还在
};
ar_.go = function(url){
	window.location.href="";
	window.location.href=url;
}

//日期控件改变值的检查
ar_.date_change = function(name){
	var $form = $("[name='"+name+"']").parents("form");
	var formId = $form.attr("id");
	$("[id='"+formId+"']").data('bootstrapValidator')
		//.updateStatus(name, 'NOT_VALIDATED', null)
		.validateField(name);
};

//按钮是否禁用
ar_.buttonDisable = function(id,disable){
	if(disable){
		$("[id='"+id+"']").blur();
		$("[id='"+id+"']").attr("disabled","disabled");
	}else{
		$("[id='"+id+"']").removeAttr("disabled");
		$("[id='"+id+"']").blur();
	}
};
$(function(){
	$.fn.extend({
		aresButtonDisable : function(disabled){
			if(disabled==true||disabled=='disabled'){
				$(this).blur();
				$(this).attr("disabled","disabled");
			}else{
				$(this).removeAttr("disabled");
				$(this).blur();
			}
		}
	});
});

/*生成页面唯一ID*/
ar_.uniqueId = function (prefix) {
    var uniqueId = function (prefix) {
        if (prefix == undefined || prefix == null || prefix == '') {
            prefix = 'id';
        }
        var random = Math.floor(Math.random() * 1000000 + 1);
        var id = prefix + random;
        var ele = document.getElementById(id);
        if (ele != null) {
            return uniqueId(prefix);
        } else {
            return id;
        }
    }
    var id = uniqueId(prefix);
    return id;
};

/*日期时间显示转换*/
ar_.dtFormat = function (date,format) {  
    var o = {  
        "M+": date.getMonth() + 1,  
        "d+": date.getDate(),  
        "h+": date.getHours(),  
        "m+": date.getMinutes(),  
        "s+": date.getSeconds(),  
        "q+": Math.floor((date.getMonth() + 3) / 3),  
        "S": date.getMilliseconds()  
    }  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));  
    }  
    for (var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
        }  
    }  
    return format;  
};
/*日期格式*/
ar_.dateFormat = function(d){
	if(d==null||d==undefined||d==""){
		return "";
	}
    if( typeof(d) == 'object'){ //Date obj
        var date = d;
    }else{
        var dNumber = new Number(d);
        var date = new Date(dNumber);
    }
	return ar_.dtFormat(date,'yyyy-MM-dd');
};
/*日期时间格式*/
ar_.datetimeFormat = function(d){
	if(d==null||d==undefined||d==""){
		return "";
	}
    if( typeof(d) == 'object'){ //Date obj
        var date = d;
    }else{
        var dNumber = new Number(d);
        var date = new Date(dNumber);
    }
	return ar_.dtFormat(date,'yyyy-MM-dd hh:mm:ss');
};
/*时间格式*/
ar_.timeFormat = function(d){
	if(d==null||d==undefined||d==""){
		return "";
	}
    if( typeof(d) == 'object'){ //Date obj
        var date = d;
    }else{
        var dNumber = new Number(d);
        var date = new Date(dNumber);
    }
	return ar_.dtFormat(date,'hh:mm:ss');
};

/*将url添加随机树*/
ar_.randomUrl = function(url){
	if(typeof url !="string"){
		return url;
	}
	if(url.indexOf("?")>0){
		return url + "&_ar_random=" + Date.parse(new Date()) + Math.random();
	}else{
		return url + "?_ar_random=" + Date.parse(new Date()) + Math.random();
	}
};

/*autoHeight 自动高度 option是对象，有 {id:"",minus:5}  id是徐设定高度的id选择器,minus是高度减值 */
ar_.autoHeight = function(option){
	var DEFAULTS = {id:"",minus:5};
	var setting = $.extend({}, DEFAULTS, option);
	
	var topHeight = top.$("#page-primary").height();
	topHeight = topHeight - setting.minus;
	$("[id='"+setting.id+"']").height(topHeight);
};

/*获取页面的dialog，p为空时，从最顶层找*/
ar_.getDialog = function(p){
	if(p==undefined){
		return top.dialog.get(window);
	}else{
		return p.dialog.get(window);
	}
};

/*ar_tab插件，tab插件，提供 add和close方法
 * $("#tabId").aresTab()
 * 调用方法 $('#tab').ar_tab().add({id:'',title:'',url:'',close:true}); 
 * 除title外，其他都可为空
 */                    
(function ($) {
    'use strict';
    var DEFAULTS = {
        id : '',
        title : '',
        url : '',
        iframe : false,
        height : "300px",
        close : true
    };

    var $obj = null;
    var add = function (option) {

        var setting = $.extend({}, DEFAULTS, option);
        if (setting.title == '') {
            setting.title = "noSettingTitle"
        }

        if (setting.id == '') {
            var titleId = ar_.uniqueId('tab');
        }
        var paneId = 'paneId' + titleId;

        var title = $('<li>', {
                'id' : titleId
            }).append(
                $('<a>', {
                    'href' : '#' + paneId,
                    'data-toggle' : 'tab'
                }).html(setting.title));

        //是否允许关闭
        if (setting.close) {
            title.append(
                $('<i>', {
                    'class' : 'close-tab glyphicon glyphicon-remove'
                }));
        }

        var pane = '<div  class="tab-pane" id="' + paneId + '"></div>';

        $obj.children('.nav-tabs').append(title);
        $obj.children(".tab-content").append(pane);

        if (setting.url != '') {
        	/*载入内容*/
        	if(setting.iframe==true){
        		$('#' + paneId).empty().append(
    				$("<iframe>",{src:setting.url,width:"100%", height:setting.height,frameborder:0})
    			);
        	}else{
                $.get(setting.url, function (data) {
                    $('#' + paneId).append(data);
                });
                $('#' + paneId).css({overflow:"auto",height:setting.height});
        	}
        }
        $obj.children('.nav-tabs').children('li.active').removeClass('active');
        $obj.children(".tab-content").children('div.tab-pane.active').removeClass('active');

        $("#" + titleId).addClass('active');
        $("#" + paneId).addClass("active");

        /*返回titleId，可手动调用关闭*/
        return titleId;
    };

    var close = function (titleId) {
        //如果关闭的是当前激活的TAB，激活他的前一个TAB
        if ($obj.find("li.active").attr('id') == titleId) {
            $("#" + titleId).prev().addClass('active');
            $("#" + 'paneId' + titleId).prev().addClass('active');
        }
        //关闭TAB
        $("#" + titleId).remove();
        $("#" + 'paneId' + titleId).remove();
    };

    $.fn.ar_tab = function () {
        $obj = $(this);
        $obj.add = add;
        $obj.close = close;
        $obj.on('mouseover', 'li', function () {
            $(this).find('.close-tab').show();
        });

        $obj.on('mouseleave', 'li', function () {
            $(this).find('.close-tab').hide();
        });

        $obj.on('click', '.close-tab', function () {
            var id = $(this).prev("a").parent("li").attr("id");
            $obj.close(id);
        });

        return $obj;
    }
    $.fn.aresTab = $.fn.ar_tab;
})($);

/*禁用后退键Backspace*/
$(document).unbind('keydown').bind('keydown', function (event) {
    var doPrevent = false;
    if (event.keyCode === 8) {
        var d = event.srcElement || event.target;
        if ((d.tagName.toUpperCase() === 'INPUT' && 
             (
                 d.type.toUpperCase() === 'TEXT' ||
                 d.type.toUpperCase() === 'PASSWORD' || 
                 d.type.toUpperCase() === 'FILE' || 
                 d.type.toUpperCase() === 'SEARCH' || 
                 d.type.toUpperCase() === 'EMAIL' || 
                 d.type.toUpperCase() === 'NUMBER' || 
                 d.type.toUpperCase() === 'DATE' )
             ) || 
             d.tagName.toUpperCase() === 'TEXTAREA') {
            doPrevent = d.readOnly || d.disabled;
        }
        else {
            doPrevent = true;
        }
    }

    if (doPrevent) {
        event.preventDefault();
    }
});



/*为date监视属性变动
(function($) {
	$(function(){
		$(".ares-date").attrchange({
	  		trackValues: true, 
	  		properties: ['readonly'],
			callback: function (event) {
				if(event.attributeName=='readonly'){
					if(event.newValue=='readonly'){
						//$(this).attr('onclick',"");
						//$(this).removeAttr('onclick');
						alert(this.click);
						this.click="";
					}else{
						$(this).attr('onclick',$(this).data("hideOnclick"));
					}
				}
				//logMessage('Status: ' + properties.status + 
				//'. Attribute Name: ' + event.attributeName + 
				//' Prev Value: ' + event.oldValue + 
				//' New Value: ' + event.newValue);
			}
	  	});
	});
})($);
*/

/**
 * ares 手动校验字段插件，例子 $("#formId").aresValidField("name属性");
 * 参数 表单name 
 * @param $
 */
(function($){
	$.fn.aresValidField = function(name){
		$(this).bootstrapValidator('updateStatus', name, 'NOT_VALIDATED').bootstrapValidator('validateField', name);
	};
})($);

/**
 * ares 手动校验表单插件,返回true（校验成功）或false（校验失败）
 * @param $
 */
(function($){
	$.fn.aresValidForm = function(name){
		$(this).data('bootstrapValidator').validate(); 
        if(!$(this).data('bootstrapValidator').isValid()){ 
             return false;  
        }
        return true;
	};
})($);

/**
 * ares 元素滚动到底，父级不滚动 $("#domId").aresScrollUnique();
 * @param $
 */
(function($){
	$.fn.aresScrollUnique = function() {
	    return $(this).each(function() {
	        var eventType = 'mousewheel';
	        // 火狐是DOMMouseScroll事件
	        if (document.mozHidden !== undefined) {
	            eventType = 'DOMMouseScroll';
	        }
	        $(this).on(eventType, function(event) {
	            // 一些数据
	            var scrollTop = this.scrollTop,
	                scrollHeight = this.scrollHeight,
	                height = this.clientHeight;

	            var delta = (event.originalEvent.wheelDelta) ? event.originalEvent.wheelDelta : -(event.originalEvent.detail || 0);        

	            if ((delta > 0 && scrollTop <= delta) || (delta < 0 && scrollHeight - height - scrollTop <= -1 * delta)) {
	                // IE浏览器下滚动会跨越边界直接影响父级滚动，因此，临界时候手动边界滚动定位
	                this.scrollTop = delta > 0? 0: scrollHeight;
	                // 向上滚 || 向下滚
	                event.preventDefault();
	            }        
	        });
	    });	
	};
})($);

/*扩展jquery的resize事件，支持在resize结束时才触发事件*/
/* 使用
 $(window).on("resizeEnd", function (event) {
      // 自己的代码
 });
 */
(function ($, window) {
    var jqre = {};
    // Settings // eventName: the special event name.
    jqre.eventName = "resizeEnd";
    // delay: The numeric interval (in milliseconds)
    // at which the resizeEnd event polling loop executes.
    jqre.delay = 250;
    // Poll function
    // triggers the special event jqre.eventName when resize ends.
    // Executed every jqre.delay milliseconds while resizing.
    jqre.poll = function () {
        var elem = $(this),
            data = elem.data(jqre.eventName);
        // Clear the timer if we are still resizing
        // so that the delayed function is not exectued
        if (data.timeoutId) {
            window.clearTimeout(data.timeoutId);
        }
        // triggers the special event
        // after jqre.delay milliseconds of delay
        data.timeoutId = window.setTimeout(function () {
            elem.trigger(jqre.eventName);
        }, jqre.delay);
    };
    // Special Event definition
    $.event.special[ jqre.eventName ] = {
        // setup:
        // Called when an event handler function
        // for jqre.eventName is attached to an element
        setup: function () {
            var elem = $(this);
            elem.data(jqre.eventName, {});
            elem.on("resize", jqre.poll);
        },
        // teardown:
        // Called when the event handler function is unbound
        teardown: function () {
            var elem = $(this),
                data = elem.data(jqre.eventName);
            if (data.timeoutId) {
                window.clearTimeout(data.timeoutId);
            }
            elem.removeData(jqre.eventName);
            elem.off("resize", jqre.poll);
        }
    };
    // Creates an alias function
    $.fn[ jqre.eventName ] = function (data, fn) {
        return arguments.length > 0 ?
            this.on(jqre.eventName, null, data, fn) :
            this.trigger(jqre.eventName);
    };
}(jQuery, this));
/*END--------resizeEnd--------*/

/**
 * ares 为select添加隐藏字段插件
 *      为select添加readonly功能支持
 * @param $
 */
(function($){
	
	var DEFAULT = {
		suffix_show: "_ar_show_" /*显示的名字后缀*/
	};
	
	$.fn.aresSelectBindHidden = function(isShow,options){
		var settings = $.extend({},DEFAULT, options);
		
		this.on('change',function(){
			
			var name = $(this).attr('name');
			if(name==undefined||name==""){ return ; }
			
			var willChangeName = "";
			if(name.indexOf(settings.suffix_show)>0){
				willChangeName = name.replace(settings.suffix_show,"");
			}else{
				willChangeName = name+settings.suffix_show;
			}
			
			var value = $(this).val();
			$("[name='"+willChangeName+"']").val(value);
			
			/*触发校验*/
			var form = $("[name='"+willChangeName+"']").closest("form");
    		if(form.length>=1){
    			form.aresValidField(willChangeName);
	    		/*$(form).bootstrapValidator('updateStatus', willChangeName, 'NOT_VALIDATED').bootstrapValidator('validateField', willChangeName);*/
    		}
		});
	}
	
	$(function () {
		/* ares-select-bind-hidden 开关 */
        $('[data-toggle="ares-select-bind-hidden"]').aresSelectBindHidden();
        /* 注意：如果使用$('[name=xxx]').val(newVal)方式改了表单值，需要手动调用 onchange事件 $('[name=xxx]').trigger('change') */
    });
})($);

/**
 * ares 为checkbox添加隐藏字段插件
 * @param $
 */
(function($){
	
	var DEFAULT = {
		suffix_show: "_ar_show_" /*显示的名字后缀*/
	};
	
	$.fn.aresCheckboxBindHidden = function(isShow,options){
		var settings = $.extend({},DEFAULT, options);
		
		this.on('change',function(){
			var name = $(this).attr('name');
			if(name==undefined||name==""){ return ; }
			
			var willChangeName = "";
			if(name.indexOf(settings.suffix_show)>0){
				willChangeName = name.replace(settings.suffix_show,"");
				
				var checkDoms=document.getElementsByName(name);
				var checks = [];
				var j = -1;
				var valStr = "";
		        for(var i=0;i<checkDoms.length;i++){
		            if(checkDoms[i].checked==true){
		            	checks[++j] = checkDoms[i].value;
			        }
			    }
			    var valStr = "";
				valStr = checks.join($(this).attr("data-ar-value_split"));
				$("[name='"+willChangeName+"']").val(valStr);
				$("[name='"+willChangeName+"']").change(function(event){
		            return false;
		        });
			}else{
				
			}
			
		});
	}
	
	$(function () {
		/* ares-checkbox-bind-hidden 开关 */
        $('[data-toggle="ares-checkbox-bind-hidden"]').aresCheckboxBindHidden();
        /* 注意：如果使用$('[name=xxx]').val(newVal)方式改了表单值，需要自行设置选中值的显示 */
    });
})($);

$(function(){
	var mainHeight = function(){
		var height = $("#top-body").height();
		var mHeight = height - 105; /*original 145*/
		$("#page-primary").height(mHeight);
		$("#side-menu").height(mHeight+100);
		setTimeout(mainHeight, 200);
	}
	if($("#page-primary")){
		mainHeight();
	}
});

