function Util() {
}

/**
 * 创建一个随机的UUID
 * @returns {string} 生成的UUID字符串
 */
Util.createUUID = function () {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
};

/**
 * 获取当前的系统时间戳
 * @returns {number} 当前的UNIX时间戳
 */
Util.getUnixTimeStamp = function () {
    return Math.round(new Date().getTime());
};

/**
 * 休眠(阻塞)指定的时间
 * @param timeInterval 要休眠(阻塞)的时间,单位ms
 */
Util.sleep = function (timeInterval) {
    var now = new Date();
    var exitTime = now.getTime() + timeInterval;
    while (true) {
        now = new Date();
        if (now.getTime() > exitTime)
            return;
    }
};