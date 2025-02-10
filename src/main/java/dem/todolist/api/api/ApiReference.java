package dem.todolist.api.api;

import dem.todolist.api.todo.task.ITaskDatabase;
import dem.todolist.api.todo.ITodoList;

public class ApiReference {

    public static final ApiKey<ITaskDatabase> TASK_DB = new ApiKey<>();
    public static final ApiKey<ITodoList> TODOLIST_DB = new ApiKey<>();
}
