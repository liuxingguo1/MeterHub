package com.vking.duhv.meterhub.client.core.handler;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import com.vking.duhv.meterhub.client.acquisition.protocol.iec104.dto.APCI;
import com.vking.duhv.meterhub.client.acquisition.protocol.iec104.dto.APDU;
import com.vking.duhv.meterhub.client.core.Constant;
import com.vking.duhv.meterhub.client.core.TCPConnection;
import com.vking.duhv.meterhub.client.core.TCPHandler;
import com.vking.duhv.meterhub.client.core.conf.TCPIEC104Config;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.vking.duhv.meterhub.client.acquisition.protocol.iec104.Iec104Format.*;

@Slf4j
public class IEC104Handler extends TCPHandler {

    private static final String U_START_ACT = "68 04 07 00 00 00";
    private static final String U_START_CON = "68 04 0b 00 00 00";

    private static final String U_STOP_ACT = "68 04 13 00 00 00";
    private static final String U_STOP_CON = "68 04 23 00 00 00";

    private static final String U_TEST_ACT = "68 04 43 00 00 00";
    private static final String U_TEST_CON = "68 04 83 00 00 00";

    private static final String S_ACT_TEMPLATE = "68 04 01 00 ${NR}";
    //I帧总招
    private static final String I_TOTAL_CALL_ACT_TEMPLATE = "68 0E ${NS} ${NR} 64 01 06 00 01 00 00 00 00 14 ";

    private Boolean startup = false;//启动状态
    private Boolean uTestCon = false;//是否收到u_test_con回复 测试u帧t1超时后重新建立TCP
    private Timer timer;
    private TimerTask t2SAckTask;//收到I帧 t2超时 发送s确认帧
    private TimerTask t3FreeTimeoutTask;////空闲超时 t3超时发送u测试帧

    private Integer unAckINum = 0;//未确认I帧数量 最少到w值 发送S帧

    private Integer ns = 0;//(自己)发送序号
    private Integer nr = 0;//(自己)接收序号
    private Integer ack = 0;//(对方)正确收到的小于等于这个编号I帧的APDU 通过S帧传递
    private TCPConnection conn;
    private TCPIEC104Config config;

    @Override
    public void init(TCPConnection con) {
        this.conn = con;
        this.config = (TCPIEC104Config) con.getConfig();
    }

    public void destroy() {
        if (null != timer) {
            timer.cancel();
        }
    }

