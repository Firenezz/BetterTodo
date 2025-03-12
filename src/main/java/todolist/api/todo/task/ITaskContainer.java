package todolist.api.todo.task;

import java.util.Optional;
import java.util.UUID;

public interface ITaskContainer {

    Optional<UUID> clearTodolist();

    Optional<UUID> removeTask(UUID uuidTask);

    Optional<UUID> removeTask(ITask Task);

    void attachTask(UUID uuid);
}
