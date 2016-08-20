/**
 * 浏览器对象
 * @constructor 调用Native创建一个浏览器对象
 */
function Browser() {
    var id = window.browser.create();

    /**
     * 显示这个浏览器
     */
    this.show = function () {
        Log.info("run show");
        try {
            window.browser.show(id);
        } catch (e){
            Log.error("e.message" + e.message);
        }
        Log.info("run show over");
    };

    /**
     * 隐藏这个浏览器
     */
    this.hide = function () {
        Log.info("run hide");
        window.browser.hide(id);
        Log.info("run hide over");
    };

    /**
     * 关闭这个浏览器
     */
    this.close = function () {
        window.browser.close(id);
    };

    /**
     * 设置浏览器窗口的大小尺寸
     * @param width 浏览器的窗口宽度
     * @param height 浏览器的窗口高度
     */
    this.setSize = function (width, height) {
        window.browser.setSize(id , width , height);
    };

    /**
     * 设置浏览器窗口的位置
     * @param x 新位置的x坐标
     * @param y 新位置的y坐标
     */
    this.setPosition = function (x, y) {
        window.browser.setPosition(id , x , y);
    };

    /**
     * 在浏览器中执行指定的js代码
     * @param jsCode
     */
    this.executeJavaScript = function (jsCode) {
        return window.browser.executeJavaScript(id , jsCode);
    };

    this.operate = new BrowserOperate(this);
    this.dataGet = new BrowserDataGet(this);

}