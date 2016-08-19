package net.lemonsoft.lwc.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.lemonsoft.lwc.core.viewController.SubControllerConsoleViewController;
import net.lemonsoft.lwc.core.viewController.SubControllerViewController;

import java.net.URL;
import java.util.*;

/**
 * 子控制器 - 每一个子控制器都会对应一个浏览器组，子控制器负责分发和协调各个浏览器的工作
 * Created by lemonsoft on 2016/8/14.
 */
public class SubController implements Core {

    private String id;
    private Stage defaultStage = null;
    private Map<String, Browser> browserPool;
    private Map<String, String> consoleOutputPool;// 控制台输出池
    private Map<String, Object> dataCollectionPool;// 数据收集池, 每个子控制器有一个独立的数据收集池,子控制器下所有的浏览器收集到的数据都会集中放到这个池中
    private Map<String, BrowserCommand> systemCommandPool;// 系统命令池
    private Map<String, BrowserCommand> customCommandPool;// 自定义命令池
    private MainManager belongMainManager;
    public SubControllerViewController viewController;
    private SubControllerConsoleViewController defaultConsole;
    private Stage defaultConsoleStage;// 默认的控制台stage

    private static final String FILE_NAME = "SubControllerStage";
    private static final String WINDOW_NAME = "SubController[GUI]";

    private static final String CONSOLE_FILE_NAME = "SubControllerConsoleStage";
    private static final String CONSOLE_WINDOW_NAME = "SubControllerConsoleViewController[GUI]";

    protected SubController(MainManager belongMainManager) {
        super();
        this.id = UUID.randomUUID().toString();
        this.browserPool = new HashMap<>();
        this.consoleOutputPool = new HashMap<>();
        this.dataCollectionPool = new HashMap<>();
        this.systemCommandPool = new HashMap<>();
        this.customCommandPool = new HashMap<>();
        this.belongMainManager = belongMainManager;
    }

    public String getId() {
        return id;
    }

    /**
     * 获取当前的浏览器池
     *
     * @return 浏览器池信息Map<浏览器ID-浏览器标题>
     */
    public Map<String, String> getBrowserPoolInfo() {
        HashMap<String, String> infoMap = new HashMap<>();
        for (String bid : browserPool.keySet()) {
            infoMap.put(bid, browserPool.get(bid).getTitle());
        }
        return infoMap;
    }

    public Map<String, Browser> getBrowserPool() {
        return browserPool;
    }

    /**
     * 获取当前子控制器中的浏览器数量
     *
     * @return 当前子控制器的浏览器数量
     */
    public Integer countBrowsers() {
        return browserPool.size();
    }

    /**
     * 创建一个浏览器
     *
     * @return 新创建的浏览器ID
     */
    public String createBrowser() {
        Browser browser = new Browser(this);
        browser.show();
        browserPool.put(browser.getId(), browser);
        refreshGUI();
        return browser.getId();
    }

    /**
     * 关闭指定id的浏览器
     * @param id 要关闭的浏览器的id
     */
    public void closeBrowserById(String id){
        Browser browser = browserPool.get(id);
        browser.close();
        browserPool.remove(id);
    }

    /**
     * 是否存在这个浏览器的ID
     * @param id 要判断是否存在的浏览器的ID
     * @return 是否存在这个浏览器的布尔值
     */
    public boolean containTheBrowserId(String id){
        return browserPool.containsKey(id);
    }

    /**
     * 通过浏览器ID获取浏览器对象
     *
     * @param id 要获取的浏览器id
     * @return 获取到的浏览器对象
     */
    public Browser getBrowserById(String id) {
        return browserPool.get(id);
    }

    /**
     * 添加一个浏览器命令 - 通过命令对象添加
     *
     * @param command 浏览器命令对象
     */
    public void addBrowserCommand(BrowserCommand command) {
        this.systemCommandPool.put(UUID.randomUUID().toString(), command);
        refreshGUI();
    }

    /**
     * 获取系统命令池
     *
     * @return 获取命令池对象
     */
    public Map<String, BrowserCommand> getSystemCommandPool() {
        return systemCommandPool;
    }

    /**
     * 获取自定义命令池
     *
     * @return 获取自定义命令池对象
     */
    public Map<String, BrowserCommand> getCustomCommandPool() {
        return customCommandPool;
    }

    /**
     * 添加一个浏览器命令 - 通过命令的实际内容来构建并添加
     *
     * @param commandName      命令名称
     * @param commandIntroduce 命令介绍
     * @param commandCode      命令的执行JS代码
     */
    public void addBrowserCommand(String commandName, String commandIntroduce, String commandCode) {
        BrowserCommand command = new BrowserCommand();
        command.setName(commandName);
        command.setIntroduce(commandIntroduce);
        command.setJavaScriptCode(commandCode);
        this.addBrowserCommand(command);
    }

