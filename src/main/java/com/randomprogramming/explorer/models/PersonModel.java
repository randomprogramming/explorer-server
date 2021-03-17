package com.randomprogramming.explorer.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class PersonModel extends LoginModel {
    private String email;
    private String repeatedPassword;

    public boolean hasNullValues() {
        return email == null || username == null || password == null || repeatedPassword == null;
    }
}
