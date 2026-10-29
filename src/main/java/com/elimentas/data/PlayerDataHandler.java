package com.elimentas.data;

import com.elimentas.core.ElimenTas;
import com.elimentas.network.SkillTreeSyncPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = ElimenTas.MOD_ID)
public class PlayerDataHandler {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerSkillData data = player.getData(ModAttachments.SKILL_DATA);
            if (data.unlockedNodes().isEmpty() && data.skillPoints() == 0) {
                data.addSkillPoints(3);
            }
            syncToClient(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            syncToClient(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            syncToClient(player);
        }
    }

    public static void syncToClient(ServerPlayer player) {
        PlayerSkillData data = player.getData(ModAttachments.SKILL_DATA);
        java.util.List<String> nodeIds = data.unlockedNodes().stream()
                .map(net.minecraft.resources.ResourceLocation::toString)
                .toList();
        java.util.List<String> slotIds = new java.util.ArrayList<>();
        for (net.minecraft.resources.ResourceLocation slot : data.equippedSlots()) {
            slotIds.add(slot != null ? slot.toString() : "");
        }
        PacketDistributor.sendToPlayer(player,
                new SkillTreeSyncPayload(data.skillPoints(), nodeIds, slotIds));
    }
}
