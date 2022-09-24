package com.lixuan.esayexcel.convert;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.lixuan.esayexcel.convert.base.IntegerConverter;

/**
 * @author lixuan
 * @date 2022-09-21 14:58
 */
public class UserStateConverter extends IntegerConverter {

	@Override
	public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
											   GlobalConfiguration globalConfiguration) {

		//"1:在职,2:离职,3:休假"

		String userState = "在职";
		if (value == 2) {
			userState = "离职";
		} else if (value == 3) {
			userState = "休假";
		}
		return new WriteCellData<>(userState);
	}
}
