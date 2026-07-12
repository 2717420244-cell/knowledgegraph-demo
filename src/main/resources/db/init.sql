CREATE DATABASE IF NOT EXISTS knowledge_graph
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE knowledge_graph;

CREATE TABLE IF NOT EXISTS sys_user (
                                        id          BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        username    VARCHAR(64)  NOT NULL UNIQUE,
    password    VARCHAR(256) NOT NULL,
    email       VARCHAR(128),
    avatar_url  VARCHAR(512),
    deleted     TINYINT      DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;