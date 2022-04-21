package com.treeshop.service;

import com.treeshop.dao.UserRepository;
import com.treeshop.entity.UserEntity;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
@Transactional
public class UsersService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean checkLogin(String username, String password) {
        return userRepository.existsByUsernameAndPassword(username, password);
    }

    public UserEntity changePassword(UserEntity userEntity, String password) {
        userEntity.setPassword(password);
        return userRepository.save(userEntity);
    }

    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserEntity registerUser(UserEntity userEntity){
        String randomCode = RandomString.make(64);
        userEntity.setRoleId("1");
        userEntity.setEnabled(false);
        userEntity.setVerificationCode(randomCode);
        return userRepository.save(userEntity);
    }

    public void sendVerificationEmail(UserEntity userEntity, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String subject = "Xác nhận tài khoản";
        String senderName = "Tree Shop";
        StringBuilder mailContent = new StringBuilder("<p>Dear, " + userEntity.getFullname() + "</p>");
        mailContent.append("<p> Bấm vào link sau để tiến hành xác nhận tài khoản người dùng: </p>");
        String verifyURL = siteURL + "/home/" + userEntity.getUsername() +"/verify?code=" + userEntity.getVerificationCode();
        mailContent.append("<h3><a href=\"" + verifyURL + "\">VERIFY</a></h3>");
        mailContent.append("<p>Xin cám ơn, <br> Tree Shop</p>");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);

        mimeMessageHelper.setFrom("treeshop49@gmail.com", senderName);
        mimeMessageHelper.setTo(userEntity.getEmail());
        mimeMessageHelper.setSubject(subject);

        mimeMessageHelper.setText(mailContent.toString(), true);
        javaMailSender.send(message);

    }

    public boolean verifyClient(String verificationCode){
        UserEntity userEntity = userRepository.findUserEntityByVerificationCode(verificationCode);
        if(userEntity == null || userEntity.isEnabled()){
            return false;
        }
        else {
            userRepository.setEnableTrue(userEntity.getUsername());
            return true;
        }
    }

}
