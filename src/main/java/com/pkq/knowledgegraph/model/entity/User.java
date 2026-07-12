package com.pkq.knowledgegraph.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")                        // 指定数据库表名
public class User {

    @TableId(type = IdType.AUTO)              // 主键自增
    private Long id;

    private String username;

    private String password;

    private String email;

    private String avatarUrl;

    @TableLogic                               // 逻辑删除
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)      // 插入时自动填充
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充
    private LocalDateTime updatedAt;
}