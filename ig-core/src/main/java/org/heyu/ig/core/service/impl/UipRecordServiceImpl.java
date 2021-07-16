package org.heyu.ig.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.heyu.ig.core.UipRequestRecord;
import org.heyu.ig.core.service.UipRecordService;
import org.heyu.ig.core.util.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author heyu
 * @date 2021/3/30
 */
@Slf4j
@Service
public class UipRecordServiceImpl implements UipRecordService {

    private final LinkedBlockingQueue<UipRequestRecord> recordQueue = new LinkedBlockingQueue<>();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void record(){
        Thread thread = new Thread(this::recordLog);
        thread.setDaemon(true);
        executorService.execute(thread);
    }

    @Override
    public void add(UipRequestRecord record) {
        recordQueue.offer(record);
    }

    private void recordLog() {
        while (true) {
            try {
                UipRequestRecord uipRequestRecord = recordQueue.take();
                log.info(JsonUtil.obj2json(uipRequestRecord));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
