package dem.todolist.api.todo.todolist;

import java.util.UUID;

import net.minecraft.nbt.NBTTagList;

import com.dem.chestlib.api.database.IUuidDatabase;
import com.dem.chestlib.api.storage.INBTPartial;

import dem.todolist.api.todo.ITodoList;

public interface ITodoListDatabase extends IUuidDatabase<ITodoList>, INBTPartial<NBTTagList, UUID> {

    ITodoList createNew(UUID uuid);

}
