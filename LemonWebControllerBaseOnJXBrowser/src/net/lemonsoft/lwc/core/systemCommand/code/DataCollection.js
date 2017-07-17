function DataCollection() {
}

/**
 * 将指定的键值对数据放入到数据收集池中
 * @param key 要存储的数据的键
 * @param value 要存储的数据的值
 */
DataCollection.put = function (key, value) {
    window.dataCollection.put(key , value);
};

/**
 * 将数据收集池中的指定数据取出
 * @param key 要取出的数据的键
 */
DataCollection.get = function (key) {
    return window.dataCollection.get(key);
};

/**
 * 将数据收集池中的指定的数据删除
 * @param key 要删除的数据的键
 */
DataCollection.remove = function (key) {
    return window.dataCollection.remove(key);
};

/**
 * 删除数据收集池中的所有数据
 */
DataCollection.removeAll = function () {
    window.dataCollection.removeAll();
};