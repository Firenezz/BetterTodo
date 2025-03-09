package dem.todolist.todo;

import java.util.*;

import dem.todolist.api.api.TodoAPI;
import net.minecraft.nbt.NBTTagCompound;

import com.dem.chestlib.api.properties.IPropertyType;
import com.dem.chestlib.storage.properties.PropertyContainer;
import com.dem.chestlib.util.nbt.NBTUuidUtil;

import dem.todolist.api.enums.TaskState;
import dem.todolist.api.properties.TaskProps;
import dem.todolist.api.todo.task.ITask;

public class TaskInstance implements ITask {

    private final PropertyContainer taskInfo = new PropertyContainer();

    private ITask parent;
    private UUID uuidParent;

    public TaskInstance() {
        this.setupProps();
    }

    public void refreshCache() {
        //memCache = Collections.unmodifiableList(new ArrayList<>(subTask.keySet()));
    }

    private void setupProps() {
        setupValue(TaskProps.NAME, "New Task");
        setupValue(TaskProps.DESC, "No Description");

        setupValue(TaskProps.STATE);

    }

    private <T> void setupValue(IPropertyType<T> prop) {
        this.setupValue(prop, prop.getDefault());
    }

    private <T> void setupValue(IPropertyType<T> prop, T def) {
        taskInfo.setProperty(prop, taskInfo.getProperty(prop, def));
    }

    @Override
    public TaskState getState() {
        return taskInfo.getProperty(TaskProps.STATE);
    }

    public void setState(TaskState newState) {
        taskInfo.setProperty(TaskProps.STATE, newState);
    }

    @Override
    public void update() {}

    @Override
    public boolean is(TaskState state) {
        return getState() == state;
    }

    @Override
    public void addSubTask(UUID uuid) {
        /*if (!subTasks.contains(uuid)) {
            subTasks.add(uuid);
        } else {
            TodoAPI.getLogger().debug("duplicate id detected: {}", uuid);
        }*/
    }

    @Override
    public String toChatMessage() {
        return "Name: " + getProperty(TaskProps.NAME) + "\n" +
            "Description: " + getProperty(TaskProps.DESC) + "\n" +
            "State: " + getProperty(TaskProps.STATE).toString() + "\n";
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (uuidParent != null) {
            NBTUuidUtil.writeIdToNbt("parent-", uuidParent, nbt);
        }

        nbt.setTag("properties", taskInfo.writeToNBT(new NBTTagCompound()));

        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        taskInfo.readFromNBT(nbt.getCompoundTag("properties"));
    }

    @Override
    public <T> T getProperty(IPropertyType<T> prop) {
        return taskInfo.getProperty(prop);
    }

    @Override
    public <T> T getProperty(IPropertyType<T> prop, T def) {
        return taskInfo.getProperty(prop, def);
    }

    @Override
    public boolean hasProperty(IPropertyType<?> prop) {
        return taskInfo.hasProperty(prop);
    }

    @Override
    public <T> void setProperty(IPropertyType<T> prop, T value) {
        taskInfo.setProperty(prop, value);
    }

    @Override
    public void removeProperty(IPropertyType<?> prop) {
        taskInfo.removeProperty(prop);
    }

    @Override
    public void removeAllProps() {
        taskInfo.removeAllProps();
    }
}
