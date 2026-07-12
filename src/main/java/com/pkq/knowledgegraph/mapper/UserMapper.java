package com.pkq.knowledgegraph.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pkq.knowledgegraph.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承了
}