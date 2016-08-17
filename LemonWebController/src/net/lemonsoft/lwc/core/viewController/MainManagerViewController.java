package net.lemonsoft.lwc.core.viewController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import net.lemonsoft.lwc.core.MainManager;
import net.lemonsoft.lwc.core.SubController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 主管理器的GUI界面 - 使用JAVA FX的Stage构建
 * Created by lemonsoft on 2016/8/14.
 */
public class MainManagerViewController implements Initializable , CoreController {

    public MainManager mainManager;

    private ObservableList<SubControllerCellModel> data;

    @FXML
    private TableView<SubControllerCellModel> rootTableView;
    @FXML
    private Button destroySubControllerButton;
    @FXML
    private Button manageSubControllerButton;

    /**
     * 刷新GUI显示状态使其与主控制器对象的状态一致
     */
    public void refresh() {
        data = rootTableView.getItems();
        data.remove(0, data.size());
        for (SubController controller : mainManager.getSubControllerPool().values()) {
            data.add(data.size(), new SubControllerCellModel(controller));
        }
        destroySubControllerButton.setVisible(false);
        manageSubControllerButton.setVisible(false);
    }

    /**
     * 移除指定的子控制器 - GUI调用
     */
    public void removeSubController() {
        mainManager.getSubControllerPool().remove(getCurrentSelectedSubController().getId());
        refresh();
    }

    /**
     * 添加一个新的子控制器 - GUI调用
     */
    public void addSubController() {
        mainManager.createSubController();
        refresh();
    }

    /**
     * 管理这个子控制器 - GUI调用
     */
    public void manageSubController() {
        SubController subController = getCurrentSelectedSubController();
        Stage stage = subController.getGUIStage();
        stage.show();
    }

    /**
     * 获取当前选中的行对应的子控制器对象
     *
     * @return 对应的子控制器对象
     */
    public SubController getCurrentSelectedSubController() {
        return mainManager.getSubControllerPool().get(data.get(rootTableView.getSelectionModel().selectedIndexProperty().getValue()).getId());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SubControllerCellModel>() {
            @Override
            public void changed(ObservableValue<? extends SubControllerCellModel> observable, SubControllerCellModel oldValue, SubControllerCellModel newValue) {
                destroySubControllerButton.setVisible(true);
                manageSubControllerButton.setVisible(true);
            }
        });
    }

    /**
     * 子控制器行数据模型,用于GUI显示
     */
    public class SubControllerCellModel {
        private String id;
        private Integer browserCount;
        private Integer systemCommandCount;
        private Integer customCommandCount;

        public String getId() {
            return id;
        }

        public Integer getBrowserCount() {
            return browserCount;
        }

        public Integer getSystemCommandCount() {
            return systemCommandCount;
        }

        public Integer getCustomCommandCount() {
            return customCommandCount;
        }

        public SubControllerCellModel(SubController controller) {
            this.id = controller.getId();
            this.browserCount = controller.getBrowserPoolInfo().size();
            this.systemCommandCount = controller.getSystemCommandPool().size();
            this.customCommandCount = controller.getCustomCommandPool().size();
        }
    }

}
