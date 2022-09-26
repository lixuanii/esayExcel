package com.lixuan.esayexcel.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.Sheet;


/**
 * 自定义冻结行和列处理器
 *
 * @author lixuan
 * @date 2022-09-26 11:48
 */
public class CustomFreezeRowColHandler implements SheetWriteHandler {


	@Override
	public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

	}

	@Override
	public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
		Sheet sheet = writeSheetHolder.getSheet();
		// 冻结第一列，从第一行开始冻结
		sheet.createFreezePane(1, 1);
	}

}
