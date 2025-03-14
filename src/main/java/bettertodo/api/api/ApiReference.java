package bettertodo.api.api;

import bettertodo.api.todo.ITodoList;
import bettertodo.api.todo.task.ITaskDatabase;

public class ApiReference {

    public static final ApiKey<ITaskDatabase> TASK_DB = new ApiKey<>();
    public static final ApiKey<ITodoList> TODOLIST_DB = new ApiKey<>();
}
