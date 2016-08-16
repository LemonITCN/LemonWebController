package net.lemonsoft.lwc;

import javafx.application.Application;
import javafx.stage.Stage;
import net.lemonsoft.lwc.core.MainManager;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MainManager manager = MainManager.defaultManager();
        manager.createSubController();
        manager.getGUIStage().show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
