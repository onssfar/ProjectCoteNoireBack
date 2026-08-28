package com.cotenoire.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmation(String email, String firstName) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("TON_EMAIL@outlook.com");
        message.setTo(email);

        message.setSubject("Confirmation de votre commande - Côte Noire");

        message.setText(
                "Bonjour " + firstName + ",\n\n" +
                        "Nous vous confirmons que votre commande a bien été enregistrée.\n\n" +
                        "Votre commande sera livrée prochainement.\n\n" +
                        "Mode de paiement : Paiement à la livraison.\n\n" +
                        "Merci pour votre confiance.\n\n" +
                        "Côte Noire"
        );

        mailSender.send(message);
    }
}