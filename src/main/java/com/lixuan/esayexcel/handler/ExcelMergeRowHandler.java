package com.lixuan.esayexcel.handler;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.lixuan.esayexcel.dto.excel.MergeColumnByRowDto;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 合并单元格 根据行
 *
 * @author lixuan
 * @date 2022-09-27 17:55
 */
public class ExcelMergeRowHandler implements RowWriteHandler {

	private final MergeColumnByRowDto mergeColumnByRowDto;

	/**
	 * 第一行
	 */
	private int firstRow;
	/**
	 * 最后一行
	 */
	private int lastRow;

	/**
	 * 合并列数
	 */
	private final AtomicInteger mergeCount = new AtomicInteger(1);

	private static final ThreadPoolExecutor asyncMergeSheetExecutor;

	static {
		asyncMergeSheetExecutor = new ThreadPoolExecutor(
			1,
			1,
			0,
			TimeUnit.SECONDS,
			new ArrayBlockingQueue<>(1),
			ThreadUtil.newNamedThreadFactory("pool-mergeSheet-", false),
			new ThreadPoolExecutor.CallerRunsPolicy()
		);
	}

	public ExcelMergeRowHandler(MergeColumnByRowDto mergeColumnByRowDto) {
		this.mergeColumnByRowDto = mergeColumnByRowDto;
	}


	@Override
	public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
		if (mergeColumnByRowDto == null) {
			return;
		}
		//当前行
		int curRowIndex = row.getRowNum();
		//每一行的最大列数
		short lastCellNum = row.getLastCellNum();

		if (curRowIndex == 1) {
			//赋初值 第一行
			firstRow = curRowIndex;
		}
		//开始合并位置 （如果当前行 > 开始合并行位置，并且当前行的第一列不为空）
		if (curRowIndex > mergeColumnByRowDto.getMergeRowIndex() && StrUtil.isNotBlank(row.getCell(0).getStringCellValue())) {
			// 循环每一列
			for (int i = 0; i < lastCellNum; i++) {
				// 判断是否为需要合并的对象
				if (i == mergeColumnByRowDto.getMergeColumnIndex()[i]) {
					// 当前行号 当前行对象 合并的标识位
					mergeWithPrevAnyRow(writeSheetHolder.getSheet(), curRowIndex, row, mergeColumnByRowDto.getSignNum());
					break;
				}
			}
		}
	}

	/**
	 * 与上一个行比较合并
	 *
	 * @param sheet       sheet
	 * @param curRowIndex 当前行下标
	 * @param row         行
	 * @param signNum     合并列标识
	 * @author lixuan
	 * @date 2022/9/28 9:14
	 **/
	private void mergeWithPrevAnyRow(Sheet sheet, int curRowIndex, Row row, int signNum) {
		// 当前行的合并标识
		Object currentData = row.getCell(signNum).getCellType() == CellType.STRING ? row.getCell(signNum).getStringCellValue() : row.getCell(signNum).getNumericCellValue();
		// 上一行
		Row preRow = row.getSheet().getRow(curRowIndex - 1);
		// 上一行的合并标识
		Object preData = preRow.getCell(signNum).getCellType() == CellType.STRING ? preRow.getCell(signNum).getStringCellValue() : preRow.getCell(signNum).getNumericCellValue();
		// 判断两个标识是否相同
		boolean curEqualsPre = currentData.equals(preData);
		// 判断前一个和后一个相同 并且 标识位相同
		if (curEqualsPre) {
			lastRow = curRowIndex;
			// 相同 合并数+1
			mergeCount.incrementAndGet();
		}
		// excel过程中合并 不相同并且合并数>1
		if (!curEqualsPre && mergeCount.get() > 1) {
			// 进行合并操作
			mergeSheet(firstRow, lastRow, mergeColumnByRowDto.getMergeColumnIndex(), sheet);
			// 设置合并数=1 供下一行使用
			mergeCount.set(1);
		}

		// excel结尾处合并
		if (mergeCount.get() > 1 && mergeColumnByRowDto.getTotal() == curRowIndex) {
			mergeSheet(firstRow, lastRow, mergeColumnByRowDto.getMergeColumnIndex(), sheet);
			mergeCount.set(1);
		}

		if (!curEqualsPre) {
			firstRow = curRowIndex;
		}

	}


	/**
	 * 合并单元格
	 *
	 * @param firstRow         第一行
	 * @param lastRow          最后一行
	 * @param mergeColumnIndex 合并列
	 * @param sheet            sheet
	 * @author lixuan
	 * @date 2022/9/28 9:21
	 **/
	private void mergeSheet(int firstRow, int lastRow, int[] mergeColumnIndex, Sheet sheet) {
		// 执行到末尾一两行时会出现问题
//		asyncMergeSheetExecutor.execute(() -> {});
		for (int colNum : mergeColumnIndex) {
			CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, colNum, colNum);
			sheet.addMergedRegion(cellRangeAddress);
		}

	}
}

