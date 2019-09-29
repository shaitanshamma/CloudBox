package com.geerkbrains.netty.example.client;

import com.geerkbrains.netty.example.common.FileList;
import com.geerkbrains.netty.example.common.FileMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ClientDownload extends ChannelInboundHandlerAdapter {

    Main controller;

    public ClientDownload(Main controller) {
        this.controller = controller;
    }


    static List<String> serverList = new ArrayList<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof FileMessage) {
                FileMessage fr = (FileMessage) msg;
                Files.write(Paths.get("client_storage/" + fr.getFilename()), fr.getData());
            } else if (msg instanceof FileList) {
                serverList.clear();
//              controller.serfilesList.getItems().clear();
                FileList fl = (FileList) msg;
                for (String s : fl.getSerfilesList()) {
//                    controller.serfilesList.getItems().add(s);
                    serverList.add(s);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}

