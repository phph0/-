package com.elimentas.data;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class ClientSkillData {

    public static final int SLOT_COUNT = 4;

    private static final Set<ResourceLocation> unlockedNodes = new HashSet<>();
    private static final ResourceLocation[] equippedSlots = new ResourceLocation[SLOT_COUNT];
    private static final Map<ResourceLocation, int[]> cooldowns = new HashMap<>();
    private static int skillPoints = 0;

    public static void sync(int points, List<String> nodeIds, List<String> slotIds) {
        skillPoints = points;
        unlockedNodes.clear();
        for (String id : nodeIds) {
            unlockedNodes.add(ResourceLocation.parse(id));
        }
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (i < slotIds.size() && !slotIds.get(i).isEmpty()) {
                equippedSlots[i] = ResourceLocation.parse(slotIds.get(i));
            } else {
                equippedSlots[i] = null;
            }
        }
    }

    public static void syncCooldown(String skillId, int remaining, int total) {
        cooldowns.put(ResourceLocation.parse(skillId), new int[]{remaining, total});
    }

    public static void tickCooldowns() {
        Iterator<Map.Entry<ResourceLocation, int[]>> it = cooldowns.entrySet().iterator();
        while (it.hasNext()) {
            int[] cd = it.next().getValue();
            cd[0]--;
            if (cd[0] <= 0) it.remove();
        }
    }

    public static float getCooldownProgress(ResourceLocation skillId) {
        int[] cd = cooldowns.get(skillId);
        if (cd == null || cd[1] <= 0) return 0f;
        return (float) cd[0] / cd[1];
    }

    public static boolean isOnCooldown(ResourceLocation skillId) {
        int[] cd = cooldowns.get(skillId);
        return cd != null && cd[0] > 0;
    }

    public static boolean isUnlocked(ResourceLocation nodeId) {
        return unlockedNodes.contains(nodeId);
    }

    public static Set<ResourceLocation> unlockedNodes() {
        return Collections.unmodifiableSet(unlockedNodes);
    }

    public static int skillPoints() {
        return skillPoints;
    }

    public static ResourceLocation getSlot(int slot) {
        if (slot < 0 || slot >= SLOT_COUNT) return null;
        return equippedSlots[slot];
    }

    public static ResourceLocation[] equippedSlots() {
        return equippedSlots.clone();
    }

    public static void clear() {
        unlockedNodes.clear();
        skillPoints = 0;
        Arrays.fill(equippedSlots, null);
        cooldowns.clear();
    }
}
