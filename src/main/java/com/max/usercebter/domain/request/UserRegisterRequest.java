package com.max.usercebter.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author max
 * 8/4/25
 */

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
