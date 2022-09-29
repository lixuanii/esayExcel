package com.lixuan.esayexcel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.lixuan.esayexcel.dto.excel.MergeColumnByColDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * 合并单元格  根据列
 *
 * @author lixuan
 * @date 2022-09-26 10:26
 */
@AllArgsConstructor
@Data
public class ExcelMergeColHandler implements CellWriteHandler {

	/**
	 * 合并单元格设置
	 */
	private MergeColumnByColDto mergeColumnDto;


	@Override
	public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, WriteCellData<?> cellData, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
		CellWriteHandler.super.afterCellDataConverted(writeSheetHolder, writeTableHolder, cellData, cell, head, relativeRowIndex, isHead);
	}

	@Override
	public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

		boolean flag = mergeColumnDto != null && mergeColumnDto.getMergeColumnIndex() != null && mergeColumnDto.getMergeColumnIndex().length >= 1;
		if (!flag) {
			return;
		}
		//当前行
		int curRowIndex = cell.getRowIndex();
		//当前列
		int curColIndex = cell.getColumnIndex();

		if (curRowIndex > mergeColumnDto.getMergeRowIndex()) {
			for (int columnIndex : mergeColumnDto.getMergeColumnIndex()) {
				if (curColIndex == columnIndex) {
					mergeWithPrevRow(writeSheetHolder, cell, curRowIndex, curColIndex);
					break;
				}
			}
		}
	}

	/**
	 * 当前单元格向上合并
	 *
	 * @param writeSheetHolder  写入sheet
	 * @param cell             当前单元格
	 * @param curRowIndex      当前行
	 * @param curColIndex      当前列
	 */
	private void mergeWithPrevRow(WriteSheetHolder writeSheetHolder, Cell cell, int curRowIndex, int curColIndex) {
		// 当前行的第一个Cell
		Cell curFirstCell = cell.getSheet().getRow(curRowIndex).getCell(0);
		Object curFirstData = curFirstCell.getCellType() == CellType.STRING ? curFirstCell.getStringCellValue() : curFirstCell.getNumericCellValue();
		// 上一行的第一个Cell
		Cell preFirstCell = cell.getSheet().getRow(curRowIndex - 1).getCell(0);
		Object preFirstData = preFirstCell.getCellType() == CellType.STRING ? preFirstCell.getStringCellValue() : preFirstCell.getNumericCellValue();

		// 当前行的id和上一行的id相同则合并前面（mergeColumnRegion+1）列
		if (curFirstData.equals(preFirstData)) {
			Sheet sheet = writeSheetHolder.getSheet();
			List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
			boolean isMerged = false;
			for (int i = 0; i < mergeRegions.size() && !isMerged; i++) {
				CellRangeAddress cellRangeAddr = mergeRegions.get(i);
				// 若上一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元
				if (cellRangeAddr.isInRange(curRowIndex - 1, curColIndex)) {
					sheet.removeMergedRegion(i);
					cellRangeAddr.setLastRow(curRowIndex);
					sheet.addMergedRegion(cellRangeAddr);
					isMerged = true;
				}
			}
			// 若上一个单元格未被合并，则新增合并单元
			if (!isMerged) {
				CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex - 1, curRowIndex, curColIndex, curColIndex);
				sheet.addMergedRegion(cellRangeAddress);
			}
		}
	}

}
