package todolist.client.gui.todolist.panels;

import java.util.*;

import betterquesting.api.utils.UuidConverter;
import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.panels.lists.CanvasSearch;
import todolist.api.properties.TaskProps;
import todolist.api.todo.task.ITask;
import todolist.todo.TaskDatabase;

public abstract class CanvasTaskDatabase extends CanvasSearch<Map.Entry<UUID, ITask>, Map.Entry<UUID, ITask>> {

    public CanvasTaskDatabase(IGuiRect rect) {
        super(rect);
    }

    @Override
    protected Iterator<Map.Entry<UUID, ITask>> getIterator() {
        return TaskDatabase.INSTANCE.entrySet()
            .iterator();
    }

    @Override
    protected void queryMatches(Map.Entry<UUID, ITask> entry, String query,
        final ArrayDeque<Map.Entry<UUID, ITask>> results) {
        if (UuidConverter.encodeUuid(entry.getKey())
            .toLowerCase()
            .contains(query)
            || entry.getValue()
                .getProperty(TaskProps.NAME)
                .toLowerCase()
                .contains(query)) {
            results.add(entry);
        }
    }
}
