package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: vueblog
 * @description:
 * @author: wsg
 * @create: 2021-03-01 09:21
 **/
@TableName("hh_user")
@Data
public class HhUser implements Serializable {
    private String id;

    private String account;

    private String password;

    private String cookies;

    private String useStatus;
}
