package com.demel.todo.api.api;

import com.demel.todo.api.todo.ITaskDatabase;
import com.demel.todo.api.api.ApiKey;

public class ApiReference {
    public static final ApiKey<ITaskDatabase> TASK_DB = new ApiKey<>();
}
