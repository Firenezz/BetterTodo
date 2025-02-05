package com.demel.todo.todo;


import betterquesting.api2.storage.UuidDatabase;
import com.demel.todo.api.todo.ITask;
import com.demel.todo.api.todo.ITaskDatabase;
import net.minecraft.nbt.NBTTagList;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class TaskDatabase extends UuidDatabase<ITask> implements ITaskDatabase {

    public static final TaskDatabase INSTANCE = new TaskDatabase();

    @Override
    public ITask createNew(UUID uuid) {
        return null;
    }

    @Override
    public NBTTagList writeToNBT(NBTTagList nbt, @Nullable List<UUID> subset) {
        return null;
    }

    @Override
    public void readFromNBT(NBTTagList nbt, boolean merge) {

    }

    @Override
    public NBTTagList writeProgressToNBT(NBTTagList nbt, @Nullable List<UUID> users) {
        return null;
    }

    @Override
    public void readProgressFromNBT(NBTTagList nbt, boolean merge) {

    }
}
