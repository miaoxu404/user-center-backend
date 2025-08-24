package com.max.usercebter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.usercebter.common.ErrorCode;
import com.max.usercebter.domain.User;
import com.max.usercebter.exception.BusinessException;
import com.max.usercebter.mapper.UserMapper;
import com.max.usercebter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.max.usercebter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户实现服务类
 * @author boysimple
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2025-08-02 17:40:27
 */

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "max";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR, "用户账户过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR, "用户密码过短");
        }
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR, "星球编号过长");
        }

        //账户不能包含特殊字符
        // 匹配英文字母与数字的内容,放行
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(userAccount);
        if (matcher.matches()) {
            System.out.println("合法账户名");
        } else {
            System.out.println("包含特殊字符");
            throw new BusinessException(ErrorCode.PRAMS_ERROR, "用户账户不合法");
        }

        //密码和校验密码不同
        if(!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR, "两次密码相同");
        }

        //放在最后，性能优化,尽量只查一次数据库
        //账户不能重复
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account", userAccount);
        Long l = baseMapper.selectCount(queryWrapper);
        long count = this.count(queryWrapper);
         if (count > 0) {
             throw new BusinessException(ErrorCode.PRAMS_ERROR, "用户账户已注册");
         }

        //行星编号不能重复
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("planetCode", planetCode);
        l = baseMapper.selectCount(queryWrapper);
        count = this.count(queryWrapper);
         if (count > 0) {
             throw new BusinessException(ErrorCode.PRAMS_ERROR, "星球编号已绑定");
         }

//        //2.MD5哈希
//        String SALT = "max";
//        final String hex = DigestUtils.md5DigestAsHex((SALT + "123").getBytes());
//        System.out.println(hex);

        //2.更推荐SHA256哈希
        String hashedPassword = DigestUtils.sha256Hex(SALT + userPassword);
        //3.插入数据
        User user = new User();
        user.setAccount(userAccount);
        user.setUserPassword(hashedPassword);
        user.setPlanetCode(planetCode);
        final boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "服务器失败，用户账户未注册成功");}

        return user.getId();

    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "账户密码不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR, "用户账户过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR, "用户密码过短");
        }

        //账户不能包含特殊字符
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9_\\u4e00-\\u9fa5]");
        Matcher matcher = pattern.matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR, "用户账户包含特殊字符");

        } else {
            System.out.println("合法账户名");
        }

        String hashedPassword = DigestUtils.sha256Hex(SALT + userPassword);

        //查询用户是否存在
        final QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("account", userAccount);
        userQueryWrapper.eq("userPassword", hashedPassword);
        final User user = this.getOne(userQueryWrapper);
        //用户不存在
        if (user == null) {
            log.info("user login failed, user cannot match userPassword");
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户账户不存在");
        }

        //3.做用户脱敏
        final User safetyUser = getSafetyUser(user);

        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    @Override
    public User getSafetyUser(User user) {
        if (user == null) {
            return null;
        }
        final User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setGender(user.getGender());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setTel(user.getTel());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setStatus(user.getStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setIsDelete(user.getIsDelete());
        safetyUser.setAccount(user.getAccount());
        safetyUser.setAccount(user.getPlanetCode());

        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        return 1;
    }
}




