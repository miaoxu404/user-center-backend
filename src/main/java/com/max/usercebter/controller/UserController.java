package com.max.usercebter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.max.usercebter.common.BaseResponse;
import com.max.usercebter.common.ErrorCode;
import com.max.usercebter.common.ResultUtils;
import com.max.usercebter.domain.User;
import com.max.usercebter.domain.request.UserLoginRequest;
import com.max.usercebter.domain.request.UserRegisterRequest;
import com.max.usercebter.exception.BusinessException;
import com.max.usercebter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.max.usercebter.constant.UserConstant.ADMIN_ROLE;
import static com.max.usercebter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author max
 * 8/4/25
 */

@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin // 允许跨域访问
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){

        if (userRegisterRequest == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)) {
            return null;
        }
        final long res = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);

        return ResultUtils.success(res);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        log.info("Login request received: {}", userLoginRequest);
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PRAMS_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)) {
            return ResultUtils.error(ErrorCode.PRAMS_ERROR);
        }
        final User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null) {
            return null;
        }

        final int i = userService.userLogout(request);
        return ResultUtils.success(i);

    }

    @GetMapping ("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        final Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // todo 检验用户是否合法
        final User user = userService.getById(userId);
        final User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }


    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers( String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            userQueryWrapper.like("username", username);
        }
        final List<User> usesrList = userService.list(userQueryWrapper);
        final List<User> collect = usesrList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(collect);


    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(Long id, HttpServletRequest request) {

        if (!isAdmin(request)) {
            return ResultUtils.success(false);
        }

        if (id <= 0) {
            return null;
        }

        final boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
