package net.lemonsoft.lwc.core;

import javafx.stage.Stage;

/**
 * 主管理器的GUI界面 - 使用JAVA FX的Stage构建
 * Created by lemonsoft on 2016/8/14.
 */
public class MainManagerStage extends Stage {

    private MainManager manager;

    private MainManagerStage(){
        super();
    }

    protected MainManagerStage(MainManager manager){
        super();
        this.manager = manager;
    }

    /**
     * 刷新GUI显示状态使其与主控制器对象的状态一致
     */
    public void refresh(){

    }
}
