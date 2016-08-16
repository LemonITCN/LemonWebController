package net.lemonsoft.lwc.core;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 子控制器的GUI操作界面 - 采用JAVAFX的Stage构建
 * Created by lemonsoft on 2016/8/14.
 */
public class SubControllerViewController implements Initializable, CoreController {

    public SubController subController;

    @FXML
    private Label currentSelectedLabel;
    @FXML
    private TableView rootTableView;

    @FXML
    public TextArea consoleOutputTextArea;
    @FXML
    private TextArea consoleInputTextArea;
    @FXML
    private Button consoleInputRunButton;
    @FXML
    private Button closeBrowserButton;

    private ObservableList<BrowserCellModel> data;

    private String selectedBrowserId;

    /**
     * 刷新UI显示，使其显示为最新的子控制器状态
     */
    public void refresh() {
        data = rootTableView.getItems();
        data.removeAll(data);
        for (Browser browser : subController.getBrowserPool().values()) {
            BrowserCellModel currentModel = new BrowserCellModel(browser);
            data.add(currentModel);
        }
        currentSelectedLabel.setText("Current selected: none!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selectedBrowserId = getCurrentSelectedBrowser().getId();
                currentSelectedLabel.setText(String.format("Current selected: %s", selectedBrowserId));
            }
        });
        final boolean[] shift_press = {false};
        final boolean[] enter_press = {false};
        consoleInputTextArea.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                switch (code){
                    case SHIFT:
                        shift_press[0] = true;
                        break;
                    case ENTER:
                        enter_press[0] = true;
                        break;
                    default:
                }
                if (shift_press[0] && enter_press[0])
                    consoleInputRun();// 触发shift-enter执行代码
            }
        });
        consoleInputTextArea.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                switch (code){
                    case SHIFT:
                        shift_press[0] = false;
                        break;
                    case ENTER:
                        enter_press[0] = false;
                        break;
                    default:
                }
            }
        });
    }

    public Browser getCurrentSelectedBrowser() {
        return subController.getBrowserById(data.get(rootTableView.getSelectionModel().selectedIndexProperty().getValue()).getId());
    }

    /**
     * 添加一个新的浏览器 - GUI调用
     */
    public void addBrowser() {
        subController.createBrowser();
        refresh();
    }

    /**
     * 关闭一个浏览器 - GUI调用
     */
    public void closeBrowser() {

    }

    /**
     * 运行出控制台输入的代码
     */
    public void consoleInputRun() {
        getCurrentSelectedBrowser().executeJavaScript(consoleInputTextArea.getText());
        consoleInputTextArea.setText("");// 置空代码输入区域
    }

    public class BrowserCellModel {
        private String id;
        private String url;
        private String title;
        private String visibility;

        public String getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public String getVisibility() {
            return visibility;
        }

        public BrowserCellModel(Browser browser) {
            this.id = browser.getId();
            this.url = browser.getCurrentUrl();
            this.title = browser.getCurrentTitle();
            this.visibility = browser.isShowing() ? "Yes" : "No";
        }
    }

}
