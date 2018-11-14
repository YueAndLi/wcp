package com.farm.quartz.server;

import com.farm.quartz.domain.FarmQzScheduler;
import com.farm.quartz.domain.FarmQzTask;
import com.farm.quartz.domain.FarmQzTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.text.ParseException;

/**
 * queatz的转化接口
 *
 * @author Administrator
 */
public interface QuartzInter {
    public JobDetail getJobDetail(FarmQzScheduler node, FarmQzTask task) throws ClassNotFoundException;

    public JobDetail getJobDetail(String schedulerId, FarmQzTask task) throws ClassNotFoundException;

    public Trigger getTrigger(FarmQzScheduler node, FarmQzTrigger trigger) throws ParseException;

    public JobKey getJobKey(FarmQzScheduler node, FarmQzTask task);
}