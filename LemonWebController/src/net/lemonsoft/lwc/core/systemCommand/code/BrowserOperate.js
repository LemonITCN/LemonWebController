function BrowserOperate(browserObj) {
    var browser = browserObj;

    /**
     * 加载URL
     * @param url 要加载的URL
     */
    this.loadURL = function (url , successCallback , failedCallback) {
        window.browser.loadURL(browser.getId() , url , successCallback , failedCallback);
    };

    /**
     * 设置指定Dom元素的Attribute参数
     * @param domSelector 要设置Attribute参数的Dom元素的css选择器
     * @param attributeName 要设置的Attribute参数名
     * @param attributeValue 要设置的Attribute参数的值
     */
    this.setDomAttribute = function (domSelector , attributeName , attributeValue) {
        browser.executeJavaScript("document.querySelector('" + domSelector + "').setAttribute('" + attributeName + "' , '" + attributeValue +"')");
    };

    /**
     * 设置指定Dom元素的属性
     * @param domSelector 要设置的属性的Dom元素的css选择器
     * @param propertyName 要设置的属性名
     * @param propertyValue 要设置的属性值
     */
    this.setDomProperty = function (domSelector , propertyName , propertyValue) {
        browser.executeJavaScript("document.querySelector('" + domSelector + "')." + propertyName + " = " + "'" + propertyValue + "'");
    };

    /**
     * 触发指定Dom元素的指定函数
     * @param domSelector 要触发对应函数的Dom元素
     * @param functionName 要触发指定Dom元素的函数名称
     * @param attributes 要传入的参数列表,数组类型
     */
    this.invokeDomFunction = function (domSelector, functionName , attributes) {
        browser.executeJavaScript("document.querySelector('" + domSelector + "')." + functionName + "(" + attributes.join(",") + ")");
    };

    /**
     * 触发指定Dom元素的鼠标事件,如点击/鼠标悬浮等操作
     * @param domSelector 要触发鼠标事件的Dom元素
     * @param actionName 要触发的鼠标事件名称  ‘click’, ‘mousedown’, ‘mousemove’, ‘mouseout’, ‘mouseover’, ‘mouseup’.
     */
    this.invokeDomMouseAction = function (domSelector , actionName) {
        var code = "var _tevent = document.createEvent('MouseEvents');_tevent.initMouseEvent('" + actionName + "',true,true,window,0,0,0,0,0,false,false,false,false,0,null);document.querySelector('" + domSelector + "').dispatchEvent(_tevent);";
        browser.executeJavaScript(code);
    };

    /**
     * 在指定的Input文本控件中填写值
     * AID: 1b
     *
     * @param domSelector 要填写值的文本输入控件的css选择器字符串
     * @param fillText 要填写的文本值
     */
    this.fillInputText = function(domSelector , fillText) {
        this.setDomAttribute(domSelector , "value" , fillText);
        Log.success("在指定的Input文本控件中填写值成功");
    };

    /**
     * 选择指定的下拉菜单的下拉项目
     * AID: 1c
     *
     * @param domSelector 要选择项目的css选择器字符串
     * @param selectedIndex 要让其选择的项目的索引
     */
    this.selectItem = function(domSelector , selectedIndex) {
        this.setDomProperty("selectedIndex" , selectedIndex);
        Log.success("选择指定的下拉菜单的下拉项目成功");
    };

    /**
     * 选中指定radio或checkbox
     * AID: 1d
     *
     * @param domSelector 要选择项目的css选择器字符串
     * @param checked 设置是选中还是取消选中,boolean值
     */
    this.checkedItem = function(domSelector , checked) {
        this.setDomProperty("checked" , checked ? "checked" : "");
        Log.success("选中指定radio或checkbox成功");
    };

    /**
     * 触发提交指定的表单
     * AID: 1e
     *
     * @param domSelector 要提交的表单的css选择器字符串
     */
    this.submitForm = function (domSelector) {
        this.invokeDomFunction(domSelector , "submit" , []);
        Log.success("触发提交指定的表单成功");
    };

    /**
     * 休眠指定的时间之后再继续执行
     * AID: 1f
     *
     * @param sleepInterval 休眠的时间,单位ms
     */
    this.sleep = function (sleepInterval) {
        Log.info("休眠" + sleepInterval + " 开始");
        Util.sleep(sleepInterval);
        Log.info("休眠" + sleepInterval + " 结束");
    };

    /**
     * 让当前页面滚动到指定的位置
     * AID: 1g
     *
     * @param x 让页面要滚动到的位置的x坐标
     * @param y 让页面要滚动到的位置的y坐标
     */
    this.scrollToPosition = function (x , y) {
        browser.executeJavaScript("window.scrollTo(" + x + " , " + y + ")");
        Log.success("让当前页面滚动到指定的位置成功");
    };

    /**
     * 点击指定的Dom元素
     * AID: 1h
     *
     * @param domSelector 要点击的Dom元素的css选择器字符串
     */
    this.clickDom = function (domSelector) {
        this.invokeDomMouseAction(domSelector , "click");
        Log.success("点击指定的Dom元素成功");
    };

    /**
     * 触发指定Dom元素的鼠标悬浮事件
     * AID: 1i
     *
     * @param domSelector 要触发hover事件的dom元素的css选择器字符串
     */
    this.mouseoverDom = function (domSelector) {
        this.invokeDomMouseAction(domSelector , "mouseover");
        Log.success("触发指定Dom元素的鼠标悬浮事件成功");
    };

}