    /**
     * 显示或隐藏指定浏览器ID对应的浏览器窗口
     *
     * @param browserId 要操作的浏览器的id
     */
    public void hideOrShowBrowser(String browserId) {
        Browser browser = getBrowserById(browserId);
        browser.setShowOrHide(!browser.isShowing());
    }

    /**
     * 获取当前子控制器对应的GUI界面Stage
     *
     * @return 对应的GUI Stage对象
     */
    public synchronized Stage getGUIStage() {
        if (defaultStage == null) {
            defaultStage = new Stage();
            try {
                String corePath = getClass().getResource("").toString();
                FXMLLoader loader = new FXMLLoader(new URL(String.format("%slayout/%s.fxml", corePath, FILE_NAME)));
                Scene rootScene = new Scene(loader.load());
                rootScene.getStylesheets().add(String.format("%sstylesheet/%s.css", corePath, FILE_NAME));
                defaultStage.setScene(rootScene);
                defaultStage.setTitle(String.format("%s - %s", WINDOW_NAME, this.getId()));
                defaultStage.setResizable(false);
                viewController = loader.getController();
                viewController.belongSubController = this;
                if (belongMainManager.getGUIStage() != null) {// 判断是否为空
                    defaultStage.initModality(Modality.WINDOW_MODAL);
                    defaultStage.initOwner(belongMainManager.getGUIStage());
                }
                this.refreshGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultStage;
    }

    /**
     * 创建一个控制台
     * @return 新创建出来的控制台对象
     */
    public SubControllerConsoleViewController getConsole(){
        if (defaultConsoleStage == null) {
            defaultConsoleStage = new Stage();
            defaultConsole = new SubControllerConsoleViewController();
            try {
                String corePath = getClass().getResource("").toString();
                FXMLLoader loader = new FXMLLoader(new URL(String.format("%slayout/%s.fxml", corePath, CONSOLE_FILE_NAME)));
                Scene rootScene = new Scene(loader.load());
                rootScene.getStylesheets().add(String.format("%sstylesheet/%s.css", corePath, CONSOLE_FILE_NAME));
                defaultConsoleStage.setScene(rootScene);
                defaultConsoleStage.setTitle(String.format("%s - %s", CONSOLE_WINDOW_NAME, this.getId()));
                defaultConsoleStage.setResizable(false);
                defaultConsole = loader.getController();
                defaultConsole.setConsoleStage(defaultConsoleStage);
                defaultConsole.belongSubController = this;
                this.refreshGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultConsole;
    }

    /**
     * 控制台输出指定的文本
     *
     * @param browserId 要进行输出控制台的浏览器id
     * @param info      要输出的信息
     */
    public void consoleOut(String browserId, String info) {
        this.consoleOutputPool.put(browserId, String.format("%s%s\n", consoleOutputPool.getOrDefault(browserId, ""), info));
        if (viewController != null) {
            viewController.browserOutputTextArea.setText(consoleOutputPool.get(browserId));
            viewController.browserOutputTextArea.setScrollTop(viewController.browserOutputTextArea.getPrefRowCount() * 100);// 控制其始终滚动到底部
        }
    }

    /**
     * 清空指定浏览器的控制台输出
     *
     * @param browserId 要清除控制台输出的浏览器id
     */
    public void cleanConsoleOutput(String browserId) {
        this.consoleOutputPool.put(browserId, String.format("%s%s\n", consoleOutputPool.getOrDefault(browserId, ""), ""));
        viewController.browserOutputTextArea.setText("");
    }

    /**
     * 获取指定的浏览器的当前加载的URL
     * @param browserId 要获取URL的浏览器的ID
     * @return 当前加载的URL字符串
     */
    public String getBrowserCurrentUrlByBrowserId(String browserId){
        return getBrowserById(browserId).getCurrentUrl();
    }

    /**
     * 获取指定的浏览器的当前的标题
     * @param browserId 要获取标题的浏览器的ID
     * @return 当前浏览器中加载的页面的标题
     */
    public String getBrowserCurrentTitleByBrowserId(String browserId){
        return getBrowserById(browserId).getCurrentTitle();
    }

    /**
     * 获取指定的浏览器是否现在被显示
     * @param browserId 要判断是否显示的浏览器id
     * @return 指定的浏览器现在是否显示的布尔值
     */
    public boolean getBrowserIsShowingByBrowserId(String browserId){
        return getBrowserById(browserId).isShowing();
    }

    @Override
    public void refreshGUI() {
        if (viewController != null)
            viewController.refresh();
    }
}
