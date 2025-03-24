package bettertodo.todo;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import bettertodo.api.properties.TaskProps;
import bettertodo.api.todo.task.ITask;
import bettertodo.api.todo.task.ITaskDatabase;
import bettertodo.core.Todo;
import chestlib.api.properties.IPropertyType;
import chestlib.storage.properties.PropertyContainer;
import chestlib.util.nbt.NBTUuidUtil;

public class TaskInstance implements ITask {

    private final PropertyContainer taskInfo = new PropertyContainer();

    private static final ITaskDatabase DB = TaskDatabase.INSTANCE;

    @Nullable
    private UUID uuidParent;

    public TaskInstance() {
        this.setupProps();
    }

    public void refreshCache() {
        // memCache = Collections.unmodifiableList(new ArrayList<>(subTask.keySet()));
    }

    private void setupProps() {
        setupValue(TaskProps.NAME, "New Task");
        setupValue(TaskProps.DESC, "No Description");

        setupValue(TaskProps.STATE);

        setupValue(TaskProps.ASSIGNEE, Optional.of(UUID.randomUUID()));

    }

    private <T> void setupValue(IPropertyType<T> prop) {
        this.setupValue(prop, prop.getDefault());
    }

    private <T> void setupValue(IPropertyType<T> prop, T def) {
        taskInfo.setProperty(prop, taskInfo.getProperty(prop, def));
    }

    @Override
    public UUID getID() {
        return DB.inverse()
            .get(this);
    }

    @Override
    public void addSubTasks(@NotNull Iterable<UUID> uuids) {
        for (UUID uuid : uuids) {
            if (!false) {
                // placeholder
            } else {
                Todo.LOG.debug("duplicate id detected: {}", uuid);
            }
        }
        /*
         * if (!subTasks.contains(uuid)) {
         * subTasks.add(uuid);
         * } else {
         * TodoAPI.getLogger().debug("duplicate id detected: {}", uuid);
         * }
         */
    }

    @Override
    public String toChatMessage() {
        return "Name: " + getProperty(TaskProps.NAME)
            + "\n"
            + "Description: "
            + getProperty(TaskProps.DESC)
            + "\n"
            + "State: "
            + getProperty(TaskProps.STATE).toString()
            + "\n";
    }

    @Override
    public Optional<UUID> getParentID() {
        return Optional.ofNullable(uuidParent);
    }

    @Override
    public Optional<ITask> getParent() {
        if (getParentID().isPresent() && DB.containsKey(uuidParent)) {
            return Optional.of(DB.get(uuidParent));
        }
        return Optional.empty();
    }

    @Override
    public Collection<UUID> getChildrensIDs() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ITask> getChildrens() {
        return Collections.unmodifiableCollection(
            getChildrensIDs().stream()
                .map((DB::get))
                .collect(Collectors.toList()));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (uuidParent != null) {
            NBTUuidUtil.writeIdToNbt("p", uuidParent, nbt);
        }

        nbt.setTag("properties", taskInfo.writeToNBT(new NBTTagCompound()));

        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        uuidParent = NBTUuidUtil.tryReadFromNbt("p", nbt)
            .orElse(null);

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
