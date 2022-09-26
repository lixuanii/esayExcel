package com.lixuan.esayexcel.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * sheet 写入设置
 *
 * @author lixuan
 * @date 2022-09-26 16:04
 */
@AllArgsConstructor
public class SheetHandler implements SheetWriteHandler {

	/**
	 * 隐藏索引
	 */
	private List<Integer> hiddenIndices;

	/**
	 * 工作表创建后操作
	 *
	 * @param writeWorkbookHolder book
	 * @param writeSheetHolder    sheet
	 * @author lixuan
	 * @date 2022/9/26 16:15
	 **/
	@Override
	public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
		Workbook workbook = writeWorkbookHolder.getWorkbook();
		Sheet sheet = workbook.getSheetAt(0);
		//设置隐藏列
		hidden(sheet);
	}

	/**
	 * 设置隐藏列
	 *
	 * @param sheet 工作薄
	 * @author lixuan
	 * @date 2022/9/26 16:06
	 **/
	private void hidden(Sheet sheet) {
		if (!CollUtil.isEmpty(hiddenIndices)) {
			// 设置隐藏列
			for (Integer hiddenIndex : hiddenIndices) {
				sheet.setColumnHidden(hiddenIndex, true);
			}
		}
	}

}
