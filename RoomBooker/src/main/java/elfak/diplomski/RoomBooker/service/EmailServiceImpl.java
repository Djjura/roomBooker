package elfak.diplomski.RoomBooker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl {

    @Autowired
    private MailService emailSender;

//    @EventListener(ApplicationReadyEvent.class)

    public void sendSimpleMessage(/*String to, String subject, String text*/) {
        JavaMailSender javaMailSender = emailSender.getJavaMailSender();

        String to = "roombooker224@gmail.com";
        String subject = "Potvrda o zakazanom terminu";
        String text = "Postovani zakali ste salu 1 dana 28.06.2024 u periodu od 13:00 do 14:00";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("roombooker224@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}