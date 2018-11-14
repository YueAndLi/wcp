package com.farm.web.task.impl;

import com.farm.web.task.ServletInitJobInter;
import com.farm.web.task.TaskListInter;

import java.util.List;

public class TaskListImpl implements TaskListInter {
    private List<ServletInitJobInter> tasks;

    @Override
    public List<ServletInitJobInter> getTasks() {
        return tasks;
    }

    public void setTasks(List<ServletInitJobInter> tasks) {
        this.tasks = tasks;
    }

}
