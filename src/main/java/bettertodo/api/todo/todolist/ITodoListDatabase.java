package bettertodo.api.todo.todolist;

import java.util.UUID;

import net.minecraft.nbt.NBTTagList;

import bettertodo.api.todo.ITodoList;
import chestlib.api.database.IUuidDatabase;
import chestlib.api.storage.INBTPartial;

public interface ITodoListDatabase extends IUuidDatabase<ITodoList>, INBTPartial<NBTTagList, UUID> {

    ITodoList createNew(UUID uuid);

}
