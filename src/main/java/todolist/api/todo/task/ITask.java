package todolist.api.todo.task;

import static todolist.api.properties.TaskProps.*;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import chestlib.api.properties.IPropertyContainer;
import chestlib.api.storage.INBTSaveLoad;
import todolist.api.enums.TaskState;

public interface ITask extends INBTSaveLoad<NBTTagCompound>, IPropertyContainer {

    UUID getID();

    void addSubTasks(@NotNull Iterable<UUID> uuids);

    Optional<UUID> getParentID();

    Optional<ITask> getParent();

    Collection<UUID> getChildrensIDs();

    Collection<ITask> getChildrens();

    String toChatMessage();

    default boolean is(TaskState state) {
        return getState() == state;
    }

    default boolean isCompleted() {
        return is(TaskState.Completed);
    }

    default boolean isNew() {
        return is(TaskState.New);
    }

    default boolean isInProgress() {
        return is(TaskState.InProgress);
    }

    default boolean isOnHold() {
        return is(TaskState.OnHold);
    }

    default boolean isAbandoned() {
        return is(TaskState.Abandoned);
    }

    default String getName() {
        return getProperty(NAME);
    }

    default void setName(String name) {
        setProperty(NAME, name);
    }

    default boolean hasName() {
        return hasProperty(NAME);
    }

    default String getDescription() {
        return getProperty(DESC);
    }

    default void setDescription(String description) {
        setProperty(DESC, description);
    }

    default boolean hasDescription() {
        return hasProperty(DESC);
    }

    default TaskState getState() {
        return getProperty(STATE);
    }

    default void setState(TaskState state) {
        setProperty(STATE, state);
    }

    default boolean hasState() {
        return hasProperty(STATE);
    }
}
