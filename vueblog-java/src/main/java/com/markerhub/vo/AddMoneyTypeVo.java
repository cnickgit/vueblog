package com.markerhub.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddMoneyTypeVo implements Serializable {
    private String id;
    private Integer money;
    private String timeType;
    private Integer queryNum;
}
