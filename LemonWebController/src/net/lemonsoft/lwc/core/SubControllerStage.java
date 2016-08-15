package net.lemonsoft.lwc.core;

import javafx.stage.Stage;

/**
 * 子控制器的GUI操作界面 - 采用JAVAFX的Stage构建
 * Created by lemonsoft on 2016/8/14.
 */
public class SubControllerStage extends Stage {

    private SubController controller;

    private SubControllerStage(){
        super();
    }

    /**
     * 通过子控制器对象来创建子控制器对象的GUI
     * @param controller 子控制器对象
     */
    protected SubControllerStage(SubController controller){
        super();
        this.controller = controller;
    }

    /**
     * 刷新UI显示，使其显示为最新的子控制器状态
     */
    public void refresh(){

    }

}
