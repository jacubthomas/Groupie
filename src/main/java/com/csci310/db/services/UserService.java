package com.csci310.db.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csci310.db.entities.User;
import com.csci310.db.repositories.UserRepository;

import javax.transaction.Transactional;

@Component
public class UserService {

    @Autowired
    UserRepository userRepository;

    Map<Character, Character> mapLowercase;
    Map<Character, Character> mapUppercase;

    public UserService() {
        this.mapLowercase = new HashMap<>();
        this.mapLowercase.put('a', 'n');
        this.mapLowercase.put('b', 'o');
        this.mapLowercase.put('c', 'p');
        this.mapLowercase.put('d', 'q');
        this.mapLowercase.put('e', 'r');
        this.mapLowercase.put('f', 's');
        this.mapLowercase.put('g', 't');
        this.mapLowercase.put('h', 'u');
        this.mapLowercase.put('i', 'v');
        this.mapLowercase.put('j', 'w');
        this.mapLowercase.put('k', 'x');
        this.mapLowercase.put('l', 'y');
        this.mapLowercase.put('m', 'z');
        this.mapLowercase.put('n', 'a');
        this.mapLowercase.put('o', 'b');
        this.mapLowercase.put('p', 'c');
        this.mapLowercase.put('q', 'd');
        this.mapLowercase.put('r', 'e');
        this.mapLowercase.put('s', 'f');
        this.mapLowercase.put('t', 'g');
        this.mapLowercase.put('u', 'h');
        this.mapLowercase.put('v', 'i');
        this.mapLowercase.put('w', 'j');
        this.mapLowercase.put('x', 'k');
        this.mapLowercase.put('y', 'l');
        this.mapLowercase.put('z', 'm');

        this.mapUppercase = new HashMap<>();
        this.mapUppercase.put('A', 'N');
        this.mapUppercase.put('B', 'O');
        this.mapUppercase.put('C', 'P');
        this.mapUppercase.put('D', 'Q');
        this.mapUppercase.put('E', 'R');
        this.mapUppercase.put('F', 'S');
        this.mapUppercase.put('G', 'T');
        this.mapUppercase.put('H', 'U');
        this.mapUppercase.put('I', 'V');
        this.mapUppercase.put('J', 'W');
        this.mapUppercase.put('K', 'X');
        this.mapUppercase.put('L', 'Y');
        this.mapUppercase.put('M', 'Z');
        this.mapUppercase.put('N', 'A');
        this.mapUppercase.put('O', 'B');
        this.mapUppercase.put('P', 'C');
        this.mapUppercase.put('Q', 'D');
        this.mapUppercase.put('R', 'E');
        this.mapUppercase.put('S', 'F');
        this.mapUppercase.put('T', 'G');
        this.mapUppercase.put('U', 'H');
        this.mapUppercase.put('V', 'I');
        this.mapUppercase.put('W', 'J');
        this.mapUppercase.put('X', 'K');
        this.mapUppercase.put('Y', 'L');
        this.mapUppercase.put('Z', 'M');
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }


    public User getUserByUsername(String uname) {
        uname = this.encrypt(uname);
        try {
            User user = userRepository.findByUsername(uname).get();
            user.setUsername(this.decrypt(user.getUsername()));
            user.setPassword(this.decrypt(user.getPassword()));
            user.setConfirmPassword(this.decrypt(user.getConfirmPassword()));
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isUsernameTaken(String username) {
        username = this.encrypt(username);
        return userRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public void persistUser(User user) {
        user.setUsername(this.encrypt(user.getUsername()));
        user.setPassword(this.encrypt(user.getPassword()));
        user.setConfirmPassword(this.encrypt(user.getConfirmPassword()));
        user.setEncrpted(true);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(UUID id) {
        userRepository.deleteUserById(id);
    }

    @Transactional
    public void deleteUserByUsername(String uname) {
        String username = encrypt(uname);
        userRepository.deleteUserByUsername(username);
    }

    public boolean didAuthenticateUser(String username, String password) {
        username = this.encrypt(username);
        password = this.encrypt(password);
        return userRepository.findByUsernameAndPassword(username, password).isPresent();
    }

    public boolean passwordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * Encryption algorithm is ROT-13
     */

    public String encrypt(String val) {
        if (val == null) { return null; }
        StringBuilder sb = new StringBuilder(val);
        for (int i = 0; i < val.length(); i++) {
            if (Character.isUpperCase(sb.charAt(i))) {
                sb.replace(i, i + 1, String.valueOf(this.mapUppercase.get(sb.charAt(i))));
            } else if (Character.isLowerCase(sb.charAt(i))) {
                sb.replace(i, i + 1, String.valueOf(this.mapLowercase.get(sb.charAt(i))));
            }
        }
        return sb.toString();
    }

    public String decrypt(String val) {
        return this.encrypt(val); // encryption and decryption algorithm is the same because of ROT-13
    }
}