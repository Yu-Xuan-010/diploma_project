 package com.cms.reception.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.reception.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}