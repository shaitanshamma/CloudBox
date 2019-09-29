package com.geerkbrains.netty.example.client;

import com.geerkbrains.netty.example.client.protocol.NettyNetwork;
import com.geerkbrains.netty.example.common.FileMessage;
import com.geerkbrains.netty.example.common.FileRequest;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable {


//
//    public Main(Main controller) {
//        this.controller = controller;
//    }
//
////
//    public static Main getController(){
//        return controller;
//    }

    @FXML
    TextField tfFileName;

    @FXML
    ListView<String> filesList;

    @FXML
    ListView<String> serfilesList;

    String fileName;


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


//    public void run() {
////        Thread t = new Thread(() -> {
////            try {
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            Bootstrap clientBootstrap = new Bootstrap();
//            clientBootstrap.group(group);
//            clientBootstrap.channel(NioSocketChannel.class);
//            //clientBootstrap.remoteAddress(new InetSocketAddress("localhost", 8189));
//            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
//                protected void initChannel(SocketChannel socketChannel) throws Exception {
//                    socketChannel.pipeline().addLast(
//                            new ObjectDecoder(50 * 1024 * 1024, ClassResolvers.cacheDisabled(null)),
//                            new ObjectEncoder(),
//                            new ClientDownload());
//                    setCurrentChannel(socketChannel);
//                    System.out.println(currentChannel + " rfyfk");
//                }
//            });
//            ChannelFuture channelFuture = clientBootstrap.connect("localhost", 8189).sync();
//            channelFuture.channel().closeFuture().sync();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                group.shutdownGracefully().sync();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//            } finally {
//
//            }
//        });
//            t.setDaemon(true);
//            t.start();
//    }

    public void refreshList(ActionEvent actionEvent) {
        refreshLocalFilesList();
        refreshServerFilesList();

    }

    public void selection() {
        fileName = null;
        MultipleSelectionModel<String> langsSelectionModel = filesList.getSelectionModel();
        MultipleSelectionModel<String> langsSelectionModel2 = serfilesList.getSelectionModel();
        langsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>() {
            String str = " ";

            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue) {
                str = newValue;
                setFileName(str);
                tfFileName.clear();
                tfFileName.appendText(str);
                System.out.println("at client" + fileName);

            }
        });
        langsSelectionModel2.selectedItemProperty().addListener(new ChangeListener<String>() {
            String str = " ";

            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue) {
                str = newValue;
                setFileName(str);
                tfFileName.clear();
                tfFileName.appendText(str);
                System.out.println("at server " + fileName);
            }
        });
    }

    public void pressDellButton(ActionEvent actionEvent) throws IOException {
        if (tfFileName.getLength() > 0) {
            Files.delete(Paths.get("client_storage\\" + tfFileName.getText()));
            tfFileName.clear();
            refreshLocalFilesList();
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

    public void refreshServerFilesList() {
//       // getServerFileList();
//        NettyNetwork.currentChannel.writeAndFlush(new FileRequest("list", "delete"));
//        serfilesList.getItems().clear();
//        for (String s : ClientDownload.serverList ) {
//            serfilesList.getItems().add(s);
//        }
        if (Platform.isFxApplicationThread()) {
            NettyNetwork.currentChannel.writeAndFlush(new FileRequest("list", "delete"));
            serfilesList.getItems().clear();
            for (String s : ClientDownload.serverList) {
                serfilesList.getItems().add(s);
            }
        } else {
            Platform.runLater(() -> {
                NettyNetwork.currentChannel.writeAndFlush(new FileRequest("list", "delete"));
                serfilesList.getItems().clear();
                for (String s : ClientDownload.serverList) {
                    serfilesList.getItems().add(s);
                }
            });
        }
    }


    public void pressDownKey() {
        NettyNetwork.currentChannel.writeAndFlush(new FileRequest(tfFileName.getText(), "down"));
        refreshLocalFilesList();
        refreshServerFilesList();
    }

    public void pressUploadKey() {
        try {
            NettyNetwork.currentChannel.writeAndFlush(new FileMessage(Paths.get("client_storage/" + tfFileName.getText())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshServerFilesList();
        tfFileName.clear();
    }

    public void pressDellatServerButton(ActionEvent actionEvent) {
        NettyNetwork.currentChannel.writeAndFlush(new FileRequest(tfFileName.getText(), "delete"));
        refreshServerFilesList();
        tfFileName.clear();
    }

    public void getServerFileList() {
        NettyNetwork.currentChannel.writeAndFlush(new FileRequest("list", "delete"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                NettyNetwork.getInstance().start();
            }
        }).start();

        refreshLocalFilesList();
        //refreshServerFilesList();
        selection();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Box Client");
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}

