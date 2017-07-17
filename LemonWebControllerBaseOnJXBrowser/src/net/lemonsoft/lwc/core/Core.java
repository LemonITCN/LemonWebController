package net.lemonsoft.lwc.core;

/**
 * 两个核心模块且存在GUI界面的类的父类
 * Created by LiuRi on 16/8/16.
 */
public interface Core {

    /**
     * 刷新图形化界面,当没有创建出图形化界面的时候实际没有执行任何操作
     */
    void refreshGUI();
}
