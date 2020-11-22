package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@TableName("zyj_token")
@Data
public class ZyjToken implements Serializable {
    private String id;

    private Timestamp expireTime;

    private String status;

    private Integer queryNumber;

    private Integer timeCount;

    private String type;

    private String remarks;
}
