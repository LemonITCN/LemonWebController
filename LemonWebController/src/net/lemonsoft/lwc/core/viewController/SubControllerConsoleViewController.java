package net.lemonsoft.lwc.core.viewController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.lemonsoft.lwc.core.SubController;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.UUID;

/**
 * 子控制器的控制台
 * Created by LiuRi on 16/8/17.
 */
public class SubControllerConsoleViewController implements Initializable {

    public SubController belongSubController;
    private Stage consoleStage;
    private HashMap<String , Tty> ttyPools;// 控制台单位池

    @FXML
    private TabPane consoleTabPane;
    @FXML
    private TextField newTtyNameTextField;// 新Tty名称输入框

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ttyPools = new HashMap<>();
    }

    public Stage getConsoleStage() {
        return consoleStage;
    }

    public void setConsoleStage(Stage consoleStage) {
        this.consoleStage = consoleStage;
    }

    /**
     * 显示控制台GUI界面
     */
    public void show(){
        consoleStage.show();
    }

    /**
     * 隐藏控制台GUI界面
     */
    public void hide(){
        consoleStage.hide();
    }

    /**
     * 通过GUI创建一个TTY
     */
    public void createTtyWithGUI(){
        if (newTtyNameTextField.getText().length() > 0) {
            this.createATty(newTtyNameTextField.getText());
        }
    }

    /**
     * 添加一个TTY
     */
    public void createATty(String ttyName){
        Tty tty = new Tty(ttyName);
        Tab tab = new Tab(ttyName);
        tab.setContent(tty.getContainer());
        consoleTabPane.getTabs().add(tab);
    }

    /**
     * 控制台单位
     */
    public class Tty{
        private String id;
        private String name;
        private WebView container;

        public Tty(String name){
            super();
            this.id = UUID.randomUUID().toString();
            this.name = name;
            container = new WebView();
            container.getStyleClass().add("ttyWebView");
            container.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {

                }
            });
            container.getEngine().loadContent("<div style=''>Welcome use LemonWebController Console!</div><div><textarea></textarea></div>");
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public WebView getContainer() {
            return container;
        }

        public void setContainer(WebView container) {
            this.container = container;
        }
    }
}
