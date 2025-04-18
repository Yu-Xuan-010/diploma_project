-- 删除旧的学习记录表（如果存在）
DROP TABLE IF EXISTS user_study_record;

-- 创建新的学习记录表
CREATE TABLE user_study_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    lesson_id BIGINT NOT NULL COMMENT '课时ID',
    first_study_time DATETIME NOT NULL COMMENT '首次学习时间',
    last_study_time DATETIME NOT NULL COMMENT '最后学习时间',
    total_duration INT NOT NULL DEFAULT 0 COMMENT '总学习时长（秒）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_lesson` (`user_id`, `lesson_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户学习记录表'; 