// src/main/java/com/fpoly/be_wanren_buffet/service/EmailService.java

package com.fpoly.be_wanren_buffet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Gửi email đơn giản (text)
     *
     * @param to      Địa chỉ email người nhận
     * @param subject Tiêu đề email
     * @param text    Nội dung email
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@gmail.com"); // Địa chỉ email gửi
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
            System.out.println("Email đã được gửi thành công!");
        } catch (MailException e) {
            e.printStackTrace();
            System.out.println("Có lỗi xảy ra khi gửi email.");
        }
    }
}
