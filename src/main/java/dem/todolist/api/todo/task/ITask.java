package dem.todolist.api.todo.task;

import net.minecraft.nbt.NBTTagCompound;

import com.dem.chestlib.api.properties.IPropertyContainer;
import com.dem.chestlib.api.storage.INBTSaveLoad;

import dem.todolist.api.enums.TaskState;

public interface ITask extends INBTSaveLoad<NBTTagCompound>, IPropertyContainer {

    TaskState getState();

    void update();

    boolean is(TaskState state);

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
}
