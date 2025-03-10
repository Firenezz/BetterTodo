package dem.todolist.handlers.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;

import betterquesting.core.BetterQuesting;
import dem.todolist.api.api.TodoAPI;
import dem.todolist.core.TodoSettings;
import dem.todolist.todo.TaskDatabase;
import dem.todolist.utils.NBTUtils;

public class DatabasePersistence {

    public static DatabasePersistence INSTANCE = new DatabasePersistence();

    private File fileDatabase = null;

    private File rootDir;

    private void setFiles(MinecraftServer server) {
        if (BetterQuesting.proxy.isClient()) {
            TodoSettings.curWorldDir = server.getFile("saves/" + server.getFolderName() + "/todolist");
            rootDir = server.getFile("saves/" + server.getFolderName());
        } else {
            TodoSettings.curWorldDir = server.getFile(server.getFolderName() + "/todolist");
            rootDir = server.getFile(server.getFolderName());
        }

        fileDatabase = new File(TodoSettings.curWorldDir, "db.nbt");
    }

    public void loadDatabases(MinecraftServer server) {
        setFiles(server);

        loadTasks();

        TodoAPI.getLogger()
            .info("Loaded " + TaskDatabase.INSTANCE.size() + " tasks");
    }

    public void loadTasks() {
        NBTTagCompound nbt = NBTUtils.readNBTFile(fileDatabase);

        TaskDatabase.INSTANCE.readFromNBT(nbt.getTagList("tasks", 10), false);
    }

    public List<Future<Void>> saveDatabases() {
        List<Future<Void>> allFutures = new ArrayList<>(5);
        allFutures.add(saveTasks());

        return allFutures;
    }

    public Future<Void> saveTasks() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setTag("tasks", TaskDatabase.INSTANCE.writeToNBT(new NBTTagList(), null));

        return NBTUtils.writeNBTToFileSafe(fileDatabase, nbt);
    }

    public void unloadDatabases() {
        TaskDatabase.INSTANCE.clear();
    }
}
