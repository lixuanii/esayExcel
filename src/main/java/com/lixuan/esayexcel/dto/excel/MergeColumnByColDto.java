package com.lixuan.esayexcel.dto.excel;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 合并单元格
 *
 * @author lixuan
 * @date 2022-09-27 10:42
 */
@Accessors(chain = true)
@Data
public class MergeColumnByColDto {

	/**
	 * 合并列
	 */
	private Integer[] mergeColumnIndex;
	/**
	 * 从哪一行开始合并 默认第一行
	 */
	private int mergeRowIndex = 1;
}
