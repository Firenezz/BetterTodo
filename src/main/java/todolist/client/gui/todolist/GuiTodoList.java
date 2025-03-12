package todolist.client.gui.todolist;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import net.minecraft.client.gui.GuiScreen;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import betterquesting.api.client.gui.misc.INeedsRefresh;
import betterquesting.api2.client.gui.GuiScreenCanvas;
import betterquesting.api2.client.gui.controls.IPanelButton;
import betterquesting.api2.client.gui.controls.PanelButton;
import betterquesting.api2.client.gui.controls.PanelButtonStorage;
import betterquesting.api2.client.gui.controls.PanelTextField;
import betterquesting.api2.client.gui.controls.filters.FieldFilterString;
import betterquesting.api2.client.gui.events.IPEventListener;
import betterquesting.api2.client.gui.events.PEventBroadcaster;
import betterquesting.api2.client.gui.events.PanelEvent;
import betterquesting.api2.client.gui.events.types.PEventButton;
import betterquesting.api2.client.gui.misc.GuiAlign;
import betterquesting.api2.client.gui.misc.GuiPadding;
import betterquesting.api2.client.gui.misc.GuiRectangle;
import betterquesting.api2.client.gui.misc.GuiTransform;
import betterquesting.api2.client.gui.panels.CanvasEmpty;
import betterquesting.api2.client.gui.panels.CanvasTextured;
import betterquesting.api2.client.gui.panels.bars.PanelVScrollBar;
import betterquesting.api2.client.gui.panels.content.PanelTextBox;
import betterquesting.api2.client.gui.themes.presets.PresetColor;
import betterquesting.api2.client.gui.themes.presets.PresetIcon;
import betterquesting.api2.client.gui.themes.presets.PresetTexture;
import betterquesting.api2.utils.QuestTranslation;
import todolist.api.enums.TaskState;
import todolist.api.properties.TaskProps;
import todolist.api.todo.task.ITask;
import todolist.client.gui.todolist.panels.CanvasTaskDatabase;
import todolist.network.handlers.NetTaskCreate;
import todolist.todo.TaskInstance;

public class GuiTodoList extends GuiScreenCanvas implements IPEventListener, INeedsRefresh {

    private CanvasTaskDatabase canvasDB;

    private static final int ID_BTN_EDIT = 1;

    public GuiTodoList(GuiScreen parent) {
        super(parent);
    }

    @Override
    public void initPanel() {
        super.initPanel();

        PEventBroadcaster.INSTANCE.register((Consumer<PanelEvent>) this::onPanelEvent, PEventButton.class);
        Keyboard.enableRepeatEvents(true);

        // General panel
        CanvasTextured cvBackground = new CanvasTextured(
            new GuiTransform(GuiAlign.FULL_BOX, new GuiPadding(0, 0, 0, 0), 0),
            PresetTexture.PANEL_MAIN.getTexture());
        this.addPanel(cvBackground);

        PanelTextBox panTxt = new PanelTextBox(
            new GuiTransform(GuiAlign.TOP_EDGE, new GuiPadding(0, 16, 0, -32), 0),
            "TodoList").setAlignment(1);
        panTxt.setColor(PresetColor.TEXT_HEADER.getColor());
        cvBackground.addPanel(panTxt);

        PanelButton btnBack = new PanelButton(
            new GuiTransform(GuiAlign.BOTTOM_CENTER, -100, -16, 200, 16, 0),
            0,
            QuestTranslation.translate("gui.back")) {

            @Override
            public void onButtonClick() {
                mc.displayGuiScreen(parent);
            }
        };
        cvBackground.addPanel(btnBack);

        // Right panel

        CanvasEmpty cvRight = new CanvasEmpty(new GuiTransform(GuiAlign.FULL_BOX, new GuiPadding(8, 32, 16, 24), 0));
        cvBackground.addPanel(cvRight);

        PanelTextBox txtDb = new PanelTextBox(
            new GuiTransform(GuiAlign.TOP_EDGE, new GuiPadding(0, 0, 0, -16), 0),
            "Tasks").setAlignment(1)
                .setColor(PresetColor.TEXT_MAIN.getColor());
        cvRight.addPanel(txtDb);

        PanelTextField<String> searchBox = new PanelTextField<>(
            new GuiTransform(GuiAlign.TOP_EDGE, new GuiPadding(0, 16, 8, -32), 0),
            "",
            FieldFilterString.INSTANCE);
        searchBox.setWatermark("Search...");
        cvRight.addPanel(searchBox);

        canvasDB = new CanvasTaskDatabase(new GuiTransform(GuiAlign.FULL_BOX, new GuiPadding(0, 32, 8, 24), 0)) {

            @Override
            protected boolean addResult(Map.Entry<UUID, ITask> entry, int index, int cachedWidth) {
                PanelButtonStorage<Map.Entry<UUID, ITask>> btnAdd = new PanelButtonStorage<>(
                    new GuiRectangle(0, index * 16, 16, 16, 0),
                    2,
                    "",
                    entry);
                btnAdd.setIcon(PresetIcon.ICON_POSITIVE.getTexture());
                // btnAdd.setActive(!containsReq(quest, entry.getKey()));
                this.addPanel(btnAdd);

                PanelButtonStorage<Map.Entry<UUID, ITask>> btnEdit = new PanelButtonStorage<>(
                    new GuiRectangle(16, index * 16, width - 32, 16, 0),
                    ID_BTN_EDIT,
                    "test", // QuestTranslation.translateQuestName(entry),
                    entry);
                this.addPanel(btnEdit);

                PanelButtonStorage<Map.Entry<UUID, ITask>> btnDel = new PanelButtonStorage<>(
                    new GuiRectangle(width - 16, index * 16, 16, 16, 0),
                    4,
                    "",
                    entry);
                btnDel.setIcon(PresetIcon.ICON_TRASH.getTexture());
                this.addPanel(btnDel);

                return true;
            }
        };
        cvRight.addPanel(canvasDB);

        PanelVScrollBar scDb = new PanelVScrollBar(
            new GuiTransform(GuiAlign.RIGHT_EDGE, new GuiPadding(-8, 32, 0, 24), 0));
        cvRight.addPanel(scDb);
        canvasDB.setScrollDriverY(scDb);

        PanelButton btnNew = new PanelButton(
            new GuiTransform(GuiAlign.BOTTOM_EDGE, new GuiPadding(0, -16, 0, 0), 0),
            5,
            QuestTranslation.translate("betterquesting.btn.new"));
        cvRight.addPanel(btnNew);
    }

    @Override
    public void refreshGui() {
        canvasDB.refreshSearch();
    }

    @Override
    public void onPanelEvent(PanelEvent event) {
        if (event instanceof PEventButton) {
            onButtonPress((PEventButton) event);
        }
    }

    @SuppressWarnings("unchecked")
    private void onButtonPress(@NotNull PEventButton event) {
        IPanelButton btn = event.getButton();

        switch (btn.getButtonID()) {
            case ID_BTN_EDIT:
                Map.Entry<UUID, ITask> entry = ((PanelButtonStorage<Map.Entry<UUID, ITask>>) btn).getStoredValue();
                this.openPopup(new GuiTaskDetail(entry.getValue()));
                break;
            case 5:
                var task = new TaskInstance();
                task.setProperty(TaskProps.STATE, TaskState.Abandoned);
                task.setProperty(TaskProps.NAME, "Test from book.");

                NetTaskCreate.sendCreate(Collections.singletonList(task));
                break;
        }
    }
}
