package com.markerhub.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenDto implements Serializable {
    private String account;

    private String password;

    private String cookie;

    private Object obj;
}
