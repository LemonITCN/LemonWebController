/**
 * 日志类 - 包含各种记录日志的方法
 */
function Log() {
}

/**
 * 日志模型
 * @param content 日志的内容
 * @param type 日志的类型
 * @constructor
 */
function LogModel(content , type) {
    this.content;
    this.type;
    this.time = Math.round(new Date().getTime());
}

/**
 * 输出一个日志 - 所有的日志输出方法都调用此方法,再通过日志类型来决定输出的日志种类
 * @param typeIndex 日志的类型的索引
 * @param info 打印日志的具体信息
 */
Log.msg = function (typeIndex, info) {
    var classArr = ["logItemSuccess" , "logItemError" , "logItemInfo" , "logItemWarning"];
    var typeArr = ["success" , "error" , "info" , "warning"];
    try {
        window.log.addLog(JSON.stringify(new LogModel(info , typeArr[typeIndex])));
        var logElement = document.createElement("div");
        logElement.setAttribute("class", classArr[typeIndex]);
        logElement.innerText = "[LOG]" + info;
        document.getElementById("logList").appendChild(logElement);
    } catch (e){
        console.log("No GUI to log:" + info);
    }
};

/**
 * 打印成功的日志
 * @param info 要打印的日志内容
 */
Log.success = function (info) {
    this.msg(0 , info);
};

/**
 * 打印错误信息的日志
 * @param info 要打印的日志内容
 */
Log.error = function (info) {
    this.msg(1 , info);
};

/**
 * 打印普通的信息日志
 * @param info 要打印的日志内容
 */
Log.info = function (info) {
    this.msg(2 , info);
};

/**
 * 打印警告信息的日志
 * @param info 要打印的日志内容
 */
Log.warning = function (info) {
    this.msg(3 , info);
};