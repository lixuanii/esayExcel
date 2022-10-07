DROP TABLE IF EXISTS `u_file_export`;
CREATE TABLE `u_file_export`
(
    `id`               int(0)                                                         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `operator`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '操作人',
    `operation_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '操作模块',
    `file_type`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '文件类型',
    `file_url`         varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '访问路径',
    `oss_path`         varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '上传oos路径',
    `verify_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL COMMENT '审核人',
    `verify_id`        int(0)                                                         NULL     DEFAULT NULL COMMENT '审核人id',
    `is_verify`        int(0)                                                         NOT NULL DEFAULT 0 COMMENT '是否需要审核：0-否，1-是',
    `verify_status`    int(0)                                                         NOT NULL DEFAULT 0 COMMENT '审核状态：0-未审核，1-已审核',
    `export_result`    varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '导出结果',
    `create_time`      datetime(0)                                                    NULL     DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `create_user`      int(0)                                                         NULL     DEFAULT NULL COMMENT '操作人',
    `update_time`      datetime(0)                                                    NULL     DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `version`          int(0)                                                         NOT NULL DEFAULT 0 COMMENT '乐观锁',
    `deleted`          int(0)                                                         NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `downloads_number` int(0)                                                         NULL     DEFAULT 0 COMMENT '下载次数',
    `file_name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL COMMENT '文件名',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_export_id_uindex` (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4512
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'oa文件导出'
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;