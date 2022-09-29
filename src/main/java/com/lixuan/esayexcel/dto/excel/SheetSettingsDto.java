package com.lixuan.esayexcel.dto.excel;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * sheet设置
 *
 * @author lixuan
 * @date 2022-09-27 10:37
 */
@Accessors(chain = true)
@Data
public class SheetSettingsDto {

	/**
	 * 冻结标识 (第一行第一列)
	 */
	private boolean freezePaneFlag;

	/**
	 * 隐藏索引
	 */
	private Integer[] hiddenIndices;

	/**
	 * 下拉框
	 * key:index  value:下拉选项
	 */
	private Map<Integer, String[]> mapDropDown;
	/**
	 * 下拉结束行
	 */
	private Integer dropDownLastRow;

}
