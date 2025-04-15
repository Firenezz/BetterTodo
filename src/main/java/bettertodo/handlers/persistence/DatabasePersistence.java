package bettertodo.handlers.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;

import betterquesting.core.BetterQuesting;
import bettertodo.api.api.BetterTodoAPI;
import bettertodo.core.TodoSettings;
import bettertodo.todo.TaskDatabase;
import bettertodo.todo.TodoListDatabase;
import bettertodo.utils.NBTUtils;
import chestlib.api.versionning.Version;

public class DatabasePersistence {

    public static DatabasePersistence INSTANCE = new DatabasePersistence();

    private File fileDatabase = null;
    private File fileTodolistDatabase = null;

    private Version CUR_VERSION = new Version(0, 1);

    private File rootDir;

    private void setFiles(MinecraftServer server) {
        if (BetterQuesting.proxy.isClient()) {
            TodoSettings.curWorldDir = server.getFile("saves/" + server.getFolderName() + "/bettertodo");
            rootDir = server.getFile("saves/" + server.getFolderName());
        } else {
            TodoSettings.curWorldDir = server.getFile(server.getFolderName() + "/bettertodo");
            rootDir = server.getFile(server.getFolderName());
        }

        fileDatabase = new File(TodoSettings.curWorldDir, "db_tasks.nbt");
        fileTodolistDatabase = new File(TodoSettings.curWorldDir, "db_todolist.nbt");
    }

    public void loadDatabases(MinecraftServer server) {
        setFiles(server);

        loadTodoList();
        loadTasks();

        BetterTodoAPI.getLogger()
            .info("Loaded {} tasks", TaskDatabase.INSTANCE.size());
    }

    public void loadTasks() {
        NBTTagCompound nbt = NBTUtils.readNBTFile(fileDatabase)
            .orElse(new NBTTagCompound());

        TaskDatabase.INSTANCE.readFromNBT(nbt.getTagList("tasks", 10), false);
    }

    public void loadTodoList() {
        NBTTagCompound nbt = NBTUtils.readNBTFile(fileTodolistDatabase)
            .orElse(new NBTTagCompound());

        TaskDatabase.INSTANCE.readFromNBT(nbt.getTagList("tasks", 10), false);
    }

    public List<Future<Void>> saveDatabases() {
        List<Future<Void>> allFutures = new ArrayList<>(5);
        allFutures.add(saveTasks());
        allFutures.add(saveTodoList());

        return allFutures;
    }

    public Future<Void> saveTasks() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setTag("tasks", TaskDatabase.INSTANCE.writeToNBT(new NBTTagList(), null));
        nbt.setTag("version", CUR_VERSION.writeToNBT(new NBTTagCompound()));

        return NBTUtils.writeNBTToFileSafe(fileDatabase, nbt);
    }

    public Future<Void> saveTodoList() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setTag("todolist", TodoListDatabase.INSTANCE.writeToNBT(new NBTTagList(), null));
        nbt.setTag("version", CUR_VERSION.writeToNBT(new NBTTagCompound()));

        return NBTUtils.writeNBTToFileSafe(fileTodolistDatabase, nbt);
    }

    public void unloadDatabases() {
        TaskDatabase.INSTANCE.clear();
    }
}
