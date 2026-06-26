package com.teamradar.hud;

import com.teamradar.data.TeammateInfo;
import com.teamradar.data.TeammateTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;

public class TeamHud {

    private static boolean visible = true;

    public static void toggleVisible() {
        visible = !visible;
    }

    public static void render(DrawContext context) {
        if (!visible) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;
        if (!TeammateTracker.hasTeammates()) return;

        Collection<TeammateInfo> all = TeammateTracker.getAll();
        Vec3d playerPos = client.player.getPos();
        float playerYaw = client.player.getYaw();

        TextRenderer tr = client.textRenderer;
        int panelWidth = 185;
        int x = context.getScaledWindowWidth() - panelWidth - 4;
        int y = 4;
        int lineH = 12;

        int panelHeight = 14 + all.size() * (lineH * 3 + 6) + 4;
        context.fill(x - 3, y - 2, x + panelWidth, y + panelHeight, 0x88000000);

        context.drawTextWithShadow(tr, "[ TeamRadar ]", x, y, 0xFFD700);
        y += lineH + 2;

        for (TeammateInfo info : all) {
            String onlineStatus = info.online ? "ONLINE" : "OFFLINE";
            int nameColor = info.online ? 0xFFFFFF : 0x666666;
            int statusColor = info.online ? 0x55FF55 : 0xFF4444;

            context.drawTextWithShadow(tr, info.name, x, y, nameColor);
            context.drawTextWithShadow(tr, onlineStatus, x + panelWidth - tr.getWidth(onlineStatus), y, statusColor);
            y += lineH;

            if (info.online) {
                float hpPct = info.getHealthPercent() / 100f;
                int barWidth = panelWidth - 2;
                int barFill = (int)(barWidth * hpPct);

                context.fill(x, y, x + barWidth, y + 4, 0xFF333333);
                context.fill(x, y, x + barFill, y + 4, info.getHealthColor());

                String hpText = String.format("%.1f / %.0f HP", info.health, info.maxHealth);
                context.drawTextWithShadow(tr, hpText, x, y + 6, info.getHealthColor());
                y += lineH + 2;

                double dist = info.distanceTo(playerPos.x, playerPos.y, playerPos.z);
                String dir = getDirectionArrow(playerYaw, info.x - playerPos.x, info.z - playerPos.z);
                String dim = info.getDimensionShort();

                String line2 = String.format("%s  %.0f blk  [%s]", dir, dist, dim);
                int line2Color = (info.health <= 6f) ? 0xFF4444 : 0xAAAAAA;
                context.drawTextWithShadow(tr, line2, x, y, line2Color);
                y += lineH;

                if (info.recentlyDied) {
                    context.drawTextWithShadow(tr, "!! DIED !!", x, y, 0xFF0000);
                    y += lineH;
                }

            } else {
                context.drawTextWithShadow(tr, "Not in render distance", x, y, 0x555555);
                y += lineH * 2;
            }

            context.fill(x, y, x + panelWidth, y + 1, 0x44FFFFFF);
            y += 6;
        }
    }

    private static String getDirectionArrow(float yaw, double dx, double dz) {
        double angle = Math.toDegrees(Math.atan2(dz, dx)) - yaw + 90;
        angle = ((angle % 360) + 360) % 360;

        if (angle < 22.5 || angle >= 337.5) return "^";
        if (angle < 67.5) return "^>";
        if (angle < 112.5) return ">";
        if (angle < 157.5) return "v>";
        if (angle < 202.5) return "v";
        if (angle < 247.5) return "<v";
        if (angle < 292.5) return "<";
        return "<^";
    }
}
