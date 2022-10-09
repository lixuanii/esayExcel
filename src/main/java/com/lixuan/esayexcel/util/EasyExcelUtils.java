package com.lixuan.esayexcel.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.lixuan.esayexcel.entity.FileExport;
import com.lixuan.esayexcel.dto.excel.BigDataExcelWriteDto;
import com.lixuan.esayexcel.dto.excel.ExcelWriteDto;
import com.lixuan.esayexcel.dto.excel.SimpleExcelWriteDto;
import com.lixuan.esayexcel.dto.excel.WriteExcelResult;
import com.lixuan.esayexcel.handler.ExcelMergeColHandler;
import com.lixuan.esayexcel.handler.ExcelMergeRowHandler;
import com.lixuan.esayexcel.handler.ExcelWidthStyleStrategy;
import com.lixuan.esayexcel.handler.SheetHandler;
import com.lixuan.esayexcel.handler.WaterMarkHandler;
import com.lixuan.esayexcel.service.excel.ExcelWriteService;
import com.lixuan.esayexcel.service.FileExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 工具类
 *
 * @author lixuan
 * @date 2022-09-21 10:56
 */
@Slf4j
@Component
public class EasyExcelUtils {

    /**
     * 默认最大导出限制数量
     */
    public static final int MAX_SIZE = 10000;

    @Autowired
    private FileExportService fileExportService;

    private static ExecutorService bigDataWriteExecutor;