    //channel准备就绪
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.debug("通道准备就绪");
        timer = new Timer();
        uStartAct();
        resetT2SAckTask();
        resetT3FreeTimeoutTask();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //定时发送总招
                iTotalCallAct();
            }
        }, 30 * 1000, 3600 * 1000);
    }

    //channel被关闭
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("通道被关闭");
        destroy();
    }

    //channel中有可读的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        resetT3FreeTimeoutTask();
        char[] hex = HexUtil.encodeHex((byte[]) msg);

        APDU apdu = new APDU((byte[]) msg);
        APCI apci = apdu.getApci();
        switch (apci.getFormat()) {
            case I -> {
                nr++;
                unAckICheck();
                if (ns - Math.abs(calcNr(apci)) > config.getK()) {
                    //发送序号和接受序号差值超过k 主动关闭前发送S帧 等逻辑
                    //sAct(ctx.channel());
                    //reconnect();
                }
                this.conn.getMeterHubServerClient().send(StrUtil.format(Constant.METER_JSON_TEMP,
                        this.config.getHost(),
                        this.config.getCode(),
                        Constant.DATA_PROTOCOL_IEC104,
                        System.currentTimeMillis(),
                        String.valueOf(hex)
                ));
            }
            case U -> {
                //start_con确认开始传输
                if (((apci.getCtl1() >> 3) & 1) == 1) {
                    startup = true;
                }
                //stop_con确认停止传输
                if (((apci.getCtl1() >> 5) & 1) == 1) {
                    startup = false;
                }
                //test_con确认测试传输
                if (((apci.getCtl1() >> 7) & 1) == 1) {
                    uTestCon = true;
                }
                //test_act启动测试传输
                if (((apci.getCtl1() >> 6) & 1) == 1) {
                    //立即返回test_con
                    uTestCon();
                }
            }
            case S -> {
                //仅包含接受序号
                ack = calcNr(apci);
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("{}:通道异常", conn.getConfig().getName(), cause.getMessage());
    }


    //启动传输
    public void uStartAct() {
        if (!startup) {
            log.debug("{}:主站发送启动传输", conn.getConfig().getName());
            send(U_START_ACT);
        }
    }

    //停止传输
    public void uStopAct() {
        if (startup) {
            log.debug("{}:主站发送停止传输", conn.getConfig().getName());
            send(U_STOP_ACT);
        }
    }

    //测试传输
    public void uTestAct() {
        log.debug("{}:主站发送测试传输", conn.getConfig().getName());
        send(U_TEST_ACT);
        uTestCon = false;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!uTestCon) {
                    //主动关闭(TCP)并主动连接(TCP)
                    conn.destroy();
                    conn.setDestroy(false);
                    conn.start();
                }
            }
        }, config.getT1() * 1000);
    }

    //确认测试传输
    public void uTestCon() {
        log.debug("{}:主站发送确认测试传输", conn.getConfig().getName());
        send(U_TEST_CON);
    }

    //发送S帧
    public void sAct() {
        log.debug("{}:主站发送S帧确认报文", conn.getConfig().getName());
        send(S_ACT_TEMPLATE, new HashMap<>() {{
            put("NR", splitAndFlipHex(nr));
        }});
        unAckINum = 0;
        resetT2SAckTask();
    }

    //发送I帧 总招
    public void iTotalCallAct() {
        log.debug("{}:主站发送总招", conn.getConfig().getName());
        send(I_TOTAL_CALL_ACT_TEMPLATE, new HashMap<>() {{
            put("NS", splitAndFlipHex(ns));
            put("NR", splitAndFlipHex(nr));
        }});
        ns++;
    }

    private void unAckICheck() {
        unAckINum++;
        if (unAckINum >= config.getW()) {
            sAct();
        }

    }

    //s确认帧发送
    private void resetT2SAckTask() {
        if (null != t2SAckTask) {
            t2SAckTask.cancel();
        }
        t2SAckTask = new TimerTask() {
            @Override
            public void run() {
                sAct();
            }
        };
        timer.schedule(t2SAckTask, config.getT2() * 1000);
    }

    private void resetT3FreeTimeoutTask() {
        if (null != t3FreeTimeoutTask) {
            t3FreeTimeoutTask.cancel();
        }
        t3FreeTimeoutTask = new TimerTask() {
            @Override
            public void run() {
                uTestAct();
            }
        };
        timer.schedule(t3FreeTimeoutTask, config.getT3() * 1000);
    }

    private void send(String str) {
        conn.getChannel().writeAndFlush(HexUtil.decodeHex(StrUtil.cleanBlank(str)));
    }

    private void send(String str, Map<String, String> param) {
        send(StringSubstitutor.replace(str, param));
    }

    private int calcNs(APCI apci) {
        int ns_lsb = apci.getCtl1() >> 1;//低位接受序号
        int ns_msb = apci.getCtl2();//高位接收序号
        return (ns_lsb | ns_msb << 7);
    }

    private int calcNr(APCI apci) {
        int nr_lsb = apci.getCtl3() >> 1;//低位接受序号
        int nr_msb = apci.getCtl4();//高位接收序号
        return (nr_lsb | nr_msb << 7);
    }

    //发送I 接收I/S 序列号转换 低位在前高位在后
    private String splitAndFlipHex(int n) {
        int i = n << 1;
        int a = i & 255;
        int b = i >> 8 & 255;
        String s1 = Integer.toHexString(a);
        String s2 = Integer.toHexString(b);
        return (a < 16 ? "0" + s1 : s1) + (b < 16 ? "0" + s2 : s2);
    }

}