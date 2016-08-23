package net.lemonsoft.lwc.core.ttyCommand;

import net.lemonsoft.lwc.core.SubController;

/**
 * 通讯命令
 * Created by lemonsoft on 2016/8/23.
 */
public class CommunicationCommand {

    private SubController belongSubController;

    public CommunicationCommand(SubController controller){
        super();
        this.belongSubController = controller;
    }

    /**
     * 调用指定的通讯handler
     * @param name 通讯handler的名称
     * @param data 传输的数据
     */
    public Object call(String name , Object data){
        return belongSubController.callCommunicationHandler(name , data);
    }

}
