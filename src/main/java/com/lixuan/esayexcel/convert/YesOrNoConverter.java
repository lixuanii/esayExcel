package com.lixuan.esayexcel.convert;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.lixuan.esayexcel.convert.base.IntegerConverter;

/**
 * 0 否 1是
 *
 * @author lixuan
 * @date 2022-09-21 14:30
 */
public class YesOrNoConverter extends IntegerConverter {


	@Override
	public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
													GlobalConfiguration globalConfiguration) {
		return new WriteCellData<>(value == 1 ? "是" : "否");
	}
}
