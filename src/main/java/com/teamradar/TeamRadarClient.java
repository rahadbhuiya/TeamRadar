package com.teamradar;

import com.teamradar.data.TeamData;
import com.teamradar.data.TeammateTracker;
import com.teamradar.hud.TeamHud;
import com.teamradar.hud.TeamManageScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class TeamRadarClient implements ClientModInitializer {

    public static KeyBinding keyManageTeam;
    public static KeyBinding keyToggleHud;

    @Override
    public void onInitializeClient() {
        TeamData.load();
        TeammateTracker.reload();

        keyManageTeam = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.teamradar.manage",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_T,
                "TeamRadar"
        ));

        keyToggleHud = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.teamradar.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "TeamRadar"
        ));

        HudRenderCallback.EVENT.register((context, tickCounter) -> TeamHud.render(context));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            TeammateTracker.tick(client);

            while (keyManageTeam.wasPressed()) {
                client.setScreen(new TeamManageScreen());
            }
            while (keyToggleHud.wasPressed()) {
                TeamHud.toggleVisible();
            }
        });
    }
}
