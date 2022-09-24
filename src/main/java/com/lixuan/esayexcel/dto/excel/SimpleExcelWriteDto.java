package com.lixuan.esayexcel.dto.excel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * T 导出类
 * R service查询类
 *
 * @author lixuan
 * @date 2022-09-22 10:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SimpleExcelWriteDto<T, R> extends ExcelWriteDto<T, R> {

    /**
     * 导出结果
     */
    private List<R> data;

    /**
     * 模板标识
     */
    private boolean templateFlag;
}
