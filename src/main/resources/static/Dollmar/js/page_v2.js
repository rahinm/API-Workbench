 /*
    Included using page class file
    Includes DYN_WEB.Event with domReady (DON'T REMOVE - code pages use for init)
    checkTmplHeight (push footer down on short pages)
    Break out of iframes...
*/

// break out of iframe
if ( top !== self ) {
    //top.location = self.location; // doesn't work anymore (permission denied)
    self.location = 'about:blank'; // triggers errors below (no ids)
}


// DYN_WEB is namespace used for code from dyn-web.com
// replacing previous use of dw_ prefix for object names
var DYN_WEB = DYN_WEB || {};

/*
    dw_event.js - version date May 2013 (added .domReady)
    .domReady is whenReady fn (slightly modified) from JavaScript the Definitive Guide
    6th edition by David Flanagan, example 17.01
*/
// DON'T REMOVE!!! old version doesn't have domReady and code pages use that for init
DYN_WEB.Event=(function(Ev){Ev.add=document.addEventListener?function(obj,etype,fp,cap){cap=cap||false;obj.addEventListener(etype,fp,cap);}:function(obj,etype,fp){obj.attachEvent('on'+etype,fp);};Ev.remove=document.removeEventListener?function(obj,etype,fp,cap){cap=cap||false;obj.removeEventListener(etype,fp,cap);}:function(obj,etype,fp){obj.detachEvent('on'+etype,fp);};Ev.DOMit=function(e){e=e?e:window.event;if(!e.target){e.target=e.srcElement;}if(!e.preventDefault){e.preventDefault=function(){e.returnValue=false;return false;};}if(!e.stopPropagation){e.stopPropagation=function(){e.cancelBubble=true;};}return e;};Ev.getTarget=function(e){e=Ev.DOMit(e);var tgt=e.target;if(tgt.nodeType!==1){tgt=tgt.parentNode;}return tgt;};Ev.domReady=(function(){var funcs=[];var ready=false;function handler(e){if(ready){return;}if(e.type==="readystatechange"&&document.readyState!=="complete"){return;}for(var i=0,len=funcs.length;i<len;i++){funcs[i].call(document);}ready=true;funcs=[];}if(document.addEventListener){document.addEventListener("DOMContentLoaded",handler,false);document.addEventListener("readystatechange",handler,false);window.addEventListener("load",handler,false);}else if(document.attachEvent){document.attachEvent("onreadystatechange",handler);window.attachEvent("onload",handler);}return function whenReady(f){if(ready){f.call(document);}else{funcs.push(f);}};})();return Ev;}(DYN_WEB.Event||{}));


DYN_WEB.Util = (function( Ut ) {
    
    Ut.getWindowDims = function() {
        var doc = document, w = window;
        var docEl = (doc.compatMode && doc.compatMode === 'CSS1Compat')? doc.documentElement: doc.body;
        
        var width = docEl.clientWidth;
        var height = docEl.clientHeight;
        
        // mobile zoomed in?
        if ( w.innerWidth && width > w.innerWidth ) {
            width = w.innerWidth;
            height = w.innerHeight;
        }
        
        return {width: width, height: height};
    }
    
    return Ut;

}( DYN_WEB.Util || {} ) );


(function() {
    
    var Ev = DYN_WEB.Event;
    var Ut = DYN_WEB.Util;
    
    // to push footer down on short pages
    function checkTmplHeight() {
        var ht = Ut.getWindowDims().height;
        
        // to subtract height of header, footer, and main padding
        var diff = document.getElementById('header').offsetHeight +
                document.getElementById('footer').offsetHeight + 37;
        
        document.getElementById('main').style.minHeight = ht - diff + "px";
    }
    checkTmplHeight.timer = null;
    
    Ev.domReady( checkTmplHeight );
    Ev.add(window, 'load', checkTmplHeight);
    
    Ev.add(window, 'resize', function() {
        if ( checkTmplHeight.timer ) {
            clearTimeout( checkTmplHeight.timer );
        }
        checkTmplHeight.timer = setTimeout( checkTmplHeight, 100 );
    });
    
}());


// contact 
function dw_getAddy(txt) {
    var usr = 'shpdynweb', srv = 'gmail', tld = 'com';
    var link = '<a class="mail" href="mailto:' + usr + '&#64;' + srv + '.' + tld + '">' + txt +'<\/a>';
    var show = ' <span class="addy">(' + usr + ' at ' + srv + ' dot ' + tld + ')<\/span>';
    document.write(link + show);
}

