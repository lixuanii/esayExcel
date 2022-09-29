package com.lixuan.esayexcel.handler;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.lixuan.esayexcel.dto.excel.SheetSettingsDto;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * sheet 写入设置
 *
 * @author lixuan
 * @date 2022-09-26 16:04
 */
@AllArgsConstructor
public class SheetHandler implements SheetWriteHandler {


	private SheetSettingsDto sheetDto;

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
		// 设置隐藏列
		this.hidden(sheet);
		// 下拉框
		this.dropDown(sheet, workbook);
		// 冻结第一列
		this.freezePane(sheet);
	}

	private void freezePane(Sheet sheet) {
		if (sheetDto != null && sheetDto.isFreezePaneFlag()) {
			// 冻结第一列，从第一行开始冻结
			sheet.createFreezePane(1, 1);
		}
	}

	/**
	 * 设置隐藏列
	 *
	 * @param sheet 工作薄
	 * @author lixuan
	 * @date 2022/9/26 16:06
	 **/
	private void hidden(Sheet sheet) {
		if (sheetDto != null && sheetDto.getHiddenIndices() != null && sheetDto.getHiddenIndices().length > 0) {
			// 设置隐藏列
			for (Integer hiddenIndex : sheetDto.getHiddenIndices()) {
				sheet.setColumnHidden(hiddenIndex, true);
			}
		}
	}


	/**
	 * 设置下拉框
	 * (突破下拉框255的限制)
	 *
	 * @param sheet 工作薄
	 * @author lixuan
	 * @date 2022/9/27 10:34
	 **/
	private void dropDown(Sheet sheet, Workbook workbook) {
		if (sheetDto == null || CollUtil.isEmpty(sheetDto.getMapDropDown())) {
			return;
		}
		DataValidationHelper helper = sheet.getDataValidationHelper();
		AtomicInteger index = new AtomicInteger();
		// k 为存在下拉数据集的单元格下表 v为下拉数据集
		sheetDto.getMapDropDown().forEach((k, v) -> {
			// 定义sheet的名称
			String sheetName = "sheet" + k;
			// 创建一个隐藏的sheet
			Sheet hideSheet = workbook.createSheet(sheetName);
			// 从第二个工作簿开始隐藏
			index.getAndIncrement();
			// 设置隐藏
			workbook.setSheetHidden(index.get(), true);
			// 循环赋值（为了防止下拉框的行数与隐藏域的行数相对应，将隐藏域加到结束行之后）
			for (int i = 0, length = v.length; i < length; i++) {
				// i:表示你开始的行数 0表示你开始的列数
				hideSheet.createRow(i).createCell(0).setCellValue(v[i]);
			}
			Name category1Name = workbook.createName();
			category1Name.setNameName(sheetName);
			// $A$1:$A$N代表 以A列1行开始获取N行下拉数据
			category1Name.setRefersToFormula(sheetName + "!$A$1:$A$" + (v.length));
			// 起始行、终止行、起始列、终止列
			CellRangeAddressList addressList = new CellRangeAddressList(1, sheetDto.getDropDownLastRow(), k, k);
			DataValidationConstraint constraint = helper.createFormulaListConstraint(sheetName);
			DataValidation dataValidation = helper.createValidation(constraint, addressList);
			// 阻止输入非下拉选项的值
			dataValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation.setShowErrorBox(true);
			dataValidation.setSuppressDropDownArrow(true);
			dataValidation.createErrorBox("提示", "此值与单元格定义格式不一致");
			// validation.createPromptBox("填写说明：","填写内容只能为下拉数据集中的单位，其他单位将会导致无法入仓");
			sheet.addValidationData(dataValidation);
		});

	}

}
