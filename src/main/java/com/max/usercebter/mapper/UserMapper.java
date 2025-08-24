package com.max.usercebter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.max.usercebter.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author boysimple
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-08-05 18:00:33
* @Entity generator.domain.User
*/

@Mapper
public interface UserMapper extends BaseMapper<User> {

}




