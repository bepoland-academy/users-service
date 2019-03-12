package pl.betse.beontime.users.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.betse.beontime.users.bo.UserBo;
import pl.betse.beontime.users.entity.PasswordTokenEntity;
import pl.betse.beontime.users.repository.PasswordTokenRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Slf4j
@Service
public class PasswordService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final PasswordTokenRepository passwordTokenRepository;

    public PasswordService(JavaMailSender javaMailSender, TemplateEngine templateEngine, PasswordTokenRepository passwordTokenRepository) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.passwordTokenRepository = passwordTokenRepository;
    }


    public void sendMessageToUser(UserBo userBo, String originLink) {
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append(originLink);
        linkBuilder.append("/password?token=");
        List<PasswordTokenEntity> passwordTokenEntities = passwordTokenRepository.findAll();
        for (PasswordTokenEntity passwordTokenEntity : passwordTokenEntities) {
            if (userBo.getUserId().equals(passwordTokenEntity.getUserEntity().getGuid())) {
                linkBuilder.append(passwordTokenEntity.getToken());
            }
        }
        log.debug(linkBuilder.toString());
        sendPasswordMessage(userBo, prepareEmailContextMessage(userBo, linkBuilder.toString()));
    }


    public String prepareEmailContextMessage(UserBo userBo, String linkForRegistration) {
        Context context = new Context();
        context.setVariable("firstName", userBo.getFirstName());
        context.setVariable("lastName", userBo.getLastName());
        context.setVariable("link", linkForRegistration);
        return templateEngine.process("template", context);
    }


    public void sendPasswordMessage(UserBo userBo, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(userBo.getEmail());
            helper.setReplyTo(userBo.getEmail());
            helper.setSubject("[BeOnTime] Please setup password for your account.");
            helper.setText(content, true);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }


}
