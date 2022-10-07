package com.lixuan.esayexcel.dto.excel;

import com.lixuan.esayexcel.service.excel.ExcelWriteService;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * T 导出类
 *
 * @author lixuan
 * @date 2022-09-22 10:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BigDataExcelWriteDto<T> extends ExcelWriteDto<T> {

	/**
	 * 总共数量
	 */
	private long count;

	/**
	 * service
	 */
	private ExcelWriteService<T> excelWriteService;

	/**
	 * 每页查询数量
	 */
	private long size;

	/**
	 * 支持最大数
	 */
	private Integer maxSize;




}
