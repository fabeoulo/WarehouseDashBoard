/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.websocket;

import com.advantech.helper.CronTrigMod;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PreDestroy;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author Wei.Cheng
 */
public abstract class BasicHandler {

    private static final Logger log = LoggerFactory.getLogger(BasicHandler.class);

    @Autowired
    private CronTrigMod ctm;

    protected final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final Map<WebSocketSession, Integer> errroCounter = new HashMap();

    private String jobName;

    protected void init(String jobName) {
        this.jobName = jobName;
    }

    ///Brocast the servermessage to all online users.
    public void sendAll(String msg) {

        /* Send the new rate to all open WebSocket sessions */
        ArrayList<WebSocketSession> closedSessions = new ArrayList<>();
        sessions.forEach((session) -> {
            try {
                if (!session.isOpen()) {
                    closedSessions.add(session);
                } else {
                    synchronized (session) {
                        TextMessage t = new TextMessage(msg);
                        session.sendMessage(t);
                    }
                }
            } catch (IOException ex) {
                log.error("Error cause on session id " + session.getId(), ex);
                removeIfSessionReachErrorCount(session);
            }
        });
        sessions.removeAll(closedSessions);
    }

    //Remove not exist client session in sessions
    //https://bbs.csdn.net/topics/392066158
    private void removeIfSessionReachErrorCount(WebSocketSession session) {
        Integer errorCount = errroCounter.get(session);
        if (errorCount != null && ++errorCount == 3) {
            sessions.remove(session);
            log.info("Error session removed, now have " + sessions.size() + " in the sessions(static collection).");
        } else {
            errroCounter.put(session, errorCount == null ? 0 : errorCount);
        }
    }

    // Generate when connect users are at least one.
    protected void pollingAndBrocast() {
        try {
            ctm.scheduleJob(jobName);
            log.info("Job " + jobName + " has been schedule");
        } catch (SchedulerException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    // Delete when all users are disconnect.
    protected void unPollingDB() {
        try {
            ctm.removeJob(jobName);
            log.info("Job " + jobName + " has been removed");
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    @PreDestroy
    protected void removeSessions() {
        this.sessions.clear();
        this.errroCounter.clear();
    }

}
