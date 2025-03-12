package todolist.api.todo;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import chestlib.api.properties.IPropertyContainer;
import chestlib.api.storage.INBTSaveLoad;
import todolist.api.todo.task.ITask;

public interface ITodoList extends INBTSaveLoad<NBTTagCompound>, IPropertyContainer {

    Optional<UUID> clearTodolist();

    Optional<UUID> removeTask(UUID uuidTask);

    Optional<UUID> removeTask(ITask Task);

    void attachTask(UUID uuid);

}
