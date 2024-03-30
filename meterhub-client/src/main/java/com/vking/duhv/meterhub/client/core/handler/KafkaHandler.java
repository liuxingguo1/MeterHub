package com.vking.duhv.meterhub.client.core.handler;

import com.vking.duhv.meterhub.client.core.KafkaConnection;
import com.vking.duhv.meterhub.client.core.MQHandler;
import com.vking.duhv.meterhub.client.core.SubSystemConnection;

public class KafkaHandler extends MQHandler {

    private KafkaConnection con;


    public void init(SubSystemConnection con) {
        this.con = (KafkaConnection) con;
    }

    @Override
    public void receive(Object obj) {
        if (null != obj) {
            con.getMeterHubServerClient().send(obj.toString());
        }
    }


}