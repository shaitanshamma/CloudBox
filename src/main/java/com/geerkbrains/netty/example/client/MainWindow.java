package com.geerkbrains.netty.example.client;

import com.geerkbrains.netty.example.client.protocol.NettyNetwork;
import com.geerkbrains.netty.example.common.ClientConnection;
import com.geerkbrains.netty.example.common.FileRequest;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindow extends Application implements Initializable {
    @Override
    public void start(Stage primaryStage) throws Exception {
        PasswordField passwordField = new PasswordField();
        TextField loginField = new TextField();
        Button button = new Button("Show Password");
        Label pass = new Label("pass");
        Label login = new Label("login");

        button.setOnAction((EventHandler<ActionEvent>) event -> {
            String password = passwordField.getText();
            pass.setText(password);
        });

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid, 400, 400);
        Text scenetitle = new Text("Cloud box");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);
        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        NettyNetwork.getInstance().start();
//                    }
//                }).start();
                NettyNetwork.currentChannel.writeAndFlush(new ClientConnection(userTextField.getText(), passwordField.getText()));
                if(ClientDownload.isOk){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
                Parent root = null;
                try {
                    root = fxmlLoader.load();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                primaryStage.setTitle("Box Client");
                Scene scene = new Scene(root);

                primaryStage.setScene(scene);
                primaryStage.show();

            }}
        });
        primaryStage.setTitle("Box Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                NettyNetwork.getInstance().start();
            }
        }).start();
    }
}

