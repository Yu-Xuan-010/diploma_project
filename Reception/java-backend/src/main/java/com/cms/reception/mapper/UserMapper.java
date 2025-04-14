package com.cms.reception.mapper;

import com.cms.reception.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.Date;

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

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Insert("INSERT INTO user (username, password, email, nickname, avatar, create_time, update_time, role, major_id, college_id, phone_number, real_name, status, last_login, login_attempts, gender, birthday, address) " +
            "VALUES (#{username}, #{password}, #{email}, #{nickname}, #{avatar}, #{createTime}, #{updateTime}, #{role}, #{majorId}, #{collegeId}, #{phoneNumber}, #{realName}, #{status}, #{lastLogin}, #{loginAttempts}, #{gender}, #{birthday}, #{address})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE user SET " +
            "username = #{username}, " +
            "email = #{email}, " +
            "nickname = #{nickname}, " +
            "avatar = #{avatar}, " +
            "update_time = #{updateTime}, " +
            "major_id = #{majorId}, " +
            "college_id = #{collegeId}, " +
            "phone_number = #{phoneNumber}, " +
            "real_name = #{realName}, " +
            "status = #{status}, " +
            "last_login = #{lastLogin}, " +
            "login_attempts = #{loginAttempts}, " +
            "gender = #{gender}, " +
            "birthday = #{birthday}, " +
            "address = #{address} " +
            "WHERE id = #{id}")
    int updateUser(User user);

    @Update("UPDATE user SET password = #{newPassword} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("newPassword") String newPassword);

    @Update("UPDATE user SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Update("UPDATE user SET login_attempts = #{loginAttempts}, last_login = #{lastLogin} WHERE id = #{id}")
    int updateLoginInfo(@Param("id") Long id, @Param("loginAttempts") Integer loginAttempts, @Param("lastLogin") Date lastLogin);

    @Update("UPDATE user SET role = #{role} WHERE id = #{userId}")
    void updateUserRole(@Param("userId") Long userId, @Param("role") String role);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User getUserByUsername(@Param("username") String username);
}
