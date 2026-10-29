package com.elimentas.skill;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class CooldownManager {

    private static final Map<UUID, Map<ResourceLocation, Integer>> COOLDOWNS = new HashMap<>();

    public static boolean isOnCooldown(ServerPlayer player, Skill skill) {
        Map<ResourceLocation, Integer> map = COOLDOWNS.get(player.getUUID());
        if (map == null) return false;
        return map.containsKey(skill.id());
    }

    public static void startCooldown(ServerPlayer player, Skill skill) {
        COOLDOWNS.computeIfAbsent(player.getUUID(), k -> new HashMap<>())
                .put(skill.id(), skill.cooldownTicks());
    }

    public static int getRemainingTicks(ServerPlayer player, ResourceLocation skillId) {
        Map<ResourceLocation, Integer> map = COOLDOWNS.get(player.getUUID());
        if (map == null) return 0;
        return map.getOrDefault(skillId, 0);
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        Iterator<Map.Entry<UUID, Map<ResourceLocation, Integer>>> it = COOLDOWNS.entrySet().iterator();
        while (it.hasNext()) {
            Map<ResourceLocation, Integer> map = it.next().getValue();
            map.entrySet().removeIf(e -> {
                e.setValue(e.getValue() - 1);
                return e.getValue() <= 0;
            });
            if (map.isEmpty()) it.remove();
        }
    }

    public static void clearPlayer(UUID uuid) {
        COOLDOWNS.remove(uuid);
    }
}
