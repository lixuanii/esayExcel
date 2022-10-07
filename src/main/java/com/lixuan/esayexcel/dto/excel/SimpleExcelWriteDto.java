package com.lixuan.esayexcel.dto.excel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * T Service类型
 *
 * @author lixuan
 * @date 2022-09-22 10:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SimpleExcelWriteDto<T> extends ExcelWriteDto<T> {

	private List<T> data;

	/**
	 * 模板标识
	 */
	private boolean templateFlag;
}
