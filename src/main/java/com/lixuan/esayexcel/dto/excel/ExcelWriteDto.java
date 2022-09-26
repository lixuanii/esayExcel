package com.lixuan.esayexcel.dto.excel;

import com.lixuan.esayexcel.enums.OaSystemModuleNameEnum;
import lombok.Data;

/**
 * excel 导出dto
 *
 * @author lixuan
 * @date 2022-09-21 13:38
 */
@Data
public class ExcelWriteDto<T,R> {

	/**
	 * 导出类
	 */
	private Class<T> clazz;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 工作薄name
	 */
	private String sheetName;
	/**
	 * 模块
	 */
	private OaSystemModuleNameEnum model;
	/**
	 * 真实名
	 */
	private String realName;
	/**
	 * 是否需要审核
	 */
	private boolean verifyFlag = true;
	/**
	 * 文件类型
	 */
	private String fileType = "xlsx";

	/**
	 * 合并单元格标识
	 */
	private boolean mergeColumnFlag;
	/**
	 * 合并列
	 */
	private int[] mergeColumnIndex;
	/**
	 * 从哪一行开始合并 默认第一行
	 */
	private int mergeRowIndex = 1;


	public int isVerifyFlag() {
		return verifyFlag ? 1 : 0;
	}
}
