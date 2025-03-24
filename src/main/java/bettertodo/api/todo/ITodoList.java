package bettertodo.api.todo;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import bettertodo.api.todo.task.ITask;
import chestlib.api.properties.IPropertyContainer;
import chestlib.api.storage.INBTSaveLoad;

public interface ITodoList extends INBTSaveLoad<NBTTagCompound>, IPropertyContainer {

    Optional<UUID> clearTodolist();

    Optional<UUID> removeTask(UUID uuidTask);

    Optional<UUID> removeTask(ITask Task);

    void attachTask(UUID uuid);

}
