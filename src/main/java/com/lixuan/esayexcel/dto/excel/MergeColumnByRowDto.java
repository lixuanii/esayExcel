package com.lixuan.esayexcel.dto.excel;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 合并单元格 根据行
 *
 * @author lixuan
 * @date 2022-09-27 19:08
 */
@Accessors(chain = true)
@Data
public class MergeColumnByRowDto {

	/**
	 * 从哪一行开始合并 默认1
	 */
	private int mergeRowIndex = 1;

	/**
	 * excel合并的列
	 */
	private int[] mergeColumnIndex;

	/**
	 * 合并列 唯一标识
	 */
	private int signNum;

	/**
	 * 总行数
	 */
	private long total;
}
