/**
 * 通讯类 - 用于tty的命令与
 * @constructor
 */
function Communication() {
}

/**
 * 调用指定的通讯handler
 * @param name 通讯handler的名称
 * @param data 通讯handler携带的数据
 * @returns {*} 原生代码返回的对象
 */
Communication.call = function (name, data) {
    return window.communication.call(name , data);
};

