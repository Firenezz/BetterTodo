package dem.todolist.api.api;

import dem.todolist.api.todo.ITodoList;
import dem.todolist.api.todo.task.ITaskDatabase;

public class ApiReference {

    public static final ApiKey<ITaskDatabase> TASK_DB = new ApiKey<>();
    public static final ApiKey<ITodoList> TODOLIST_DB = new ApiKey<>();
}
