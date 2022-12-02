package com.form.generator.utility.exceptions;

public class UserValidationException extends Exception {

    public UserValidationException(String str) {

        super(str);
    }

    @Override
    public String getMessage() {

        return super.getMessage().replace("java.lang.Exception: ", "");
    }
}