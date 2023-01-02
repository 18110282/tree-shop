package com.treeshop.serviceImpl;

import com.treeshop.dao.UserRepository;
import com.treeshop.entity.UserEntity;
import com.treeshop.service.UsersService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public UsersServiceImpl(UserRepository userRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }
    @Override
    public UserEntity findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public boolean checkLogin(String username, String password) {
        return userRepository.existsByUsernameAndPassword(username, password);
    }
    @Override
    public void changePassword(UserEntity userEntity, String password) {
        userEntity.setPassword(password);
        userRepository.save(userEntity);
    }
    @Override
    public void saveUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Override
    public void saveUserInWeb(String username, UserEntity userEntity) {
        UserEntity updateClient = userRepository.findByUsername(username);
        updateClient.setFullname(userEntity.getFullname());
        updateClient.setPhoneNumber(userEntity.getPhoneNumber());
        updateClient.setAddress(userEntity.getAddress());
        updateClient.setEmail(userEntity.getEmail());
        userRepository.save(updateClient);
    }

    @Override
    public boolean savePasswordOfClient(String username, String oldPassword, String newPassword) {
        UserEntity client = userRepository.findByUsername(username);
        if(client.getPassword().equals(oldPassword)){
            client.setPassword(newPassword);
            userRepository.save(client);
            return true;
        }
        return false;
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
    @Override
    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    @Override
    public void registerUser(UserEntity userEntity){
        String randomCode = RandomString.make(64);
        userEntity.setRoleId("2");
        userEntity.setEnabled(false);
        userEntity.setVerificationCode(randomCode);
        userRepository.save(userEntity);
    }
    @Override
    public void sendVerificationEmail(UserEntity userEntity, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String subject = "Xác nhận tài khoản";
        String senderName = "Tree Shop";
        StringBuilder mailContent = new StringBuilder("<p>Dear, " + userEntity.getFullname() + "</p>");
        mailContent.append("<p> Bấm vào link sau để tiến hành xác nhận tài khoản người dùng: </p>");
        String verifyURL = siteURL + "/home/" + userEntity.getUsername() +"/verify?code=" + userEntity.getVerificationCode();
        mailContent.append("<h3><a href=\"").append(verifyURL).append("\">VERIFY</a></h3>");
        mailContent.append("<p>Xin cám ơn, <br> ❤ Tree Shop ❤</p>");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);

        mimeMessageHelper.setFrom("treeshop49@gmail.com", senderName);
        mimeMessageHelper.setTo(userEntity.getEmail());
        mimeMessageHelper.setSubject(subject);

        mimeMessageHelper.setText(mailContent.toString(), true);
        javaMailSender.send(message);

    }
    @Override
    public void sendForgotPassEmail(UserEntity userEntity, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String subject = "Quên mật khẩu";
        String senderName = "Tree Shop";
        String newPass =  RandomString.make(8);
        StringBuilder mailContent = new StringBuilder("<p>Dear, " + userEntity.getFullname() + "</p>");
        mailContent.append("<p> Mật khẩu của bạn là: <strong style='color:blue'>" + newPass + "</strong></p>");
        mailContent.append("<p>Xin cám ơn, <br> ❤ Tree Shop ❤</p>");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);

        mimeMessageHelper.setFrom("treeshop49@gmail.com", senderName);
        mimeMessageHelper.setTo(userEntity.getEmail());
        mimeMessageHelper.setSubject(subject);

        mimeMessageHelper.setText(mailContent.toString(), true);
        javaMailSender.send(message);
        userEntity.setPassword(newPass);
        userRepository.save(userEntity);
    }
    @Override
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
