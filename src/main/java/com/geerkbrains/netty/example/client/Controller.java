package com.geerkbrains.netty.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void goToWindow2(ActionEvent actionEvent) {
        try {
            new Main();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
