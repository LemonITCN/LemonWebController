package net.lemonsoft.lwc.core;

import javafx.stage.Stage;

import java.util.*;

/**
 * 子控制器 - 每一个子控制器都会对应一个浏览器组，子控制器负责分发和协调各个浏览器的工作
 * Created by lemonsoft on 2016/8/14.
 */
public class SubController extends Stage{

    private String id;
    private Map<String , Browser> browserPool;
    private Map<String , BrowserCommand> browserCommandPool;
    private static SubControllerStage defaultStage = null;
    private MainManager belongMainManager;

    protected SubController(MainManager belongMainManager){
        super();
        this.id = UUID.randomUUID().toString();
        this.browserPool = new HashMap<>();
        this.browserCommandPool = new HashMap<>();
        this.belongMainManager = belongMainManager;
    }

    public String getId() {
        return id;
    }

    /**
     * 获取当前的浏览器池
     * @return 浏览器池信息Map<浏览器ID-浏览器标题>
     */
    public Map<String, String> getBrowserPoolInfo() {
        HashMap<String , String> infoMap = new HashMap<>();
        for (String bid : browserPool.keySet()){
            infoMap.put(bid , browserPool.get(bid).getTitle());
        }
        return infoMap;
    }

    /**
     * 获取当前子控制器中的浏览器数量
     * @return 当前子控制器的浏览器数量
     */
    public Integer countBrowsers(){
        return browserPool.size();
    }

    /**
     * 创建一个浏览器
     * @return 新创建的浏览器ID
     */
    public String createBrowser(){
        Browser browser = new Browser(this);
        browserPool.put(browser.getId() , browser);
        return browser.getId();
    }

    /**
     * 通过浏览器ID获取浏览器对象
     * @param id 要获取的浏览器id
     * @return 获取到的浏览器对象
     */
    public Browser getBrowserById(String id){
        return browserPool.get(id);
    }

    /**
     * 添加一个浏览器命令 - 通过命令对象添加
     * @param command 浏览器命令对象
     */
    public void addBrowserCommand(BrowserCommand command){
        this.browserCommandPool.put(UUID.randomUUID().toString(),command);
    }

    /**
     * 获取命令池
     * @return 获取命令池对象
     */
    public Map<String, BrowserCommand> getBrowserCommandPool() {
        return browserCommandPool;
    }

    /**
     * 添加一个浏览器命令 - 通过命令的实际内容来构建并添加
     * @param commandName 命令名称
     * @param commandIntroduce 命令介绍
     * @param commandCode 命令的执行JS代码
     */
    public void addBrowserCommand(String commandName , String commandIntroduce , String commandCode){
        BrowserCommand command = new BrowserCommand();
        command.setName(commandName);
        command.setIntroduce(commandIntroduce);
        command.setJavaScriptCode(commandCode);
        this.addBrowserCommand(command);
    }



    /**
     * 获取当前子控制器对应的GUI界面Stage
     * @return 对应的GUI Stage对象
     */
    public synchronized SubControllerStage getGUIStage(){
        if (defaultStage == null)
            defaultStage = new SubControllerStage(this);
        return defaultStage;
    }

    /**
     * 通知刷新GUI Stage界面
     */
    public void notifyRefreshGUIStage(){
        if (defaultStage != null)
            defaultStage.refresh();
    }
}
