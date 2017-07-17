package net.lemonsoft.lwc.core.ttyCommand;

import net.lemonsoft.lwc.core.SubController;

/**
 * Created by lemonsoft on 2016/8/23.
 */
public class LogCommand {

    private SubController belongSubController;

    public LogCommand(SubController subController){
        super();
        this.belongSubController = subController;
    }

    /**
     * 添加一个log
     * @param logStr
     */
    public void addLog(String logStr){
        belongSubController.addLog(logStr);
    }

    /**
     * 通过索引移除log
     * @param index 要移除的log的索引
     */
    public void removeByIndex(Integer index){
        belongSubController.removeLogByIndex(index);
    }

    /**
     * 移除所有的log
     */
    public void removeAllLog(){
        belongSubController.removeAllLogs();
    }

}
