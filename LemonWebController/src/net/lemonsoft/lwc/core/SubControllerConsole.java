package net.lemonsoft.lwc.core;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 子控制器的控制台
 * Created by LiuRi on 16/8/17.
 */
public class SubControllerConsole implements Initializable {

    private SubController belongSubController;

    private SubControllerConsole(){super();}

    protected SubControllerConsole(SubController controller){
        super();
        this.belongSubController = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
