package bettertodo.commands;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.ICommandSender;

import com.gtnewhorizon.gtnhlib.commands.GTNHClientCommand;

import bettertodo.handlers.persistence.DatabasePersistence;

public class ClearDatabasesCommand extends GTNHClientCommand {

    @Override
    public String getCommandName() {
        return "todocleardb";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        DatabasePersistence.INSTANCE.unloadDatabases();
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("tcd");
    }
}
