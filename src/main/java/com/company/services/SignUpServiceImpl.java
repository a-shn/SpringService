package com.company.services;

import com.company.models.User;
import com.company.repositories.UserRepository;
import com.company.services.EmailSender;
import com.company.services.SignUpPool;
import com.company.services.SignUpService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SignUpServiceImpl implements SignUpService {
    SignUpPool signUpPool;
    UserRepository userRepository;
    EmailSender emailSender;

    public SignUpServiceImpl(SignUpPool signUpPool, UserRepository userRepository, EmailSender emailSender) {
        this.signUpPool = signUpPool;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    @Override
    public boolean signUp(String login, String email, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User(login, email, encoder.encode(password));
            String link = createLink(user);
            signUpPool.put(user, link, LocalDateTime.now());
            emailSender.sendVerificationEmail(user, link);
            return true;
        } else {
            return false;
        }
    }

    private String createLink(User user) {
        try {
            String tmp = user.getEmail() + LocalDateTime.now().toString();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(tmp.getBytes());
            byte[] digest = md.digest();
            String hash = Base64.getEncoder().encodeToString(digest);;
            String part1 = "http://localhost:8080/springservice/verification";
            String part2 = "?hash=" + hash;
            return part1 + part2;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException();
        }
    }
}
