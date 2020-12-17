package com.markerhub.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: vueblog
 * @description:
 * @author: wsg
 * @create: 2020-12-17 18:53
 **/
@Data
public class TokenExcel implements Serializable {
    /**
     * 单位名称
     */
    @Excel(name = "链接")
    @NotNull
    private String code;
    /**
     * 金额
     */
    @Excel(name = "金额")
    @NotNull
    private Integer money;

    /**
     * prescription
     */
    @Excel(name = "次数")
    @NotNull
    private Integer prescription;
    /**
     * 类型说明
     */
    @Excel(name = "说明")
    @NotNull
    private String typeRemarks;


}
