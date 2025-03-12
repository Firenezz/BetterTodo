package dem.todolist.client.gui.todolist;

import betterquesting.api.client.gui.misc.INeedsRefresh;
import betterquesting.api2.client.gui.GuiScreenCanvas;
import betterquesting.api2.client.gui.events.IPEventListener;
import betterquesting.api2.client.gui.events.PEventBroadcaster;
import betterquesting.api2.client.gui.events.PanelEvent;
import betterquesting.api2.client.gui.events.types.PEventButton;
import betterquesting.api2.client.gui.misc.GuiAlign;
import betterquesting.api2.client.gui.misc.GuiRectangle;
import betterquesting.api2.client.gui.misc.GuiTransform;
import betterquesting.api2.client.gui.panels.CanvasEmpty;
import betterquesting.api2.client.gui.panels.CanvasResizeable;
import betterquesting.api2.client.gui.themes.presets.PresetTexture;
import dem.todolist.api.todo.task.ITask;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.util.vector.Vector4f;

import java.util.UUID;

public class GuiTaskDetail extends CanvasEmpty implements INeedsRefresh {
    private final ITask task;

    // FEATURE: Add scrollposition saving
    public GuiTaskDetail(ITask task) {
        super(new GuiTransform(GuiAlign.FULL_BOX));
        this.task = task; // TO CHECK: Should I use the id instead
    }

    @Override
    public void initPanel() {
        super.initPanel();

        if (task == null) {
            return;
        }

        CanvasResizeable cvBox = new CanvasResizeable(
            new GuiTransform(new Vector4f(0.5F, 0.45F, 0.5F, 0.45F)),
            PresetTexture.PANEL_MAIN.getTexture());
        this.addPanel(cvBox);
        cvBox.lerpToRect(new GuiTransform(new Vector4f(0.2F, 0.3F, 0.8F, 0.6F)), 200L, true);


    }

    @Override
    public void refreshGui() {

    }

    @Override
    public boolean onMouseClick(int mx, int my, int click) {
        return super.onMouseClick(mx, my, click);
    }
}
