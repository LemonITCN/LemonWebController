function DataCollection() {
}

/**
 * 初始化csv表头数据列名
 * @param columns 列名csv字符串
 */
DataCollection.init = function (columns) {
    window.dataCollection.init(columns);
};

/**
 * 将指定的键值对数据放入到数据收集池中
 * @param key 要存储的数据的键
 * @param value 要存储的数据的值
 */
DataCollection.addRow = function (row) {
    window.dataCollection.addRow(row);
};

/**
 * 将数组中的元素拼成,分割的字符串，然后插入到数据收集池中
 * @param items
 */
DataCollection.addRowItems = function (items) {
    var row = '';
    for (var i = 0 ; i < items.length ; i ++){
        if (row.length > 0)
            row += ',';
        row += items[i];
    }
    window.dataCollection.addRow(row);
};

/**
 * 把缓存中的所有数据写出到本地文件
 */
DataCollection.flush = function () {
    window.dataCollection.flush();
};

// /**
//  * 将数据收集池中的指定数据取出
//  * @param key 要取出的数据的键
//  */
// DataCollection.get = function (index) {
//     return window.dataCollection.get(key);
// };

// /**
//  * 将数据收集池中的指定的数据删除
//  * @param key 要删除的数据的键
//  */
// DataCollection.remove = function (key) {
//     return window.dataCollection.remove(key);
// };

/**
 * 删除数据收集池中的所有数据
 */
DataCollection.removeAll = function () {
    window.dataCollection.removeAll();
};