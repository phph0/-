package com.elimentas.data;

import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientSkillData {

    private static final Set<ResourceLocation> unlockedNodes = new HashSet<>();
    private static int skillPoints = 0;

    public static void sync(int points, List<String> nodeIds) {
        skillPoints = points;
        unlockedNodes.clear();
        for (String id : nodeIds) {
            unlockedNodes.add(ResourceLocation.parse(id));
        }
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

    public static void clear() {
        unlockedNodes.clear();
        skillPoints = 0;
    }
}
