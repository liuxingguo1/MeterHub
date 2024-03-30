package com.vking.duhv.meterhub.controller;

import com.vking.duhv.meterhub.server.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TCPController {

    @Autowired
    private SocketHandler handler;

    @GetMapping("/sendMsgToClient")
    public String sendMsgToClient(@RequestParam("msg") String msg) {
        handler.sendMsgToClient(msg);
        return "ok";
    }

}