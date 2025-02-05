package com.demel.todo.api.todo;

import betterquesting.api.properties.IPropertyContainer;
import betterquesting.api2.storage.INBTProgress;
import betterquesting.api2.storage.INBTSaveLoad;
import com.demel.todo.api.enums.TaskState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public interface ITask extends INBTSaveLoad<NBTTagCompound>, INBTProgress<NBTTagCompound>, IPropertyContainer {

    TaskState getState(EntityPlayer player);

    void update(EntityPlayer player);

    boolean is(UUID uuid, TaskState state);

    default boolean isCompleted(UUID uuid) {
        return is(uuid, TaskState.Completed);
    }

    default boolean isNew(UUID uuid) {
        return is(uuid, TaskState.New);
    }

    default boolean isInProgress(UUID uuid) {
        return is(uuid, TaskState.InProgress);
    }

    default boolean isOnHold(UUID uuid) {
        return is(uuid, TaskState.OnHold);
    }

    default boolean isAbandoned(UUID uuid) {
        return is(uuid, TaskState.Abandoned);
    }
}