    static {
        int cpuSize = Runtime.getRuntime().availableProcessors();
        bigDataWriteExecutor = new ThreadPoolExecutor(
                cpuSize + 1,
                cpuSize * 3,
                60,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(200),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

//    public <T, R> void doSomething(ExcelWriteDto<T> dto, FileExport fileExport) {
//
//
//        CompletableFuture.supplyAsync(() -> {
//            WriteExcelResult result = null;
//            if (dto instanceof BigDataExcelWriteDto) {
//                result = this.bigDataWriteExcel((BigDataExcelWriteDto<T, R>) dto);
//            } else if (dto instanceof SimpleExcelWriteDto) {
//                result = this.simpleWriteExcel((SimpleExcelWriteDto<T, R>) dto);
//            }
//            if (result == null) {
//                log.error("EasyExcelUtils.exportAndUpload.WriteExcel 文件导出并上传 文件写入至excel失败,dto:{}", dto);
//            }
//            return result;
//        }).thenApply(result -> {
//            if (result == null) {
//                return fileExport;
//            }
//            if (result.getInputStream() != null) {
//                String filePath = getFilePath(dto.getFileName(), dto.getModel().getModule(), dto.getFileType());
//                if (StrUtil.isBlank(filePath)) {
//                    log.error("EasyExcelUtils.exportAndUpload.getFilePath 文件导出并上传,获取上传路径失败,fileName:{},model:{},fileType:{}", dto.getFileName(), dto.getModel(), dto.getFileType());
//                    return fileExport;
//                }
//                AliYunOssUtil.uploadToOss(filePath, result.getInputStream());
//                fileExport.setFileUrl(AliYunOssUtil.getObjectOssUrl(filePath));
//                fileExport.setOssPath(filePath);
//                fileExport.setExportResult("导出成功");
//                log.info("文件导出成功,path:{}", fileExport.getFileUrl());
//            } else {
//                fileExport.setExportResult("导出失败:" + result.getErrMsg());
//            }
//            return fileExport;
//        }).thenApply(fileExports -> {
//            return fileExportService.updateById(fileExport);
//        }).thenAccept( flag->{
//            return;
//        }).thenCompose(t->{
//            final var t1 = t;
//
//        },ossAsyncServiceExecutor);
//    }


    public <T, R> void exportAndUpload(ExcelWriteDto<T> dto) {
        if (this.checkDtoErr(dto)) {
            return;
        }
        try {
            FileExport fileExport = this.buildSaveFileExport(dto);
            if (!fileExportService.save(fileExport)) {
                log.error("EasyExcelUtils.exportAndUpload.saveFile 文件导出并上传 保存文件失败,fileExport:{}", fileExport);
                return;
            }
            WriteExcelResult result = null;
            if (dto instanceof BigDataExcelWriteDto) {
                result = this.bigDataWriteExcel((BigDataExcelWriteDto<T>) dto);
            } else if (dto instanceof SimpleExcelWriteDto) {
                result = this.simpleWriteExcel((SimpleExcelWriteDto<T>) dto);
            }
            if (result == null) {
                log.error("EasyExcelUtils.exportAndUpload.WriteExcel 文件导出并上传 文件写入至excel失败,dto:{}", dto);
                return;
            }
            if (result.getInputStream() != null) {
                String filePath = getFilePath(dto.getFileName(), dto.getModel().getModule(), dto.getFileType());
                if (StrUtil.isBlank(filePath)) {
                    log.error("EasyExcelUtils.exportAndUpload.getFilePath 文件导出并上传,获取上传路径失败,fileName:{},model:{},fileType:{}", dto.getFileName(), dto.getModel(), dto.getFileType());
                    return;
                }
                AliYunOssUtil.uploadToOss(filePath, result.getInputStream());
                fileExport.setFileUrl(AliYunOssUtil.getObjectOssUrl(filePath));
                fileExport.setOssPath(filePath);
                fileExport.setExportResult("导出成功");
                log.info("文件导出成功,path:{}", fileExport.getFileUrl());
            } else {
                fileExport.setExportResult("导出失败:" + result.getErrMsg());
            }
            fileExportService.updateById(fileExport);
        } catch (Exception e) {
            log.error("文件导出出现异常", e);
        }
    }


    /**
     * 分页导出excel数据
     *
     * @param dto 条件
     * @return WriteExcelResult
     * @author lixuan
     * @date 2022/9/22 17:56
     **/
    private <T> WriteExcelResult bigDataWriteExcel(BigDataExcelWriteDto<T> dto) {
        WriteExcelResult result = new WriteExcelResult();
        if (this.checkDtoErr(dto)) {
            result.setErrMsg(String.format("dto参数异常,dto:%s", dto));
            return result;
        }
        // 根据条件查询出来的总数
        long count = dto.getCount();
        if (count <= 0) {
            result.setErrMsg("未查询到数据");
            return result;
        }
        ExcelWriteService<T> excelWriteService = dto.getExcelWriteService();
        if (excelWriteService == null) {
            log.error("EasyExcelUtils.bigDataWriteExcel.ExcelWriteService isNull");
            return result;
        }
        dto.setMaxSize(dto.getMaxSize() == null || dto.getMaxSize() == 0 ? MAX_SIZE : dto.getMaxSize());
        if (dto.getCount() > dto.getMaxSize()) {
            result.setErrMsg(String.format("导出数据超出最大记录数%s,查询数%s", dto.getMaxSize(), dto.getCount()));
            return result;
        }
        long size = dto.getSize();
        // 总数 / 每页显示的条数 = 分页数(页数、循环次数)
        int page;
        if (count % size == 0) {
            page = Math.toIntExact(count / size);
        } else {
            page = Math.toIntExact(count / size + 1);
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ExcelWriterBuilder builder = EasyExcel.write(outputStream, dto.getClazz())
                    .registerWriteHandler(new ExcelWidthStyleStrategy())
                    .registerWriteHandler(new ExcelMergeColHandler(dto.getMergeColumnByColDto()))
                    .registerWriteHandler(new ExcelMergeRowHandler(dto.getMergeColumnByRowDto()))
                    .registerWriteHandler(new SheetHandler(dto.getSheetDto()));
            if (count < MAX_SIZE) {
                // 数据超过指定值不添加水印，原因是因为添加水印需要将数据写入内存,而数据过多会OOM
                builder.inMemory(true).registerWriteHandler(new WaterMarkHandler(watermark(dto.getModel().getDesc(), dto.getRealName())));
            }
            ExcelWriter excelWriter = builder.build();
            List<Callable<List<T>>> task = new CopyOnWriteArrayList<>();
            for (int i = 0; i < page; i++) {
                int finalI = i;
                task.add(() -> excelWriteService.getPageList(finalI + 1, size));
            }
            List<Future<List<T>>> futures = bigDataWriteExecutor.invokeAll(task);
            WriteSheet writeSheet = EasyExcel.writerSheet(dto.getSheetName()).build();
            List<T> list;
            for (int i = 0; i < page; i++) {
                list = futures.get(i).get();
                excelWriter.write(list, writeSheet);
            }
            task.clear();
            futures.clear();
            excelWriter.close();
            result.setInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (Exception e) {
            result.setErrMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


//    /**
//     * 分页导出excel数据
//     *
//     * @param dto 条件
//     * @return WriteExcelResult
//     * @author lixuan
//     * @date 2022/9/22 17:56
//     **/
//    private <T> WriteExcelResult bigDataWriteExcel(BigDataExcelWriteDto<T> dto) {
//        WriteExcelResult result = new WriteExcelResult();
//        if (this.checkDtoErr(dto)) {
//            result.setErrMsg(String.format("dto参数异常,dto:%s", dto));
//            return result;
//        }
//        // 根据条件查询出来的总数
//        long count = dto.getCount();
//        if (count <= 0) {
//            result.setErrMsg("未查询到数据");
//            return result;
//        }
//        ExcelWriteService<T> excelWriteService = dto.getExcelWriteService();
//        if (excelWriteService == null) {
//            log.error("EasyExcelUtils.bigDataWriteExcel.ExcelWriteService isNull");
//            return result;
//        }
//        dto.setMaxSize(dto.getMaxSize() == null || dto.getMaxSize() == 0 ? MAX_SIZE : dto.getMaxSize());
//        if (dto.getCount() > dto.getMaxSize()) {
//            result.setErrMsg(String.format("导出数据超出最大记录数%s,查询数%s", dto.getMaxSize(), dto.getCount()));
//            return result;
//        }
//        long size = dto.getSize();
//        // 分页数(页数、循环次数) = 总数 / 每页显示的条数
//        int page;
//        if (count % size == 0) {
//            page = Math.toIntExact(count / size);
//        } else {
//            page = Math.toIntExact(count / size + 1);
//        }
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            ExcelWriterBuilder builder = EasyExcel.write(outputStream, dto.getClazz())
//                    .inMemory(true)
//                    .registerWriteHandler(new WaterMarkHandler(watermark(dto.getModel().getDesc(), dto.getRealName())))
//                    .registerWriteHandler(new ExcelWidthStyleStrategy())
//                    .registerWriteHandler(new ExcelMergeColHandler(dto.getMergeColumnByColDto()))
//                    .registerWriteHandler(new ExcelMergeRowHandler(dto.getMergeColumnByRowDto()))
//                    .registerWriteHandler(new SheetHandler(dto.getSheetDto()));
//            ExcelWriter excelWriter = builder.build();
//            WriteSheet sheet = EasyExcel.writerSheet(dto.getSheetName())
//                    .head(dto.getClazz())
//                    .build();
//            // 不可以丢入list中，数据量太大直接OOM
//            List<Callable<List<T>>> tasks = new ArrayList<>(page);
//            for (int i = 1; i <= page; i++) {
//                int finalI = i;
//                tasks.add(() -> excelWriteService.getPageList(finalI, size));
//                excelWriter.write(() -> excelWriteService.getPageList(finalI, size), sheet);
//            }
//            List<Future<List<T>>> futures = bigDataWriteExecutor.invokeAll(tasks);
//            if (CollUtil.isEmpty(futures)) {
//                excelWriter.close();
//                result.setErrMsg("导出失败,查询记录为空");
//                return result;
//            }
//            futures.stream().filter(Objects::nonNull).forEach(item -> {
//                try {
//                    excelWriter.write(item.get(), sheet);
//                } catch (InterruptedException | ExecutionException e) {
//                    excelWriter.close();
//                    result.setErrMsg("导出失败,写入excel出现异常,e" + e.getMessage());
//                    throw new RuntimeException(e);
//                }
//            });
//            excelWriter.close();
//            result.setInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
//        } catch (Exception e) {
//            result.setErrMsg(e.getMessage());
//            e.printStackTrace();
//        }
//        return result;
//    }


    /**
     * 写入到excel
     *
     * @return InputStream
     * @author lixuan
     * @date 2022/9/21 11:11
     **/
    private <T> WriteExcelResult simpleWriteExcel(SimpleExcelWriteDto<T> dto) {
        WriteExcelResult result = new WriteExcelResult();
        if (this.checkDtoErr(dto)) {
            result.setErrMsg(String.format("dto参数异常,dto:%s", dto));
            return result;
        } else if (CollUtil.isEmpty(dto.getData()) && !dto.isTemplateFlag()) {
            result.setErrMsg("列表为空");
            return result;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 构建excel写入
            EasyExcel.write(outputStream, dto.getClazz())
                    .inMemory(true)
                    .registerWriteHandler(new WaterMarkHandler(watermark(dto.getModel().getDesc(), dto.getRealName())))
                    .registerWriteHandler(new ExcelWidthStyleStrategy())
                    .registerWriteHandler(new SheetHandler(dto.getSheetDto()))
                    .registerWriteHandler(new ExcelMergeColHandler(dto.getMergeColumnByColDto()))
                    .registerWriteHandler(new ExcelMergeRowHandler(dto.getMergeColumnByRowDto()))
                    .includeColumnFieldNames(this.includeColumnFiledNames(dto.getClazz()))
                    .sheet(dto.getSheetName()).doWrite(dto.getData());

            result.setInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
            return result;
        } catch (Exception e) {
            result.setErrMsg(e.getMessage());
            log.error("数据写入到excel出现异常", e);
            return result;
        }
    }

    /**
     * 获取文件上传路径
     *
     * @param fileName 文件名  xx导出
     * @param module   模块名 order
     * @param fileType xlsx
     * @return String
     * @author lixuan
     * @date 2022/9/23 10:34
     **/
    private String getFilePath(String fileName, String module, String fileType) {
        if (StrUtil.isBlank(fileName) || StrUtil.isBlank(module)) {
            log.error("EasyExcelUtils.getFilePath.error,fileName:{},module:{}", fileName, module);
            return null;
        }
        if (StrUtil.isBlank(fileType)) {
            fileType = "xlsx";
        }
        String realFileName = String.format("%s-%s.%s", fileName, this.getTimeRandom(), fileType);
        // 生成上传文件名(模块名称+类型+随机数+原文件名)
        return "upload/" + "excelExport/" + module + "/" + fileType + "/" + realFileName;
    }


    /**
     * 获取指定列 导出
     *
     * @param clazz 导出clazz
     * @return Set<String> 列
     * @author lixuan
     * @date 2022/9/23 15:52
     **/
    private <T> Set<String> includeColumnFiledNames(Class<T> clazz) {
        Set<String> includeColumnFiledNames = new HashSet<>();
        Field[] fields = ReflectUtil.getFields(clazz);
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                includeColumnFiledNames.add(field.getName());
            }
        }
        return includeColumnFiledNames;
    }


    /**
     * 水印
     *
     * @param desc     模块描述
     * @param operator 操作人
     * @return 水印内容
     */
    private static String watermark(String desc, String operator) {
        if (StrUtil.isBlank(desc) || StrUtil.isBlank(operator)) {
            return "安骏物流";
        }
        return "安骏物流_" + desc + "_" + operator;
    }


    /**
     * 校验dto
     *
     * @param dto 入参
     * @return boolean  true有错误，false无错误
     * @author lixuan
     * @date 2022/9/22 17:56
     **/
    private <T> boolean checkDtoErr(ExcelWriteDto<T> dto) {
        if (dto == null) {
            log.error("EasyExcelUtils.checkDto dto isNull,导出-->dto为空");
            return true;
        } else if (StrUtil.isBlank(dto.getFileName()) || dto.getClazz() == null) {
            log.error("EasyExcelUtils.checkDto fileName or clazz isNull,导出-->文件名或者clazz为空");
            return true;
        } else if (dto.getModel() == null || StrUtil.isBlank(dto.getModel().getModule())) {
            log.error("EasyExcelUtils.checkDto model isNull,导出-->模块为空");
            return true;
        } else if (StrUtil.isBlank(dto.getRealName())) {
            log.error("EasyExcelUtils.checkDto realName isNull,导出-->操作人name为空");
            return true;
        } else if (!Objects.equals(dto.getFileType(), "xls") && !Objects.equals(dto.getFileType(), "xlsx")) {
            log.error("EasyExcelUtils.checkDto fileType error,导出-->文件类型异常,fileType:{}", dto.getFileType());
            return true;
        }
        return false;
    }

    /**
     * 构建文件导出实体
     *
     * @param dto 导出入参
     * @return FileExport
     * @author lixuan
     * @date 2022/9/23 11:59
     **/
    private <T> FileExport buildSaveFileExport(ExcelWriteDto<T> dto) {
        FileExport fileExport = new FileExport();
        fileExport.setExportResult("文件导出中");
        fileExport.setOperationModule(dto.getModel() != null ? dto.getModel().getDesc() : null);
        fileExport.setIsVerify(dto.isVerifyFlag());
        fileExport.setFileType(dto.getFileType());
        fileExport.setOperator(StrUtil.isNotBlank(dto.getRealName()) ? dto.getRealName() : SpringUtil.getActiveProfile());
        return fileExport;
    }

    /**
     * 时间+随机数
     *
     * @return String  当前时间+3位随机数
     * @author lixuan
     * @date 2022/9/24 21:45
     **/
    public String getTimeRandom() {
        String dataTime = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATETIME_PATTERN);
        int randomInt = RandomUtil.randomInt(100, 999);
        return dataTime + "-" + randomInt;
    }

}
