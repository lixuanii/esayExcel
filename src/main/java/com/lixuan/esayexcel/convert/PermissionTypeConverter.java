package com.lixuan.esayexcel.convert;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.lixuan.esayexcel.convert.base.IntegerConverter;

/**
 * 用户权限类型  1:普通用户 2:个性化  3:未授权用户
 *
 * @author lixuan
 * @date 2022-09-21 15:10
 */
public class PermissionTypeConverter extends IntegerConverter {

	@Override
	public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
		// 1:普通用户,2:个性化,3:未授权用户
		String permissionType = "普通用户";
		if (value == 2) {
			permissionType = "个性化";
		} else if (value == 3) {
			permissionType = "未授权用户";
		}
		return new WriteCellData<>(permissionType);
	}
}
