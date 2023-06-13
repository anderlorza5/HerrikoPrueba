package com.example.herrikoprueba.Clases;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMail {
    private static final String USERNAME = "ander210194@gmail.com";
    private static final String PASSWORD = "ypsmddzypbehuwku";

    public static void send(String to, String subject, String text) {
        // Configura las propiedades del servidor de correo
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Crea una nueva sesión con una autenticación
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {
            // Crea un nuevo mensaje de correo electrónico
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);

            // Enviar el mensaje
            Transport.send(message);

            System.out.println("Mensaje enviado con éxito");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
