package com.markerhub.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZyjUser implements Serializable {
    private String id;

    private String account;

    private String password;

    private String move;
}
