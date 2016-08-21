package net.lemonsoft.lwc.core.ttyCommand;

import net.lemonsoft.lwc.core.SubController;

/**
 * 数据收集命令
 * Created by LiuRi on 16/8/21.
 */
public class DataCollectionCommand {

    private SubController belongSubController;

    public DataCollectionCommand(SubController controller) {
        super();
        this.belongSubController = controller;
    }

    /**
     * 将数据放置到数据收集池中
     *
     * @param key   数据的键
     * @param value 数据的值
     */
    public void put(String key, Object value) {
        belongSubController.putData(key, value);
    }

    /**
     * 获取指定的数据
     *
     * @param key 要获取的数据的键
     * @return 要获取的数据
     */
    public Object get(String key) {
        return belongSubController.getDataCollectionPool().get(key);
    }

    /**
     * 删除指定的数据
     *
     * @param key 要删除的数据的键
     * @return 删除的数据值
     */
    public Object remove(String key) {
        Object value = belongSubController.getDataCollectionPool().get(key);
        belongSubController.getDataCollectionPool().remove(key);
        return value;
    }

    /**
     * 移除所有的采集数据
     */
    public void removeAll() {
        belongSubController.getDataCollectionPool().clear();
    }

}
