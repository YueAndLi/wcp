package com.farm.quartz.adapter;

import com.farm.quartz.server.FarmQzSchedulerManagerInter;
import com.farm.util.spring.BeanFactory;
import com.farm.web.task.ServletInitJobInter;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;

public class StartSysTask implements ServletInitJobInter {
    private static final Logger log = Logger.getLogger(StartSysTask.class);

    @Override
    public void execute(ServletContext context) {
        FarmQzSchedulerManagerInter aloneIMP = (FarmQzSchedulerManagerInter) BeanFactory
                .getBean("farmQzSchedulerManagerImpl");
        try {
            aloneIMP.start();
            log.info("started '任务调度'");
        } catch (Exception e) {
            log.error(e);
        }
    }

}
