package com.vking.duhv.meterhub.client.controller;

import com.vking.duhv.meterhub.client.api.dto.ConfigDTO;
import com.vking.duhv.meterhub.client.core.ConnectionManager;
import com.vking.duhv.meterhub.client.core.SubSystemConnection;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@Tag(name = "连接管理")
@RestController
public class ConnectionController {

    @Autowired
    private ConnectionManager connectionManager;

    @GetMapping("/findAllConnection")
    public Collection<SubSystemConnection> findAllConnection() {
        Collection<SubSystemConnection> list = connectionManager.getAllConnection();
        return list;
    }

    @PostMapping("/addConnection")
    public SubSystemConnection addConnection(@RequestBody ConfigDTO config) {
        SubSystemConnection conn = connectionManager.addConnection(config);
        return conn;
    }

    @GetMapping("/deleteConnection")
    public Boolean deleteConnection(@RequestParam("id") String id) {
        return connectionManager.deleteConnection(id);
    }

    @PostMapping("/modifyConnection")
    public SubSystemConnection modifyConnection(@RequestBody ConfigDTO config) {
        SubSystemConnection conn = connectionManager.modifyConnection(config);
        return conn;
    }

}
