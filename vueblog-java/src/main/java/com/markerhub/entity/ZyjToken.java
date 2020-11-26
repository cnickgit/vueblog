package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: vueblog
 * @description:
 * @author: wsg
 * @create: 2020-11-24 15:04
 **/
@Data
@TableName("zyj_token")
public class ZyjToken implements Serializable {
    /**
     * id
     */
    @TableId
    private String id;

    private String code;

    private String type;

    /**
     * 类型说明
     */
    private String typeRemarks;

    /**
     * 金额
     */
    private Integer money;

    /**
     * prescription
     */
    private Integer prescription;

    /**
     * 是否启用
     */
    private String enable;

    private Date enableTime;

    private Date endTime;

    private Date createTime;

    private Date updateTime;

}
