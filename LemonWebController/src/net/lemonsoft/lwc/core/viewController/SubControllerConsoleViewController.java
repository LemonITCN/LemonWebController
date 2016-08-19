package net.lemonsoft.lwc.core.viewController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.lemonsoft.lwc.core.SubController;

import java.net.URL;
import java.util.*;

/**
 * 子控制器的控制台
 * Created by LiuRi on 16/8/17.
 */
public class SubControllerConsoleViewController implements Initializable {

    public SubController belongSubController;
    private Stage consoleStage;
    private HashMap<String , Tty> ttyPool;// 控制台单位池
    private List<String> ttyList;// 控制台单位列表，用于记录TTY ID与GUI界面的顺序对应

    @FXML
    private TabPane consoleTabPane;
    @FXML
    private TextField newTtyNameTextField;// 新Tty名称输入框

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ttyPool = new HashMap<>();
        ttyList = new ArrayList<>();
        // create a default tty
        createATty("defaultTTY");
        consoleTabPane.getTabs().get(0).setClosable(false);
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
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"");
            alert.setTitle("Create new tty error");
            alert.getDialogPane().setContentText("Can not create new tty, because you have not entered the name of the TTY!");
            alert.showAndWait();
        }
    }

    /**
     * 添加一个TTY
     */
    public Tty createATty(String ttyName){
        Tty tty = new Tty(ttyName);
        Tab tab = new Tab(ttyName);
        tab.setContent(tty.getContainer());
        consoleTabPane.getTabs().add(tab);
        ttyPool.put(tty.getId() , tty);
        tab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {// 设置TTY的GUI关闭事件
                removeTtyById(tty.getId());
            }
        });
        return tty;
    }

    /**
     * 通过TTYID移除tty
     * @param ttyId 要移除的TTYid
     */
    public void removeTtyById(String ttyId){
        Tty tty = ttyPool.get(ttyId);
        tty.container.getEngine().load("http://");
        ttyPool.remove(ttyId);
        int index = ttyList.indexOf(ttyId);
        consoleTabPane.getTabs().remove(index);
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
            System.out.println(getClass().getResource("../") + "layout/SubControllerConsoleTty.html");
            container.getEngine().load(getClass().getResource("../") + "layout/SubControllerConsoleTty.html");
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
