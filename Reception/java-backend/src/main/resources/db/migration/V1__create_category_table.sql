CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT COMMENT '父分类ID',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态（0：禁用，1：启用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程分类表';

-- 插入一些初始数据
INSERT INTO category (name, parent_id, sort, status) VALUES
('前端开发', NULL, 1, 1),
('后端开发', NULL, 2, 1),
('移动开发', NULL, 3, 1),
('数据库', NULL, 4, 1),
('Vue.js', 1, 1, 1),
('React', 1, 2, 1),
('Spring Boot', 2, 1, 1),
('MySQL', 4, 1, 1),
('MongoDB', 4, 2, 1); 