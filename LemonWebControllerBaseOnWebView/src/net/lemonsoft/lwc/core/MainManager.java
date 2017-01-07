package net.lemonsoft.lwc.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.lemonsoft.lwc.core.viewController.MainManagerViewController;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 主管理器 - 负责管理所有的子控制器，以及各子控制器相关指令的分发
 * Created by lemonsoft on 2016/8/14.
 */
public class MainManager implements Core {

    /**
     * 子控制器池 - 所有的自控制器在创建的时候都会有一个唯一的UUID作为标识
     */
    private Map<String, SubController> subControllerPool;
    private String id;
    private static MainManager defaultManager = null;
    private Stage defaultStage = null;
    private MainManager self;
    private MainManagerViewController viewController;

    private static final String FILE_NAME = "MainManagerStage";
    private static final String WINDOW_NAME = "MainManager[GUI]";

    public MainManager() {
        super();
        subControllerPool = new HashMap<>();
        this.id = UUID.randomUUID().toString();
        MainManagerPool.addMainManager(this);
    }

    /**
     * 获取默认的主管理器
     *
     * @return 当前默认的主管理器
     */
    public static synchronized MainManager defaultManager() {
        if (defaultManager == null)
            defaultManager = new MainManager();
        return defaultManager;
    }

    /**
     * 创建一个子控制器
     *
     * @return 新创建的子控制器的id
     */
    public SubController createSubController() {
        SubController subController = new SubController(this);
        subControllerPool.put(subController.getId(), subController);
        return subController;
    }

    /**
     * 获取子控制器池
     *
     * @return 子控制器池
     */
    public Map<String, SubController> getSubControllerPool() {
        return subControllerPool;
    }

    /**
     * 获取指定id的子控制器
     *
     * @param id 要获取的子控制器的ID
     * @return 获取到的子控制器对象
     */
    public SubController getSubControllerById(String id) {
        return subControllerPool.getOrDefault(id, null);
    }

    /**
     * 获取当前子控制器池中的子控制器数量
     *
     * @return 子控制器的数量
     */
    public Integer countSubControllers() {
        return subControllerPool.size();
    }

    /**
     * 获取主管理器的GUIStage对象
     *
     * @return 获取当前主管理器的GUI界面
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
                viewController.mainManager = this;
                this.refreshGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return defaultStage;
    }

    /**
     * 获取主管理器的标识符id
     *
     * @return 主管理器的id
     */
    public String getId() {
        return id;
    }

    @Override
    public void refreshGUI()  {
        if (this.viewController != null)
            this.viewController.refresh();
    }
}
