package com.cms.reception.repository;

/**
 * &#064;BelongsProject: CMS
 * &#064;BelongsPackage: com.cms.reception.repository
 * &#064;Author: gaogao
 * &#064;CreateTime: 2025-03-12  19:24
 * &#064;Description: TODO
 * &#064;Version: 1.0
 */
import com.cms.reception.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);  // 根据用户名查询用户
}
