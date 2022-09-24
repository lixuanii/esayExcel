package com.lixuan.esayexcel.service.excel;

import java.util.List;

/**
 * @author lixuan
 * @date 2022-09-22 9:39
 */
public interface ExcelWriteService<T> {


    /**
     * 获取分页列表 (不同业务不同列表)
     *
     * @param current 当前页
     * @param size    页数量
     * @return List<T>
     * @author lixuan
     * @date 2022/9/22 10:32
     **/
    List<T> getPageList(int current, long size);

}
