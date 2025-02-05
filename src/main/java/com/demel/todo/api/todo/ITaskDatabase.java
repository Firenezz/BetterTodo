package com.demel.todo.api.todo;

import betterquesting.api2.storage.INBTPartial;
import betterquesting.api2.storage.INBTProgress;
import betterquesting.api2.storage.IUuidDatabase;
import net.minecraft.nbt.NBTTagList;

import java.util.UUID;

public interface ITaskDatabase extends IUuidDatabase<ITask>, INBTPartial<NBTTagList, UUID>, INBTProgress<NBTTagList> {

    ITask createNew(UUID uuid);
}
