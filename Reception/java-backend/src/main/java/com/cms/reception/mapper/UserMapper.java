package com.cms.reception.mapper;

import com.cms.reception.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * &#064;BelongsProject: diploma_project
 * &#064;BelongsPackage: com.cms.reception.mapper
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-04-05  12:43
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO user (username, password, email) VALUES (#{username}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);
}
