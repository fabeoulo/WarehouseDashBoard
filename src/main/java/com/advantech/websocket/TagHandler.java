/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.websocket;

import com.advantech.job.PollingTags;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author Justin.Yeh
 */
public class TagHandler extends BasicHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(TagHandler.class);

    private final String JOB_NAME = "PollingTags";

    private static boolean isJobScheduled = false;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PollingTags pollingTags;

    @PostConstruct
    private void init() {
        log.info("Endpoint2 init polling job: " + JOB_NAME);
        super.init(JOB_NAME);
        if (super.sessions != null && !super.sessions.isEmpty()) {
            synchronized (sessions) {
                sessions.clear();
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession wss) throws Exception {
//        if (wss.isOpen()) {
//            String sessionId = (String) wss.getAttributes().get("sessionId");
//            System.out.println("ConnectionEstablished: " + sessionId);
//            sessions.add(wss);
////            wss.sendMessage(new TextMessage("ConnectionEstablished"));
//        }

        //Push the current status on client first connect
        try {
            wss.sendMessage(new TextMessage(pollingTags.getData()));
        } catch (Exception e) {
            //Remove session because reconnectedWebSocket.js not trigger ws.close when reconnected
            sessions.remove(wss);
            log.error("Remove empty session " + wss.getId());
        }
        sessions.add(wss);

        //每次當client連接進來時，去看目前session的數量 當有1個session時把下方quartz job加入到schedule裏頭(只要執行一次，不要重複加入)
        synchronized (sessions) {
            int a = sessions.size();
            if (a > 0 && isJobScheduled == false) {
                pollingAndBrocast();
                isJobScheduled = true;
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession wss, WebSocketMessage<?> wsm) throws Exception {
        System.out.println(wsm.getPayload());

//        if (wsm.getPayload() instanceof String) {
//            String message = (String) wsm.getPayload();
//            sessions.forEach(s -> {
//                try {
//                    s.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
//                } catch (Exception ex) {
//                    System.out.println(ex);
//                }
//            });
//        }
    }

    @Override
    public void handleTransportError(WebSocketSession wss, Throwable thrwbl) throws Exception {
        System.out.println("TransportError");
        synchronized (sessions) {
            sessions.remove(wss);
            log.error(thrwbl.toString(), thrwbl);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession wss, CloseStatus cs) throws Exception {
        System.out.println("ConnectionClosed");
        synchronized (sessions) {
            sessions.remove(wss);
            System.out.println("Sessions size: " + sessions.size());

            //當client端完全沒有連結中的使用者時，把job給關閉(持續執行浪費性能)
            if (sessions.isEmpty()) {
                unPollingDB();
                isJobScheduled = false;
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
