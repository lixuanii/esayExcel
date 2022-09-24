package com.lixuan.esayexcel.convert;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.lixuan.esayexcel.convert.base.IntegerConverter;

/**
 * 学历
 *
 * @author lixuan
 * @date 2022-09-21 14:14
 */
public class EducationConverter extends IntegerConverter {


	@Override
	public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
													GlobalConfiguration globalConfiguration) {

		String education = switch (value) {
			default -> "小学";
			case 1 -> "初中";
			case 2 -> "高中";
			case 3 -> "大专";
			case 4 -> "本科";
			case 5 -> "硕士";
			case 6 -> "博士";
		};
		//0:小学,1:初中,2:高中,3:大专,4:本科,5:硕士,6:博士
		return new WriteCellData<>(education);
	}

}
