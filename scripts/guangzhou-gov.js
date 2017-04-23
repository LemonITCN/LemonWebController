JS页面跳转函数

var b = new Browser();
b.show();
b.operate.loadURL('http://www.gzg2b.gov.cn/_Layouts/ApplicationPages/News/News.aspx?ColumnID=6D13C10D-1BF3-4AA2-B739-CBDA18E17D33',function(){
    Log.success(JSON.stringify(b.executeJavaScript('document.body.innerHTML')));
    b.executeJavaScript('var theForm=document.forms[\'aspnetForm\'];if(!theForm){theForm=document.aspnetForm}function __doPostBack(eventTarget,eventArgument){if(!theForm.onsubmit||(theForm.onsubmit()!=false)){theForm.__EVENTTARGET.value=eventTarget;theForm.__EVENTARGUMENT.value=eventArgument;theForm.submit()}};');
    b.executeJavaScript('__doPostBack(\'ctl00$main$pagerHeader\',\'10\')');
},function(){

});

__doPostBack('ctl00$main$pagerHeader','10');