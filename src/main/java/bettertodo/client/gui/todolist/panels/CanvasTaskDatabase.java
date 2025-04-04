package bettertodo.client.gui.todolist.panels;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import betterquesting.api.utils.UuidConverter;
import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.panels.lists.CanvasSearch;
import bettertodo.api.properties.TaskProps;
import bettertodo.api.todo.task.ITask;
import bettertodo.todo.TaskDatabase;

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
