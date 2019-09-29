package com.geerkbrains.netty.example.client;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import com.geerkbrains.netty.example.common.AbstractMessage;
import com.geerkbrains.netty.example.common.FileMessage;
import com.geerkbrains.netty.example.common.FileRequest;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainController implements Initializable {
    @FXML
    TextField tfFileName;

    @FXML
    ListView<String> filesList;

    @FXML
    ListView<String> serfilesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof FileMessage) {
                        FileMessage fm = (FileMessage) am;
                        Files.write(Paths.get("client_storage/" + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                        refreshLocalFilesList();
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                Network.stop();
            }
        });
        t.setDaemon(true);
        t.start();
 //       refreshLocalFilesList();
       // refreshServerFilesList();
    }

//    public void pressOnDownloadBtn(ActionEvent actionEvent) {
//        if (tfFileName.getLength() > 0) {
//            Network.sendMsg(new FileRequest(tfFileName.getText(),"down"));
//            tfFileName.clear();
//        }
//    }

    public void pressDellButton(ActionEvent actionEvent) throws IOException {
        if (tfFileName.getLength() > 0) {
            for (Object o : filesList.getItems()) {
                if(tfFileName.equals(o.toString())){
                    Files.list(Paths.get("client_storage")).map(p -> p.getFileName().toString()).forEach(p -> filesList.getItems().remove(p));
                }
            }
            tfFileName.clear();
        }
    }

    public void refreshLocalFilesList() {
        if (Platform.isFxApplicationThread()) {
            try {
                filesList.getItems().clear();
                Files.list(Paths.get("client_storage")).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(() -> {
                try {
                    filesList.getItems().clear();
                    Files.list(Paths.get("client_storage")).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    public void refreshServerFilesList(){
        if (Platform.isFxApplicationThread()) {
            try {
                serfilesList.getItems().clear();
                Files.list(Paths.get("server_storage")).map(p -> p.getFileName().toString()).forEach(o -> serfilesList.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(() -> {
                try {
                    serfilesList.getItems().clear();
                    Files.list(Paths.get("server_storage")).map(p -> p.getFileName().toString()).forEach(o -> serfilesList.getItems().add(o));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
