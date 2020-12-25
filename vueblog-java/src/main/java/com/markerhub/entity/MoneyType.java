package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("moneyType")
public class MoneyType implements Serializable {
    private String id;
    private Integer money;
    private String timeType;
    private Integer queryNum;
    private Date createTime;
    private Date updateTime;
}
