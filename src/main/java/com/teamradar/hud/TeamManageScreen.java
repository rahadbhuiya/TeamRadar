package com.teamradar.hud;

import com.teamradar.data.TeamData;
import com.teamradar.data.TeammateTracker;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.List;

public class TeamManageScreen extends Screen {

    private TextFieldWidget inputField;
    private String statusMessage = "";
    private int statusColor = 0xFFFFFF;

    public TeamManageScreen() {
        super(Text.literal("TeamRadar - Manage Team"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;

        inputField = new TextFieldWidget(this.textRenderer,
                cx - 100, this.height / 2 - 60, 200, 20,
                Text.literal("Player name"));
        inputField.setMaxLength(64);
        inputField.setFocused(true);
        this.addSelectableChild(inputField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Add Member"), btn -> {
            String name = inputField.getText().trim();
            if (!name.isEmpty()) {
                if (TeamData.isMember(name)) {
                    statusMessage = name + " is already in your team.";
                    statusColor = 0xFFAA00;
                } else {
                    TeamData.addMember(name);
                    TeammateTracker.reload();
                    statusMessage = "Added: " + name;
                    statusColor = 0x55FF55;
                    inputField.setText("");
                }
            }
        }).dimensions(cx - 105, this.height / 2 - 30, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Remove Member"), btn -> {
            String name = inputField.getText().trim();
            if (!name.isEmpty()) {
                if (TeamData.isMember(name)) {
                    TeamData.removeMember(name);
                    TeammateTracker.reload();
                    statusMessage = "Removed: " + name;
                    statusColor = 0xFF6666;
                    inputField.setText("");
                } else {
                    statusMessage = name + " is not in your team.";
                    statusColor = 0xFF4444;
                }
            }
        }).dimensions(cx + 5, this.height / 2 - 30, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), btn -> this.close())
                .dimensions(cx - 50, this.height - 35, 100, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer,
                "TeamRadar - Manage Team", this.width / 2, 15, 0xFFD700);

        context.drawTextWithShadow(this.textRenderer,
                "Enter player name:", this.width / 2 - 100, this.height / 2 - 80, 0xCCCCCC);

        inputField.render(context, mouseX, mouseY, delta);

        if (!statusMessage.isEmpty()) {
            context.drawCenteredTextWithShadow(this.textRenderer,
                    statusMessage, this.width / 2, this.height / 2, statusColor);
        }

        List<String> members = TeamData.getMembers();
        context.drawTextWithShadow(this.textRenderer,
                "Current Team (" + members.size() + "):", this.width / 2 - 100, this.height / 2 + 20, 0xAAAAAA);

        for (int i = 0; i < members.size(); i++) {
            context.drawTextWithShadow(this.textRenderer,
                    "  - " + members.get(i), this.width / 2 - 100, this.height / 2 + 34 + i * 12, 0xFFFFFF);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers)
                || inputField.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
