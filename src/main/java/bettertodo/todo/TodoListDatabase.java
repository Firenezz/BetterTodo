package bettertodo.todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.jetbrains.annotations.Nullable;

import chestlib.api.database.UuidDatabase;
import chestlib.util.nbt.NBTUuidUtil;
import bettertodo.api.api.BetterTodoAPI;
import bettertodo.api.todo.ITodoList;
import bettertodo.api.todo.todolist.ITodoListDatabase;

public class TodoListDatabase extends UuidDatabase<ITodoList> implements ITodoListDatabase {

    public static final TodoListDatabase INSTANCE = new TodoListDatabase();

    @Override
    public ITodoList createNew(UUID uuid) {
        ITodoList todoList = new TodoList();
        put(uuid, todoList);
        return todoList;
    }

    public ITodoList createNewWithUuidGen() {
        return createNew(generateKey());
    }

    @Override
    public NBTTagList writeToNBT(NBTTagList nbt, @Nullable List<UUID> subset) {
        orderedEntries().filter((entry) -> subset != null && subset.contains(entry.getKey()))
            .forEach(entry -> {
                if (entry.getValue() == null) {
                    BetterTodoAPI.getLogger()
                        .warn("Tried saving null bettertodo with ID {}", entry.getKey());
                    return;
                }

                NBTTagCompound tag = new NBTTagCompound();
                entry.getValue()
                    .writeToNBT(tag);
                NBTUuidUtil.writeIdToNbt(entry.getKey(), tag);
                nbt.appendTag(tag);
            });

        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagList nbt, boolean merge) {
        if (!merge) {
            clear();
        }

        for (int i = 0; i < nbt.tagCount(); i++) {
            NBTTagCompound qTag = nbt.getCompoundTagAt(i);

            Optional<UUID> todoListIDOptional = NBTUuidUtil.tryReadFromNbt("", qTag);
            UUID todoListID;
            if (todoListIDOptional.isPresent()) {
                todoListID = todoListIDOptional.get();
            } else {
                // Register in a lost todoList database for admins
                continue;
            }

            ITodoList todoList = get(todoListID);
            todoList = todoList != null ? todoList : createNew(todoListID);
            todoList.readFromNBT(qTag);
        }
    }
}
