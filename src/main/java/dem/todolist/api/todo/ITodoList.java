package dem.todolist.api.todo;

import com.dem.chestlib.api.properties.IPropertyContainer;
import com.dem.chestlib.api.storage.INBTSaveLoad;
import dem.todolist.api.todo.task.ITask;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Optional;
import java.util.UUID;

public interface ITodoList extends INBTSaveLoad<NBTTagCompound>, IPropertyContainer {

    Optional<UUID> clearTodolist();

    Optional<UUID> removeTask(UUID uuidTask);
    Optional<UUID> removeTask(ITask Task);
    void attachTask(UUID uuid);
}
