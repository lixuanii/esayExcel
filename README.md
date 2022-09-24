# esayExcel
EasyExcelUtli，ExcelUtil，Excel统一导入导出实现方案

# 基于EasyExcel提供统一导入导出工具类

## 什么情况下使用这个工具类？
在EasyExcel已经帮我们做了导入导出处理情况下，但是在实际业务中可能还存在一个问题是:
“ **如果一个项目中需要支持很多个不同的导入导出呢？** ”实际上的流程其实都是差不多的，只是导出的内容不一致而已，那应该如何抽取这部分流程呢？

所以本demo提供了一个如何抽取这部分流程的**解决思路**。

## excel导出流程
1. 导出操作保存至文件导出表中 fileExport 状态为“导出中”
2. 根据条件，查询需要导出的结果
3. 结果写入excel
   1. 写入成功后上传至oss，fileExport表中更新导出状态为“导出成功”，并且记录oss文件路径
   2. 导入失败，fileExport更新导出状态为 “导出失败” 并记录失败原因。

## 如何抽取？
具体实现请查看 EasyExcelUtils中的 exportAndUpload

## 如何使用？

案例：

* 简单导出 查看 UserServiceImpl.exportExcel
* 分页导出 查看 CustomerServiceImpl.exportExcel
