package com.teamradar.mixin;

import com.teamradar.data.TeamData;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onPlayerList", at = @At("TAIL"))
    private void onPlayerList(PlayerListS2CPacket packet, CallbackInfo ci) {
        // Detect teammates joining/leaving via player list packets
        for (PlayerListS2CPacket.Entry entry : packet.getEntries()) {
            if (entry.profile() == null) continue;
            String name = entry.profile().getName();
            if (TeamData.isMember(name)) {
                // Teammate is in the server player list — they are online
                // TeammateTracker.tick() handles the actual update each frame
            }
        }
    }
}
