package net.lemonsoft.lwc.core.viewController;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import net.lemonsoft.lwc.core.SubController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 子控制器数据收集池视图控制器
 * Created by LiuRi on 16/8/21.
 */
public class SubControllerDataCollectionViewController implements Initializable {

    private SubController belongSubController;

    private ObservableList<DataCellModel> data;

    @FXML
    private TableView<DataCellModel> rootTableView;
    @FXML
    private TextField dataKeyField;
    @FXML
    private TextField dataValueField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public SubController getBelongSubController() {
        return belongSubController;
    }

    public void setBelongSubController(SubController belongSubController) {
        this.belongSubController = belongSubController;
    }

    /**
     * 刷新
     */
    public void refresh() {
        data = rootTableView.getItems();
        data.removeAll(data);
//        for (String key : belongSubController.getDataCollectionPool().keySet()){
//            data.add(new DataCellModel(key , belongSubController.getDataCollectionPool().get(key)));
//        }
    }

    /**
     * 添加数据 - GUI调用
     */
    public void putData() {
        belongSubController.addRow(dataValueField.getText());
//        belongSubController.putData(dataKeyField.getText() , dataValueField.getText());
    }

    /**
     * 删除选中的数据
     */
    public void removeSelected() {
//        DataCellModel dataCellModel = data.get(rootTableView.getSelectionModel().getSelectedIndex());
//        belongSubController.removeData(dataCellModel.getKey());
    }

    /**
     * 删除所有的数据
     */
    public void removeAll() {
        belongSubController.removeAllData();
    }

    /**
     * 数据行模型
     */
    public class DataCellModel {
        private String key;
        private Object value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public DataCellModel(String key , Object value){
            super();
            this.key = key;
            this.value = value;
        }
    }
}
