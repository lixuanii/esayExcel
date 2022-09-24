package com.lixuan.esayexcel.dto.excel;

import lombok.Data;

import java.io.InputStream;

/**
 * 写入excel结果
 *
 * @author lixuan
 * @date 2022-09-22 11:26
 */
@Data
public class WriteExcelResult {
    /**
     * 错误原因
     */
    private String errMsg;

    /**
     * 流
     */
    private InputStream inputStream;
}
