package org.example.controller.responses;

public interface RegisterResponses {
    String USERNAME_EXIST = "This username already exist!";
    String WEAK_PASSWORD = "Password must contains number, uppercase, and lowercase characters!";
    String STRONG_PASSWORD = "This password is OK!";
}
