package com.lixuan.esayexcel.convert.base;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;

/**
 * int 转换
 * @author lixuan
 * @date 2022-09-21 14:56
 */
public class IntegerConverter implements Converter<Integer> {

	@Override
	public Class<Integer> supportJavaTypeKey() {
		return Integer.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}
}
