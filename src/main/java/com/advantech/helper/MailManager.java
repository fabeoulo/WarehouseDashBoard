/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 *
 * @author Wei.Cheng
 */
public class MailManager {

    private static final Logger log = LoggerFactory.getLogger(MailManager.class);

    private JavaMailSender mailSender;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendMail(String[] to, String subject, String text) throws MessagingException {
        return this.sendMail(to, new String[0], subject, text, null);
    }

    public boolean sendMail(String[] to, String[] cc, String subject, String text) throws MessagingException {
        return this.sendMail(to, cc, subject, text, null);
    }

    public boolean sendMail(String[] to, String subject, String text, Map<String, InputStreamSource> imageResources) throws MessagingException {
        return this.sendMail(to, new String[0], subject, text, imageResources);
    }

    public boolean sendMail(String[] to, String[] cc, String subject, String text, Map<String, InputStreamSource> imageResources) throws MessagingException {
        boolean flag = false;

        try {
            // Do the business calculations...
            // Call the collaborators to persist the order...
            // Create a thread safe "copy" of the template message and customize it
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setText(text, true);
            helper.setTo(to);
            helper.setCc(cc);
            helper.setSubject(subject);
            helper.setFrom("kevin1@172.20.131.52");
            if (imageResources != null) {
                for (Map.Entry<String, InputStreamSource> entry : imageResources.entrySet()) {
                    helper.addAttachment(entry.getKey(), entry.getValue());
                }
            }

            this.mailSender.send(mimeMessage);
            flag = true;
        } catch (MailException | MessagingException ex) {
            // simply log it and go on...
            log.error(ex.getMessage());
        }

        return flag;
    }
}
