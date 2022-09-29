package com.lixuan.esayexcel.dto.excel;

import lombok.Data;

import java.io.InputStream;

/**
 * @author lixuan
 * @date 2022-09-22 11:26
 */
@Data
public class WriteExcelResult {

	private String errMsg;

	private InputStream inputStream;
}
