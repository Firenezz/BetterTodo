package bettertodo.todo;

import static bettertodo.api.api.ApiReference.TASK_DB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import bettertodo.api.api.BetterTodoAPI;
import bettertodo.api.todo.ITodoList;
import bettertodo.api.todo.task.ITask;
import bettertodo.api.todo.task.ITaskDatabase;
import chestlib.api.properties.IPropertyContainer;
import chestlib.api.properties.IPropertyType;
import chestlib.storage.properties.PropertyContainer;
import chestlib.util.nbt.NBTUuidUtil;

public class TodoList implements ITodoList {

    IPropertyContainer todoInfo = new PropertyContainer();

    public List<UUID> Task = new ArrayList<>();
    private List<ITask> TaskCache;

    private ITaskDatabase TaskDatabase = BetterTodoAPI.getAPI(TASK_DB);

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        // first write fields

        nbtTagCompound.setTag("subTask", NBTUuidUtil.writeIds(Task));

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
        return Optional.empty();
    }

    @Override
    public Optional<UUID> removeTask(ITask Task) {
        return Optional.empty();
    }

    @Override
    public void attachTask(UUID uuid) {

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
