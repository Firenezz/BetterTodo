package bettertodo.todo;

import static bettertodo.api.api.ApiReference.TASK_DB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import bettertodo.api.api.BetterTodoAPI;
import bettertodo.api.todo.ITodoList;
import bettertodo.api.todo.Ownership;
import bettertodo.api.todo.task.ITask;
import bettertodo.api.todo.task.ITaskDatabase;
import chestlib.api.properties.IPropertyContainer;
import chestlib.api.properties.IPropertyType;
import chestlib.storage.properties.PropertyContainer;
import chestlib.util.nbt.NBTUuidUtil;

public class TodoList implements ITodoList {

    IPropertyContainer todoInfo = new PropertyContainer();

    public List<UUID> Tasks = new ArrayList<>();
    private List<ITask> TaskCache;

    private Ownership owner;

    private static ITaskDatabase TaskDatabase = BetterTodoAPI.getAPI(TASK_DB);

    private void refreshCache() {
        TaskCache = TodoList.TaskDatabase.getAll(Tasks)
            .toList();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        if (nbtTagCompound.hasKey("owner", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound nbtOwner = nbtTagCompound.getCompoundTag("owner");
            byte ownerType = nbtTagCompound.getByte("type");
            UUID owner = NBTUuidUtil.readIdFromNbt(nbtTagCompound.getCompoundTag("id"));

            /*
             * switch (ownerType) {
             * Ownership.PLAYER ->
             * default -> throw new IllegalStateException("Unexpected value: " + ownerType);
             * }
             */
        }

        Tasks = NBTUuidUtil.readIds(nbtTagCompound, "tasks");

        // then the property container
        todoInfo.readFromNBT(nbtTagCompound.getCompoundTag("properties"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        // first write fields

        if (owner != null) nbtTagCompound.setTag("owner", NBTUuidUtil.writeIdToNbt(owner));

        nbtTagCompound.setTag("tasks", NBTUuidUtil.writeIds(Tasks));

        // then the property container
        nbtTagCompound.setTag("properties", todoInfo.writeToNBT(new NBTTagCompound()));

        return nbtTagCompound;
    }

    @Override
    public Optional<UUID> clearTodolist() {
        return Optional.empty();
    }

    @Override
    public Optional<UUID> removeTask(UUID uuidTask) {
        if (this.Tasks.remove(uuidTask)) {
            refreshCache();
            return Optional.of(uuidTask);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UUID> removeTask(ITask task) {
        return removeTask(task.getID());
    }

    @Override
    public void attachTask(UUID uuid) {
        Tasks.add(uuid);

        refreshCache();
    }

    @Override
    public <T> T getProperty(IPropertyType<T> prop) {
        return todoInfo.getProperty(prop);
    }

    @Override
    public <T> T getProperty(IPropertyType<T> prop, T def) {
        return todoInfo.getProperty(prop, def);
    }

    @Override
    public boolean hasProperty(IPropertyType<?> prop) {
        return todoInfo.hasProperty(prop);
    }

    @Override
    public <T> void setProperty(IPropertyType<T> prop, T value) {
        todoInfo.setProperty(prop, value);
    }

    @Override
    public void removeProperty(IPropertyType<?> prop) {
        todoInfo.removeProperty(prop);
    }

    @Override
    public void removeAllProps() {
        todoInfo.removeAllProps();
    }
}
