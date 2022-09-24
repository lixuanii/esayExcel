package com.lixuan.esayexcel.convert;

import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.lixuan.esayexcel.convert.base.IntegerConverter;

/**
 * 启用禁用
 *
 * @author lixuan
 * @date 2022-09-21 15:09
 */
public class EnableDisabledStatusConverter extends IntegerConverter {

    @Override
    public WriteCellData<String> convertToExcelData(Integer value, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        return new WriteCellData<>(value == 1 ? "启用" : "停用");
    }

}
