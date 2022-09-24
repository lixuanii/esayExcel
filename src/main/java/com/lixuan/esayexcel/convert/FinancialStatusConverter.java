package com.lixuan.esayexcel.convert;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.lixuan.esayexcel.convert.base.IntegerConverter;

/**
 * @author lixuan
 * @date 2022-09-23 15:07
 */
public class FinancialStatusConverter extends IntegerConverter {

	@Override
	public WriteCellData<?> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
											   GlobalConfiguration globalConfiguration) {
		//0:正常,1:欠费,2:禁用
		String financialStatus = "正常";
		if (value == 1) {
			financialStatus = "欠费";
		} else if (value == 2) {
			financialStatus = "禁用";
		}
		return new WriteCellData<>(financialStatus);
	}
}
