package com.lixuan.esayexcel.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自适应宽度
 *
 * @author lixuan
 * @date 2022-09-21 15:29
 */
public class ExcelWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {

	private final Map<Integer, Map<Integer, Integer>> CACHE = new HashMap<>();

	// 适当增加宽度，能避免 数字显示为 * 的问题
	public static final int DEFAULT = 2;

	@Override
	protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
		boolean needSetWidth = isHead || !CollUtil.isEmpty(cellDataList);
		if (needSetWidth) {
			Map<Integer, Integer> maxColumnWidthMap = CACHE.computeIfAbsent(writeSheetHolder.getSheetNo(), k -> new HashMap<>());

			Integer columnWidth = this.dataLength(cellDataList, cell, isHead);
			if (columnWidth >= 0) {
				if (columnWidth > 255) {
					columnWidth = 255;
				}

				Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
				if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
					maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
					writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
				}

			}
		}
	}

	private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
		if (isHead) {
			return cell.getStringCellValue().getBytes().length + DEFAULT;
		} else {
			CellData<?> cellData = cellDataList.get(0);
			CellDataTypeEnum type = cellData.getType();
			if (type == null) {
				return -1;
			} else {
				return switch (type) {
					case STRING -> cellData.getStringValue().getBytes().length + DEFAULT;
					case BOOLEAN -> cellData.getBooleanValue().toString().getBytes().length + DEFAULT;
					case NUMBER -> cellData.getNumberValue().toString().getBytes().length + DEFAULT;
					case DATE -> 20;
					default -> -1;
				};
			}
		}
	}
}
