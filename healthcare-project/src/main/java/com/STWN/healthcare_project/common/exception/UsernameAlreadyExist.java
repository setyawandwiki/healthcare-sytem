package com.STWN.healthcare_project.common.exception;

public class UsernameAlreadyExist extends RuntimeException {
    public UsernameAlreadyExist(String message) {
        super(message);
    }
}
