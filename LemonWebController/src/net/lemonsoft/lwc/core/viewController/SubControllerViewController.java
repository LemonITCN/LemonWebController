package net.lemonsoft.lwc.core.viewController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.lemonsoft.lwc.core.Browser;
import net.lemonsoft.lwc.core.SubController;

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
    @FXML
    private Button hideOrShowButton;

    private ObservableList<BrowserCellModel> data;

    private String selectedBrowserId;

    /**
     * 刷新UI显示，使其显示为最新的子控制器状态
     */
    public void refresh() {
        data = rootTableView.getItems();
        data.removeAll(data);
        currentSelectedLabel.setText("Current selected: none!");
        int i = 0;
        for (Browser browser : subController.getBrowserPool().values()) {
            BrowserCellModel currentModel = new BrowserCellModel(browser);
            data.add(currentModel);
            if (browser.getId().equals(selectedBrowserId)) {
                TableView.TableViewSelectionModel singleSelectionModel = rootTableView.getSelectionModel();
                singleSelectionModel.select(i);
                setShowOrHideButtonTitle(browser.isShowing());
            }
            i++;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                Browser browser = getCurrentSelectedBrowser();
                selectedBrowserId = browser.getId();
                currentSelectedLabel.setText(String.format("Current selected: %s", selectedBrowserId));
                setShowOrHideButtonTitle(browser.isShowing());
            }
        });
        final boolean[] shift_press = {false};
        final boolean[] enter_press = {false};
        consoleInputTextArea.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                switch (code) {
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
                switch (code) {
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
        String browserId = subController.createBrowser();
        if (subController.countBrowsers() == 1)
            selectedBrowserId = browserId;
    }

    /**
     * 关闭一个浏览器 - GUI调用
     */
    public void closeBrowser() {

    }

    /**
     * 设置tableView选中指定的浏览器ID对应的行
     *
     * @param browserId 要设置选中行的浏览器id
     */
    public void setCurrentSelectRowByBrowserId(String browserId) {
        int i = 0;
        for (Browser browser : subController.getBrowserPool().values()) {
            if (browser.getId().equals(selectedBrowserId)) {
                TableView.TableViewSelectionModel singleSelectionModel = rootTableView.getSelectionModel();
                singleSelectionModel.select(i);
                break;
            }
            i++;
        }
    }

    /**
     * 显示或者关闭浏览器 - GUI调用
     */
    public void hideOrShowBrowser() {
        subController.hideOrShowBrowser(selectedBrowserId);
    }

    /**
     * 运行出控制台输入的代码
     */
    public void consoleInputRun() {
        getCurrentSelectedBrowser().executeJavaScript(consoleInputTextArea.getText());
        consoleInputTextArea.setText("");// 置空代码输入区域
    }

    /**
     * 根据当前指定的浏览器是否处于显示状态来设置显示隐藏按钮的标题
     *
     * @param isShow 当前对应的浏览器是否已经显示的布尔值
     */
    private void setShowOrHideButtonTitle(boolean isShow) {
        hideOrShowButton.setText(String.format("%s the browser", isShow ? "Hide" : "Show"));
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
