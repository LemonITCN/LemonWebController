package net.lemonsoft.lwc.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 主管理器 - 负责管理所有的子控制器，以及各子控制器相关指令的分发
 * Created by lemonsoft on 2016/8/14.
 */
public class MainManager {

    /**
     * 子控制器池 - 所有的自控制器在创建的时候都会有一个唯一的UUID作为标识
     */
    private Map<String , SubController> subControllerPool;
    private static MainManager defaultManager = null;
    private static MainManagerStage defaultStage = null;

    public MainManager(){
        super();
        subControllerPool = new HashMap<>();
    }

    /**
     * 获取默认的主管理器
     * @return 当前默认的主管理器
     */
    public synchronized MainManager defaultManager(){
        if (defaultManager == null)
            defaultManager = new MainManager();
        return defaultManager;
    }

    /**
     * 创建一个子控制器
     * @return 新创建的子控制器的id
     */
    public String createSubController(){
        SubController subController = new SubController(this);
        subControllerPool.put(subController.getId() , subController);
        return subController.getId();
    }

    /**
     * 获取指定id的子控制器
     * @param id 要获取的子控制器的ID
     * @return 获取到的子控制器对象
     */
    public SubController getSubControllerById(String id){
        return subControllerPool.getOrDefault(id , null);
    }

    /**
     * 获取当前子控制器池中的子控制器数量
     * @return 子控制器的数量
     */
    public Integer countSubControllers(){
        return subControllerPool.size();
    }

    /**
     * 获取主管理器的GUIStage对象
     * @return 获取当前主管理器的GUI界面
     */
    public synchronized MainManagerStage getGUIStage(){
        if (defaultStage == null)
            defaultStage = new MainManagerStage(this);
        return defaultStage;
    }

}
