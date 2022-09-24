package com.lixuan.esayexcel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
@TableName("file_export")
public class FileExport extends Model<FileExport> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作人ID
     */
    private Integer createUser;

    /**
     * 审核人
     */
    private String verifyName;

    /**
     * 审核人ID
     */
    private Integer verifyId;

    /**
     * 是否需要审核(0-否，1-是)
     */
    private Integer isVerify;

    /**
     * 审核状态(0-未审核，1-已审核)
     */
    private Integer verifyStatus;

    /**
     * 操作模块
     */
    private String operationModule;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 访问路径
     */
    private String fileUrl;

    /**
     * 上传oss文件路径
     */
    private String ossPath;

    /**
     * 导出结果
     */
    private String exportResult;

    /**
     * 下载次数
     */
    private Integer downloadsNumber;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
     * 逻辑删除
     */
    private Integer deleted;
}
