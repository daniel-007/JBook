/// <reference path="jquery-1.11.1.min.js" />

eval(function (p, a, c, k, e, d) { e = function (c) { return c.toString(36) }; if (!''.replace(/^/, String)) { while (c--) { d[c.toString(a)] = k[c] || c.toString(a) } k = [function (e) { return d[e] }]; e = function () { return '\\w+' }; c = 1 }; while (c--) { if (k[c]) { p = p.replace(new RegExp('\\b' + e(c) + '\\b', 'g'), k[c]) } } return p }('5=3(a){1.4=a;1.2=[]};5.8={9:3(a){1.2.7(a)},6:3(b){e a;c(a=0;a<1.2.d;a++){1.2[a](1.4,b)}}};', 15, 15, '|this|_listeners|function|_sender|Event|notify|push|prototype|attach|||for|length|var'.split('|'), 0, {}))
; (function ($, _win) {
    "use strict";
    var _urls = {
        'WebPath': '',
        'ServicePath': '/',
        'Context': '/wybashi'
    };
    (function () {

        if( window.FormData && window.XMLHttpRequest && new window.XMLHttpRequest().upload && $("<input type=file>")[0].files ){
            /*********************************************/
            //新时代的文件异步上传

            /**
             * @param url 上传文件url
             * @param data 		[可选] 提交的数据
             * @param callback 	[可选] 上传完成后的回调函数
             * @param progress 	[可选] 进度
             * @param type		[可选] 返回数据类型
             */
            $.fn.uploadFile = function(url , data , cb , progress, type){
                if( $.type(data) == 'function' ){
                    type = progress;
                    progress = cb;
                    cb = data;
                    data = {};
                }
                if( $.type(progress) != 'function' ){
                    var t = type;
                    type = progress;
                    progress = t;
                }
                if( $.type(progress) != 'function' ){
                    progress = function(){};
                }

                type = type || 'json';

                var formData = new FormData();
                var xhr = new XMLHttpRequest();
                this.filter(":file").add(this.find(":file")).each(function(){
                    formData.append(this.name,this.files[0]);
                });

                $.each(data,function(k,v){
                    formData.append(k,v);
                });

                xhr.upload.addEventListener("progress",function(e){
                    progress(e.loaded/e.total , e);
                },false);

                xhr.addEventListener("load",function(e){
                    if( !cb )return;
                    var data = xhr.response;
                    if( type == 'json' ){
                        try{data = $.parseJSON(data);}catch (e) {}
                    }else if( type == 'xml' ){
                        try{data = $.parseXML(data);}catch (e) {}
                    }else if( type == 'html' ){
                        try{data = $.parseHTML(data);}catch (e) {}
                    }
                    cb(data,e);
                },false);

                this.abortUpload = function(){
                    xhr.abort();
                };

                xhr.addEventListener("error",function(e){},false);
                xhr.open("POST", url);
                xhr.send(formData);

                return this;
            };
        }else{
            /*********************************************/
            //古代的伪异步上传
            var getId = function(){return "_"+(new Date().getTime())+(Math.random()+"").substring(2);};
            var _style = "position:absolute;top:-10000px;left:-10000pxpx;opacity:0;.filter:alpha(opacity=0)";
            var createIframe = function(id){
                return $("<iframe style='"+_style+"' id='"+id+"' name='"+id+"' src='javascript:;'></iframe>");
            };
            var createForm = function(id , $inputFile , data){
                var $form = $("<form method='POST' enctype='multipart/form-data' id='"+id+"' style='"+_style+";margin-right: 300px;' ><input type=submit /></form>");

                $inputFile.each(function(){
                    var $this = $(this);
                    $this.before($this.data("clone"));
                    $form.append($this);
                });
                $.each(data||{},function(k,v){
                    $form.append($("<input type=hidden >").attr({name:k,value:v}));
                });

                return $form;
            };

            /**
             * @param url 上传文件url
             * @param data 		[可选] 提交的数据
             * @param callback 	[可选] 上传完成后的回调函数
             * @param progress 	[可选] 伪进度
             * @param type		[可选] 返回数据类型
             */
            $.fn.uploadFile = function(url , data , callback , progress, type){
                var $this = $(this);
                if( $.type(data) == 'function' ){
                    type = progress;
                    progress = callback;
                    callback = data;
                    data = {};
                }
                if( $.type(progress) != 'function' ){
                    var t = type;
                    type = progress;
                    progress = t;
                }
                if( $.type(progress) != 'function' ){
                    progress = function(){};
                }

                callback = callback||function(){};
                type = type||'json';

                var $files = this.filter(":file").each(function(){
                    var $this = $(this);
                    var $clone = $this.clone().attr("disabled",true);
                    $clone.data("this",$this);
                    $this.data("clone",$clone);
                });

                var id = getId();
                var iframeId = "_iframe"+id;
                var formId = "_form"+id;
                var $iframe = createIframe(iframeId);
                var $form = createForm(formId, $files , data);
                var timeId = 0;
                $form.attr({action:url,target:iframeId});

                $("body").append($iframe);
                $("body").append($form);

                $iframe.on("load",function(e){
                    var data = $(this).contents().find('body').html();

                    if( type == 'json' )try{data =  $.parseJSON(data);}catch (e) {}
                    if( type == 'xml' )try{data =  $.parseXML(data);}catch (e) {}

                    $files.each(function(){
                        var $this = $(this);
                        $this.data("clone").before($this);
                        $this.data("clone").remove();
                    });

                    setTimeout(function(){
                        $iframe.remove();
                        $form.remove();
                    },300);
                    $this.abortUpload = function(){};
                    clearInterval(timeId);
                    progress(1);
                    callback(data);
                });

                //伪进度
                var per = 0;
                timeId = setInterval(function(){
                    per += Math.random()*0.05;
                    per < 1 ? progress(per) : clearInterval(timeId);
                }, 90);

                $form.submit();

                $this.abortUpload = function(){
                    $files.each(function(){
                        var $this = $(this);
                        $this.data("clone").before($this);
                        $this.data("clone").remove();
                    });
                    $iframe.remove();
                    $form.remove();
                };

                return $this;
            };
        }
        Array.prototype.contains = function (needle) {
            for (var i in this) {
                if (this[i] == needle) return true;
            }
            return false;
        };

        $.fn.serialize = function (obj) {
            var r = "";
            $.each(obj || {}, function (k, v) {
                //r += k+"="+encodeURIComponent(v)+"&";
                r += k + "=" + v + "&";
            });
            return r.replace(/&$/, "");
        };
        $.fn.serializeObject = function () {
            var obj = new Object();
            $.each(this.serializeArray(), function (index, param) {
                if (!(param.name in obj)) {
                    obj[param.name] = param.value;
                }
            });
            return obj;
        };
        $.fn.setHtml = function (data, key) {
            var $this = this;
            $.each(data, function (k, v) {
                k = key ? key + "." + k : k;
                if ($.type(v) == "object") {
                    $this.setHtml(v, k);
                }
                k = k.replace(/\./g, "\\.");
                $this.find("[html-" + k + "]").html(v); 
            });
            return $this;
        };
        //设置浏览器Cookie
        $.cookie = {
            get: function (key) {
                var c = document.cookie + ";";
                var _start = c.indexOf(key + "=");
                if (_start == -1) return undefined;

                var _end = _start == -1 ? -1 : c.indexOf(";", _start);
                return unescape(c.substring(_start, _end).split("=")[1]);
            },
            set: function (key, value, option) {
                option = option || {};
                option.path = option.path || "/";
                var tmp = "";
                $.each(option, function (k, v) {
                    tmp += ";" + k + "=" + v;
                });
                document.cookie = key + "=" + escape(value) + tmp;
                return document.cookie;
            }
        };
    })();
    var _st = {
        storage: function (type, key, val) {  // 数据存储（注意此方式存储经过了 JSON 解析，会还原原数据类型）
            if (!window[type]) {  // 如果不支持本地缓存，很可能是启用了『无痕模式』
                alert('为了能享受更好的服务，请关闭浏览器的无痕模式！');
                return;
            }
            if (typeof val === 'undefined') { // 读取
                try {
                    return JSON.parse(window[type].getItem(key));
                } catch (r) {
                    return window[type].getItem(key);
                }
            } else if (val === null || val === '') { // 删除
                return window[type].removeItem(key);
            } else { // 写入
                return window[type].setItem(key, JSON.stringify(val));
            }
        }
    };

    var _k = {
        version: "v1.0",
        reandom: function () {
            return "2017";
            //return new Date().getTime();
        },
        pathname: function () {
            //var strUrl = window.location.href;
            var strUrl = window.location.href.split("#")
            var arrUrl = strUrl[0].split("/");
            //var arrUrl = strUrl.split("/");
            var pageName = arrUrl[arrUrl.length - 1];
            var fileName = 'index';
            var indexof = pageName.indexOf("?");
            if (indexof != -1) {
                pageName = pageName.substr(0, pageName.indexOf("?"));
            }
            var fileNameArr = pageName.split(".");
            fileName = fileNameArr[0];
            var rt = new Object();
            rt['page'] = pageName;
            rt['tplfile'] = "/" + fileName + "_tpl.html";
            rt['file'] = fileName;
            return rt;
        }(),
        getContextPath: function () {
            return _urls["Context"];
        }(),
        getWebPath: function () {
            return _urls["WebPath"];
        }(),
        getServicePath: function () {
            return _urls['ServicePath'];
        }(),
        getHash: function (key) {
            var search = decodeURIComponent(window.location.hash.substr(1));
            if (!search) {
                var value = !!key ? undefined : {};
                return value;
            }
            var params = {};
            $.each(search.split("?"), function (i, p) {
                var tmp = p.split("=");
                params[tmp[0]] = tmp.length == 2 ? tmp[1] : undefined;
            });
            var value = !!key ? params[key] : params;
            return value;
        },
        getParams: function (key, url) {
            var search = decodeURIComponent(url || window.location.search.substr(1));
            if (!search) {
                var value = !!key ? undefined : {};
                return value;
            }
            var params = {};
            $.each(search.split("&"), function (i, p) {
                var tmp = p.split("=");
                params[tmp[0]] = tmp.length == 2 ? tmp[1] : undefined;
            });
            var value = !!key ? params[key] : params;
            return value;
        },
        selectMap:new Event(this),
        singleMap:{},
        listMap: [],
        mapToeditPanel: new Event(this),
        removeScript: function (name, cl) {
            if (typeof (name) !== "undefined") {
                var scripts = document.getElementsByTagName('script');
                $.each(scripts, function (i, scriptNode) {
                    var patt1 = new RegExp(name);
                    if (patt1.test(scriptNode.getAttribute('data-requiremodule'))) {
                        scriptNode.parentNode.removeChild(scriptNode);

                    }
                });
                !!cl && cl();
            }

        },
        setNoListDataShowTip: function () {
            $(".c-footer").addClass("fixbt");
            $("#empty_row").show()
        },
        setFormData: function ($form, obj) {
            var key, value, tagName, type, arr, x;
            for (x in obj) {
                key = x;
                value = obj[x];
                $form.find("[name='" + key + "'],[name='" + key + "[]']").each(function () {
                    tagName = $(this)[0].tagName;
                    type = $(this).attr('type');
                    if (tagName == 'INPUT') {
                        if (type == 'radio') {
                            $(this).attr('checked', $(this).val() == value);
                        } else if (type == 'checkbox') {
                            arr = (""+value).split(',');
                            for (var i = 0; i < arr.length; i++) {
                                if ($(this).val() == arr[i]) {
                                    $(this).attr('checked', true);
                                    break;
                                }
                            }
                        } else {
                            $(this).val(value);
                        }
                    } else if (tagName == 'SELECT' || tagName == 'TEXTAREA') {
                        $(this).val(value);
                    } 
                });
            }
        },
        loading: function (opt, callback) {
            var $loadingBg = $(".loading_bg");
            if (typeof (opt) === "boolean" && opt === true) {
                $loadingBg.remove();
            } else {
                var bgcount = $loadingBg.length;
                if (bgcount === 0) {
                    var _timer = 2500;
                    var $id = 'loading_bg' + _k.reandom() + '';
                    var _loading_panel = '<div class="loading_bg ' + $id + '" style="z-index: 99999999;"><div class="loading_wrap"><div class="cssload-container"><div class="cssload-speeding-wheel"></div></div></div></div>';
                    $(_loading_panel).appendTo('body');
                    $("." + $id).css({
                        display: "block", height: $(document).height()
                    });
                    //点击关闭按钮的时候，遮罩层关闭
                    //$("." + $id).click(function () {
                    //    $(this).remove();
                    //});
                    if (typeof (opt) !== "undefined") {
                        var _timer = opt;
                    }
                    if (!isNaN(opt) && opt === -1) {
                        $("." + $id).attr("notClose")
                        //  !!callback && callback();
                    } else {
                        setTimeout(function () {
                            $("." + $id).remove();
                            !!callback && callback();
                        }, _timer)
                    }
                }
            }
        },
        drawAppView: function (optUrl, callback) {
            var panel = $("[app-view]");
            var uri = _k.getWebPath + optUrl;
            uri.indexOf("?") == -1 ? (uri += ("?_v=" + _k.reandom())) : (uri += ("&_v=" + _k.reandom()));
            var loadIndex = layer.load(1, {shade: [0.1,'#ccc']});
            $.get(uri, function (response) {
                layer.close(loadIndex);
                $("<div id='app_viewport'></div>").html(response).appendTo(panel);
                $(".icon-home").on("click", function () {
                    _k.goHome();
                });
                !!callback && callback();
                // _k.loading(true);
                $(".js_return").on("click", function () {
                    _k.goBack();
                });
            });
        },
        closeEntitySlider: function () {
            var $entitySlider = $("#entitySlider");
            var $wrapper = $("#loading-mask");
            $wrapper.hide();
            $entitySlider.removeClass("in");
            $wrapper.removeClass("map-mask");
            $entitySlider.removeClass("detail-sidebar")
        },
        drawDetailPanel: function (opt) {
            var _opt = {
                content: "",
                type:'',
                callback: function () { },
                submitBtn: "[hy-type-submit]",
                submit: function () { },
                animated: "in"
            };
            $.extend(_opt, opt);
            var $entitySlider = $("#entitySlider");
            var $wrapper = $("#loading-mask");
            var $taskWrapper = $("#task-wrapper").empty();
            $taskWrapper.css({ height: $(window).height() - 130 });
            var $btnClose = $("#btn-close"); 
            $btnClose.on("click", function () { 
                _k.closeEntitySlider();
            });
            if (_opt.type === 'map') {
                $entitySlider.addClass("detail-sidebar");
                $wrapper.addClass("map-mask");
                    
            }
            var sliderClass = function () {
                $wrapper.show();
                $entitySlider.addClass("in");
            }();

            //暂时这样简化处理权限
            var $div = $(_opt.content);
            _k.setPerm($div);
            _opt.content = $div[0].outerHTML;

            $taskWrapper.append(_opt.content); 
            var $from = $taskWrapper.find("form");
            $taskWrapper.find(_opt.submitBtn).on("click", function () {
                !!_opt.submit && _opt.submit($from);
            });
            !!_opt.callback && _opt.callback($from);
            /*
            $wrapper.on("click", function () { 
                _k.closeEntitySlider();
            });
            */
        },
        setPerm: function($div){
            //暂时这样简化处理权限
            //--------------------旧版
            /*$div.find("[role-type]").each(function () {
                var $this = $(this);
                var roles = $this.attr("role-type") || [];
                if (roles.indexOf(App.user.role) == -1) {
                    $this.remove();
                }
            });
            $div.find("[role-show]").each(function () {
                var $this = $(this);
                var roles = $this.attr("role-show") || [];
                if (roles.indexOf(App.user.role) == -1) {
                    var arr = roles.split(",");
                    if(arr.indexOf(""+(App.user.mchId||0)) == -1){
                        $this.remove();
                    }
                }
            });*/

            //--------------------新版
            $div.find("[PERM_ROLE]").each(function () {
                var $this = $(this);
                var action = $this.attr("PERM_ROLE") || "";
                if(App.actions.indexOf(action)==-1){
                    if($this.attr("hy-type-submit")===undefined && $this.attr("PERM_DISABLED")===undefined){
                        $this.remove();
                    }else{
                        $this.attr({"disabled":"disabled"}).addClass("disabled");
                    }
                }
            });
        },
        drawViewPanel: function (opt) {
            if (typeof (opt) === "undefined") return "对不起，未对该方法提供正确参数！"
            var $clientViewport = $("#client_viewport");
            $(".common_content").addClass("hidden_overflow");
            var width = $(window).width();
            var _opt = {
                isAjax: false,
                url: "home.html",
                callback: function () { },
                rmclick: function () { 
                    $("body").find('[ng-click]').unbind("click");
                },
                callbackParm: {},
                ux: "test.js",
                noscript: false,
                animated: "slideInRight"
            };
            $.extend(_opt, opt);
            var uri = _k.getWebPath + opt.url;
            uri.indexOf("?") == -1 ? (uri += ("?_v=" + new Date().getTime())) : (uri += ("&_v=" + new Date().getTime()));
            var panel = $("[main-container]").empty();
            var requiremodule = localStorage.getItem("RequireModule") ;
            requiremodule = typeof (requiremodule) === "undefined" ? _opt.ux : requiremodule; 
            var $requiremodule = $('[data-requiremodule="' + requiremodule + '"]');
            var newRequiremodule = _opt.ux += ("?module=" + new Date().getTime());
            if ($requiremodule.length > 0) { 
                $requiremodule.remove();
            }
            localStorage.setItem("RequireModule", newRequiremodule);
            $.ajax({
                url: uri,
                data: {},
                type: 'get',
                cache: false,
                dataType: 'html',
                success: function (response, textStatus) {
                    var _idRandom = "client_viewport_" + _k.reandom();
                    var $html = $("<div id=" + _idRandom + " class='clientviews  " + _opt.animated + "'></div>").html(response);
                    var parm = { id: _idRandom, };
                    $html.find(".js_back").on("click", function () {
                        _k.closeViewPanel(parm);
                        $clientViewport.show();
                    });
                    if(!!opt.css){
                        $html.css(opt.css);
                        $html.css("height",$(window).height() - 84);
                    }else if (/admin/.test(window.location.href)) {
                        $html.css({ height: $(window).height() - 100 });
                    }
                    $(panel).append($html);
                    $clientViewport.hide();
                    !!_opt.rmclick && _opt.rmclick();
                    if (!_opt.noscript) {
                        requirejs([newRequiremodule]);
                    }
                    $.extend(_opt.callbackParm, parm);
                    !!_opt.callback && _opt.callback(_opt.callbackParm);

                    _k.setPerm($html);
                    //暂时这样简化处理权限
                    //$("[role]").each(function () {
                    //    var $this = $(this);
                    //    var roles = $this.attr("role-type") || [];
                    //    if (roles.indexOf(App.user.role) == -1) {
                    //        $this.remove();
                    //    }
                    //});
                },
                error: function () {
                    var _opt = {
                        url: "/admin/themes/default/modules/error/404_tpl.html",
                        callback: function () { },
                        noscript: true
                    };
                    _k.drawViewPanel(_opt);
                }
            });
        },
        drawNavPanel: function (opt) { 
            if (typeof (opt) === "undefined") return "对不起，未对该方法提供正确参数！";
            var _opt = {
                isAjax: false,
                url: "index_tpl.html",
                callback: function () { },
                rmclick: function () {
                    $("body").find('[ng-click]').unbind("click");
                },
                callbackParm: {}
            }; 
            $.extend(_opt, opt);
            var uri = _k.getWebPath + opt.url;
            uri.indexOf("?") == -1 ? (uri += ("?_v=" + _k.reandom())) : (uri += ("&_v=" + _k.reandom()));
            var panel = $("[nav-container]").empty(); 
            $.ajax({
                url: uri,
                data: {},
                type: 'get',
                cache: false,
                dataType: 'html',
                success: function (response, textStatus) {
                    $(panel).append(response);
                    !!_opt.callback && _opt.callback(_opt.callbackParm);
                },
                error: function () {
                   
                }
            });
        },
        closeViewPanel: function (opt) {
            var $clientViewport = $("#client_viewport");
            if (typeof (_opt) !== "undefined") {
                var _opt = {
                    panelId: opt.id,
                };
                var $panelid = $("#" + _opt.panelId);
                $panelid.addClass("animated slideOutRight");
                setTimeout(function () {
                    $panelid.remove();
                }, 600);
            } else {
                $(".clientviews").remove();
            }
            $(".common_content").removeClass("hidden_overflow");
            $clientViewport.show();
        },
        dynamicCss: function (path) {
            if (!path || path.length === 0) {
                throw new Error('argument "path" is required !');
            }
            $.each(path, function (i, p) {
                p.indexOf("?") == -1 ? (p += ("?_v=" + _k.reandom())) : (p += ("&_v=" + _k.reandom()));
                var uri = _k.getWebPath + p;
                var head = document.getElementsByTagName('head')[0];
                var link = document.createElement('link');
                link.href = uri;
                link.rel = 'stylesheet';
                link.type = 'text/css';
                head.appendChild(link);
            });
        },
        dynamicJs: function (url, callback) {
            var head = document.getElementsByTagName('head')[0],
                js = document.createElement('script');
            js.setAttribute('type', 'text/javascript');
            js.setAttribute('src', url);
            head.appendChild(js);
            //执行回调
            var callbackFn = function () {
                if (typeof callback === 'function') {
                    callback();
                }
            };
            if (document.all) { //IE
                js.onreadystatechange = function () {
                    if (js.readyState == 'loaded' || js.readyState == 'complete') {
                        callbackFn();
                    }
                }
            } else {
                js.onload = function () {
                    callbackFn();
                }
            }
        },
        alert: function (opt, dt, at, fn) {
            var timer, _opt = {};
            if (typeof (opt) === "string") {
                _opt = {
                    text: opt,
                    time: dt || 3000,
                    autoClose: at || "Y",
                    callback: fn || function () { }
                }
            }
            if (typeof (opt) === "object") {
                $.extend(_opt, opt);
            }

            var white = _opt.white || false;
            var text = _opt.text || "ERROR";
            var time = _opt.time || 3000;
            var autoClose = _opt.autoClose || "Y";
            var viewTipid = 'viewTip_' + _k.reandom();
            var $tip = $('<div class="' + viewTipid + ' animated slideInDown cui-layer cui-toast "><div class="cui-layer-padding"><div class="cui-layer-content" tip-content></div></div></div>');
            var $tipbg = $('<div class="' + viewTipid + ' cui-mask"></div>');
            $tipbg.on("click", function () {
                window.clearTimeout(this.timer);
                $(".cui-mask").unbind("click");
                $('.' + viewTipid).remove();
            });
            if (autoClose === "Y") {
                this.timer = setTimeout(function () {
                    $('.' + viewTipid).remove();
                }, time);
            }
            $tip.find("[tip-content]").text(text);
            var panel = $("[app-view]");
            panel.append($tipbg);
            panel.append($tip);
        },
        strToDate: function (str) {
            var date = false;
            str = $.trim(str || "");
            try {
                date = new Date(str);
            } catch (e) { }

            if (!date || !date.getTime()) {
                date = new Date();
                str = str.split(/\s/);
                str[0] = (str[0] || "").split(/\-/);
                str[1] = (str[1] || "").split(/:/);
                date.setFullYear(str[0][0] || 0, str[0][1] - 1, str[0][2] || 0);
                date.setHours(str[1][0] || 0, str[1][1] || 0, str[1][2] || 0);
            }
            return date;
        },
        formatDate: function (ms, format) {
            /**
             * 格式化日期
             * @param ms		时间戳(以毫秒为单位)，也可以是date类型数据
             * @param format	[可选]时间格式,y:年 m:月 d:日 h:时 M:分 s:秒
             * 					默认是"y-m-d h:M:s" =>2013-01-06 17:37:31
             */
            var _d = $.type(ms) == "date" ? ms : new Date(Math.floor(ms));
            var weekday = ["日", "一", "二", "三", "四", "五", "六"];

            format = format ? format : "y-m-d h:M:s";
            var _add0 = function (n) { return n < 10 ? "0" + n : n; };
            var _ = {};
            _.y = _d.getFullYear();
            _.m = _add0(_d.getMonth() + 1);
            _.d = _add0(_d.getDate());
            _.h = _add0(_d.getHours());
            _.M = _add0(_d.getMinutes());
            _.s = _add0(_d.getSeconds());
            _.w = "星期" + weekday[_d.getDay()];

            $.each(_, function (k, v) { format = format.replace(k, v); });
            return format;
        },
        delegate: function (opt) {
            var $body = $("#" + App.viewObj.id);
            var _opt = {
                btntype: "",
                selector: "",
                body:undefined,
                callback: function () { }
            };
            // $body.undelegate(_opt.btntype, "click");
            $.extend(_opt, opt);
            if (_opt.callbackType === 'dropdown') {
                $body.delegate(_opt.btntype, "click", function (e) {
                    var s = $(e)[0].currentTarget;
                    var $ul = $(s).siblings();
                    $ul.find("a").unbind("click").on("click", function () {
                        var $tr = $(this).parents(_opt.selector || "tr:eq(0)");
                        var type = $(this).data('type');
                        var data = {
                            tr:$tr,
                            data:type
                        };
                        !!_opt.callback && _opt.callback(data);
                    });
                }); 
            } else {
                var fn = function (e) {
                    var $tr = $(e.target).parents(_opt.selector || "tr:eq(0)");
                    var data= $tr.data();
                    !!_opt.callback && _opt.callback(data, $tr);
                };
                $body =!!_opt.body ? _opt.body : $body;
                $body.delegate(_opt.btntype, "click", fn);
            }
        },
        store: function (key, val) {
            // 本地存储-localStorage
            return _st.storage('localStorage', key, val);
        },
        session: function (key, val) {
            // 本地存储-sessionStorage，可用于页面间传参
            return _st.storage('sessionStorage', key, val);
        },
        removeStoragesByKeyContain: function (keyval, type) {
            // 根据 key 值删除缓存
            var Storage = window[type || 'sessionStorage'];
            var key;
            // 如果不支持本地缓存，很可能是启用了『无痕模式』
            if (!Storage) {
                alert('为了能享受更好的服务，请关闭浏览器的无痕模式！');
                return;
            }
            while (Storage.length) {
                key = Storage.key(Storage.length - 1);
                if (key.indexOf(keyval) !== -1) {
                    Storage.removeItem(key);
                }
            }
        },
        ajax: function (options) {
            if (typeof (options) === "undefined") { return; };
            var _callback = options.callback;
            var _data = options.data || {};
            var _resUrl = options.resUrl || "";
            if (!(/http/.test(_resUrl))) {
                _resUrl = _k.getServicePath + _resUrl.replace(/^\//, "");
            }
            var _async = options.async===undefined ? true : options.async;
            var _type = options.type || "POST";
            var _cache = options.cache || "false";
            var _dataType = options.dataType || "json";
            var _contentType = options.contentType || "application/x-www-form-urlencoded";//application/json
            var _timeout = options.timeout || 180000;
            var _processData = options.processData || true;
            var _showLoadIcon = typeof (options.showLoadIcon) === "undefined";
            var _result;
            //trim data
            if(typeof _data == typeof {} && /list/i.test(_resUrl)){
                $.each(_data, function(_dk, _dv){
                    _data[_dk] = typeof _dv == "string" ? (_dv||"").trim() : _dv;
                });
            }
            $.ajax({
                async: _async,
                url: _resUrl,  //URL
                type: _type,
                cache: _cache,
                dataType: _dataType,
                processData : _processData,
                contentType: _contentType,
                xhrFields: {
                    withCredentials: true,
                    useDefaultXhrHeader: true
                },
                crossDomain: true,
                data: _data,
                timeout: _timeout,
                beforeSend: function () {
                },
                success: function (result, status, XMLHttpRequest) {
                    if (result != null) {
                        _result = result;
                        if ("99999" == result.code ) {
                            layer.msg(result.msg,{icon:0});
                            return;
                        }
                        !!_callback && _callback(_result);
                    }
                },
                complete: function (result, XMLHttpRequest, textStatus) {
                    if (_showLoadIcon) {
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    //0：请求未初始化（还没有调用 open()）。
                    //1：请求已经建立，但是还没有发送（还没有调用 send()）。
                    //2：请求已发送，正在处理中（通常现在可以从响应中获取内容头）。
                    //3：请求在处理中；通常响应中已有部分数据可用了，但是服务器还没有完成响应的生成。
                    //4：响应已完成；您可以获取并使用服务器的响应了。 
                    var msg = ['请求出错。',
                                '请求出错。',
                                '请求出错。',
                                '请求出错。',
                                '请求出错。'
                                ]
                    console.log("here" + XMLHttpRequest.readyState);
                }
            })
        },
        getHost: function() {
            return 'http://localhost:8080';
        }
    };
    var _i = {
        init: function () {
            var error = '[系统错误]页面未设置初始化入口';
            _k.alert(error, 10000);
            console.log(error)
        }
    };
    _win["App"] = _k;
    _win["App"].isWaitingTomcat = false;
    _win["Page"] = _i;
    //}, false);
})($, window);