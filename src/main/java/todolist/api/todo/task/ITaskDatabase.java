package todolist.api.todo.task;

import java.util.UUID;

import net.minecraft.nbt.NBTTagList;

import chestlib.api.database.IUuidDatabase;
import chestlib.api.storage.INBTPartial;

public interface ITaskDatabase extends IUuidDatabase<ITask>, INBTPartial<NBTTagList, UUID> {

    ITask createNew(UUID uuid);

}
