package com.markerhub.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Money implements Serializable {
    private Integer currentMonthMoney;

    private Integer currentYearMoney;
}
