package com.geerkbrains.netty.example.client;

import com.geerkbrains.netty.example.client.protocol.NettyNetwork;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//public class MainClient extends Application {
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
//        Parent root = fxmlLoader.load();
//        primaryStage.setTitle("Box Client");
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) throws Exception {
//        launch(args);
//    }
//}
