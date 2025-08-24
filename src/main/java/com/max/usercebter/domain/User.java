package com.max.usercebter.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Getter
    @Setter
    private Long id;

    /**
     * 用户昵称
     */
    @TableField(value = "username")
    @Getter
    @Setter
    private String username;

    /**
     * 账号
     */
    @TableField(value = "account")
    @Setter
    private String account;

    /**
     * 性别
     */
    @TableField(value = "gender")
    @Getter
    @Setter
    private Integer gender;

    /**
     * 用户头像
     */
    @TableField(value = "avatarUrl")
    @Getter
    @Setter
    private String avatarUrl;

    /**
     * 密码
     */
    @TableField(value = "userPassword")
    @Getter
    @Setter
    private String userPassword;

    /**
     * 电话
     */
    @TableField(value = "tel")
    @Getter
    @Setter
    private String tel;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @Getter
    @Setter
    private String email;

    /**
     * 用户状态 0-正常
     */
    @TableField(value = "status")
    @Getter
    @Setter
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    @Getter
    @Setter
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    @Getter
    @Setter
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "isDelete")
    @TableLogic
    @Getter
    @Setter
    private Integer isDelete;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员 
     */
    @TableField(value = "userRole")
    @Getter
    @Setter
    private Integer userRole;

    /**
     * 星球编码
     */
    @TableField(value = "planetCode")
    @Getter
    @Setter
    private String planetCode;
}