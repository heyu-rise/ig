package org.heyu.ig.core.service;


import org.heyu.ig.core.UipRequestRecord;

/**
 * @author heyu
 * @date 2021/3/30
 */
public interface UipRecordService {

    /**
     * 添加日志信息
     *
     * @param record 日志信息
     */
    void add(UipRequestRecord record);

}
