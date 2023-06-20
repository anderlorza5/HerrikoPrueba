package com.example.herrikoprueba.Clases;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import java.io.ByteArrayOutputStream;
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


    public static void sendConPDF(String to, String subject, String text, ByteArrayOutputStream pdfStream) {
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
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);

            // Crea el contenido multipart para contener el mensaje y el archivo adjunto
            Multipart multipart = new MimeMultipart();

            // Agrega el texto del mensaje
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(text);
            multipart.addBodyPart(messageBodyPart);

            // Crea un DataSource a partir del ByteArrayOutputStream
            DataSource dataSource = new ByteArrayDataSource(pdfStream.toByteArray(), "application/pdf");

            // Crea un MimeBodyPart para el archivo adjunto y añádelo al multipart
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
            attachmentBodyPart.setFileName("documento.pdf");
            multipart.addBodyPart(attachmentBodyPart);

            // Asigna el multipart al mensaje
            message.setContent(multipart);

            // Enviar el mensaje
            Transport.send(message);

            System.out.println("Mensaje enviado con éxito");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
