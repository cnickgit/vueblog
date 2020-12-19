package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("zyj_user")
public class ZyjUser implements Serializable {
    @TableId
    private String id;

    private String cookie;

    private String account;

    private String password;

    private String expire;

    private String leaveNum;

    private Integer serialNum;

    private Integer maxTimes;

}
