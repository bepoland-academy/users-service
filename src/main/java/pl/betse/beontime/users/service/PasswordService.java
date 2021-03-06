package pl.betse.beontime.users.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.betse.beontime.users.bo.UserBo;
import pl.betse.beontime.users.entity.PasswordTokenEntity;
import pl.betse.beontime.users.entity.UserEntity;
import pl.betse.beontime.users.exception.PasswordTokenNotFoundException;
import pl.betse.beontime.users.repository.PasswordTokenRepository;
import pl.betse.beontime.users.repository.UserRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@Service
public class PasswordService {

    @Value("${administration.email}")
    private String ADMINISTRATION_EMAIL;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final PasswordTokenRepository passwordTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordService(JavaMailSender javaMailSender, TemplateEngine templateEngine, PasswordTokenRepository passwordTokenRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.passwordTokenRepository = passwordTokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    void sendMessageToUser(UserBo userBo, String originLink) {
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append(originLink);
        linkBuilder.append("/password?token=");
        List<PasswordTokenEntity> passwordTokenEntities = passwordTokenRepository.findAll();
        for (PasswordTokenEntity passwordTokenEntity : passwordTokenEntities) {
            if (userBo.getUserId().equals(passwordTokenEntity.getUserEntity().getGuid())) {
                linkBuilder.append(passwordTokenEntity.getToken());
            }
        }
        sendPasswordMessage(userBo, linkBuilder.toString());
    }


    private String prepareEmailContextMessage(UserBo userBo, String linkForRegistration) {
        Context context = new Context();
        context.setVariable("firstName", userBo.getFirstName());
        context.setVariable("lastName", userBo.getLastName());
        context.setVariable("link", linkForRegistration);
        return templateEngine.process("template", context);
    }


    private void sendPasswordMessage(UserBo userBo, String link) {
        String content = prepareEmailContextMessage(userBo, link);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(ADMINISTRATION_EMAIL, "BeOnTime Administration Team");
            helper.setTo(userBo.getEmail());
            helper.setSubject("[BeOnTime] Please setup password for your account.");
            helper.setText(content, true);

        } catch (MessagingException e) {
            log.error("Message has not been sent.", e);
        } catch (UnsupportedEncodingException e) {
            log.error("Wrong text decoding.", e);
        }
        javaMailSender.send(message);
    }


    public void setUserPassword(String password, String token) {
        PasswordTokenEntity passwordTokenEntity = passwordTokenRepository.findByToken(token)
                .orElseThrow(PasswordTokenNotFoundException::new);
        UserEntity userEntity = passwordTokenEntity.getUserEntity();
        userEntity.setActive(true);
        userEntity.setPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
        passwordTokenRepository.delete(passwordTokenEntity);
    }
}
