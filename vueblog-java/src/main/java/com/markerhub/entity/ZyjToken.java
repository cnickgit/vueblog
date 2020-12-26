package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String typeId;

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

    private String limitTime;
    /**
     * 剩余次数
     */
    private Integer remainingTimes;

    /**
     * 是否启用
     */
    private String enable;

    /**
     * 导出状态
     */
    private String exportStatus;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date enableTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

}
