/**
 * html5与Android同步交互接口
 *
 * @param {}
 *            service
 * @param {}
 *            action
 * @param {}
 *            args
 * @return {}
 */

 var exec = function(service,action,args){
    var json = {
        "service" : service,
        "action" : action
     }

     var result_str = prompt(JSON.stringify(json),JSON.stringify(args));
     var result;
     try{
        result = JSON.parse(result_str);
     }catch(e){
        console.error(e.message);
     }

     var status = result.status;
     var message = result.message;
     if(status == '999999' || status == '000000') {
        result message;
     }else{
        console.error("service:" + service + "action:" + action + "error:"
                      				+ message);
     }

/**
 * html5与Android异步交互接口
 *
 * @param {}
 *            service 调用的服务
 * @param {}
 *            action 调用的操作
 * @param {}
 *            args 对应的参数
 * @param {}
 *            success 成功时的回调方法
 * @param {}
 *            fail 失败时的回调方法
 */

 var exec_asyn = function(service,action,args,success,fail){
    var json = {
        "service" : service,
        "action" : action
    }
    var result = AndroidHtml5.callNative(json,args,success,fail);
 }

var AndroidHtml5 = {
    idCounter : 0,
    OUTPUT_RESULTS : {},
    CALLBACE_SUCCESS : {},
    CALLBACE_FAIL : {},
    callNative : function(cmd,args,success,fail){
        var key = "ID_" + (++this.idCounter);

        window.niftf.setCmds(JSON.stringify(cmd),key);
        window.niftf.setArgs(JSON.stringify(args),key);

        if(typeof success != 'undefined'){
        AndroidHtml5.CALLBACE_SUCCESS[key] = success;
        }else {
            AndroidHtml5.CALLBACE_SUCCESS[key] = function(result){
            };
        }

        if(typeof fail != 'undefined'){
            AndroidHtml5.CALLBACE_FAIL[key] = fail;
        }else{
            AndroidHtml5.CALLBACE_FAIL[key] = function(result) {
            			};
        }

        var iframe = document.createElement("IFRAME");
        iframe.setAttribute("src", "androidhtml://ready?id=" + key);
        document.documentElement.appendChild(iframe);
        iframe.paentNode.removeChild(iframe);
        iframe = null;
        return this.OUTPUT_RESULTS[key];
         },

        callBackJs : function(result, key) {
        		loadingHide();
        		this.OUTPUT_RESULTS[key] = result;
        		//var obj = JSON.parse(result.replace(/[\\\/\b\f\n\r\t]/g, ' ').replace(/\"{/g, '{').replace(/\}"/g, '}'));
        		if(result.indexOf('"{') >= 0){
        			var obj = JSON.parse(result.replace(/[\\\/\b\f\n\r\t]/g, ' ').replace(/\"{/g, '{').replace(/\}"/g, '}'));
        		}else{
        			var obj = JSON.parse(result.replace(/[\\\/\b\f\n\r\t]/g, ' '));
        		}
        		var message = JSON.stringify(obj.message);
        		console.log("obj :" + obj);
        		var status = obj.status;
        		if (status == '000000') {
        			if (typeof this.CALLBACE_SUCCESS[key] != 'undefined') {
        				setTimeout("AndroidHtml5.CALLBACE_SUCCESS['" + key + "']('"
        								+ message + "')", 0);
        			}
        		} else {
        			if (typeof this.CALLBACE_SUCCESS[key] != 'undefined') {
        				setTimeout("AndroidHtml5.CALLBACE_FAIL['" + key + "']('"
        								+ message + "')", 0);
        			}
        		}
        	}


    };
    /**
     * @see 显示说明框,使用方法Toast.makeTextShort(JSON字符串),例如 {"text":"aaaaa"}
     * @type
     */
    var Toast = {
    	makeTextShort : function(text) {
    		return exec("Toast", "makeTextShort", text);
    	},
    	makeTextLong : function(text) {
    		return exec("Toast", "makeTextLong", text);
    	}
    }
/**
 * @see 操作数据库
 * @type
 */
var DBMethod = {
	selectDb : function(msg, success, fail) {
		exec_asyn("DBMethod", "selectDb", msg, success, fail);
	},
	insertDb : function(msg, success, fail) {
		exec_asyn("DBMethod", "insertDb", msg, success, fail);
	},
	deletedDb : function(msg, success, fail) {
		exec_asyn("DBMethod", "deletedDb", msg, success, fail);
	},
	updateDb : function(msg, success, fail) {
		exec_asyn("DBMethod", "updateDb", msg, success, fail);
	}
}

/**
 * 加密方法操作数据库
 * @type
 */
var DBMethodAES = {
	selectDb : function(msg, success, fail) {
		msg.AESFlag = "01";
		exec_asyn("DBMethod", "selectDb", msg, success, fail);
	},
	insertDb : function(msg, success, fail) {
		msg.AESFlag = "01";
		exec_asyn("DBMethod", "insertDb", msg, success, fail);
	},
	deletedDb : function(msg, success, fail) {
		msg.AESFlag = "01";
		exec_asyn("DBMethod", "deletedDb", msg, success, fail);
	},
	updateDb : function(msg, success, fail) {
		msg.AESFlag = "01";
		exec_asyn("DBMethod", "updateDb", msg, success, fail);
	    }
    }

/**
 * @see 上送数据
 * @type
 */
var UploadData = {
	uploadData : function(msg, success, fail) {
		$(".error").empty();
		goToTop();
		loadingShow();
		var obj1 = {};
		obj1.eva = eav;
		var obj = $.mergeJsonObject(msg, obj1);
		exec_asyn("UploadData", "uploadData", obj, success, fail);
	},
	uploadNoHead : function(msg, success, fail) {
		$(".error").empty();
		goToTop();
		loadingShow();
		exec_asyn("UploadData", "uploadNoHead", msg, success, fail);
		// exec("UploadData", "uploadCompanyData", msg);
	}
}

/**
 * webview操作
 */
var WebView = {
	goBack : function() {
		return exec("WebViewMethod", "goBack", {});
	},
	goReload : function(text) {
		return exec("WebViewMethod", "goReload", {});
	}
}

/**
 * Session操作
 */
var Session = {
	addSession : function(text) {
		return exec("Session", "addSession", text);
	},
	removeSession : function(text) {
		return exec("Session", "removeSession", text);
	},
	getSession : function(text) {
		return exec("Session", "getSession", text);
	}
}

/**
 * 页面初始化或者跳转
 *
 * @type
 */
var PageProcessing = {
	initPage : function(text) {
		exec_asyn("PageProcessing", "initPage", text, initSuccess, fail);
	},
	jumpPage : function(text) {
		exec_asyn("PageProcessing", "jumpPage", text);
	}
}

/**
 * 失败时候的处理
 *
 * @param {}
 *            data
 */
var fail = function(data) {
	$("#errorShow").show();
	$("#errMsg").show();
	console.log(data);
	var msg = eval("(" + data + ")");
	var obj = {};
	obj.text = msg.message;
	// 如果不包含“未检索到”就提示错误信息
	if (obj.text.indexOf('未检索到') == -1) {
		Toast.makeTextLong(obj);
		showErr(data);
	}
};

/**
 * 成功通用方法，提示交易成功
 *
 * @param {}
 *            data
 */
var commonSuccess = function(data) {
	console.log(data);
	var msg = eval("(" + data + ")");
	var obj = {};
	obj.text = msg.message;
	Toast.makeTextLong(obj);
};

/**
 * 加载时候的显示loading图标
 */
function loadingShow() {
	var windowHeight = $(window).height();
	var windowWide = $(window).width();
	var heightImg = (windowHeight - 54) / 2;
	var widthImg = (windowWide - 55) / 2;
	var d = document.createElement("div");
	d.id = "loading";
	d.style.position = "absolute";
	d.style.width = windowWide + "px";
	d.style.height = windowHeight + "px";
	d.style.background = "#f5f5f5";
	d.style.top = "0";
	d.style.left = "0";
	d.style.opacity = "0.7";
	d.style.zIndex = "9999";
	d.innerHTML = "<img src='/sdcard/Android/data/com.psbc.creditApk/files/creditFactoryHtml/images/loading.gif' style='margin-top:"
			+ heightImg + "px; margin-left:" + widthImg + "px'  />";
	document.body.appendChild(d);
}

/**
 * 加载完成后去除loading图标
 */
function loadingHide() {
	if (document.getElementById('loading') != undefined) {
		var node = document.getElementById('loading');
		document.body.removeChild(node);
	} else {
		return;
	}
}

/**
 * 返回顶部
 */
function goToTop() {
	$('html,body').animate({
				scrollTop : '0px'
			}, 100);
}


 }