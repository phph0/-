package com.elimentas.data;

import com.elimentas.tree.SkillNode;
import com.elimentas.tree.SkillTreeDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PlayerSkillData {

    private final Set<ResourceLocation> unlockedNodes = new HashSet<>();
    private int skillPoints = 0;

    public Set<ResourceLocation> unlockedNodes() {
        return Collections.unmodifiableSet(unlockedNodes);
    }

    public boolean isUnlocked(ResourceLocation nodeId) {
        return unlockedNodes.contains(nodeId);
    }

    public int skillPoints() {
        return skillPoints;
    }

    public void addSkillPoints(int amount) {
        skillPoints += amount;
    }

    public boolean canUnlock(ResourceLocation nodeId) {
        if (isUnlocked(nodeId)) return false;
        if (skillPoints <= 0) return false;

        SkillNode node = SkillTreeDefinition.getNode(nodeId);
        if (node == null) return false;

        if (node.tier() == 0) return true;

        for (ResourceLocation connId : node.connections()) {
            SkillNode conn = SkillTreeDefinition.getNode(connId);
            if (conn != null && conn.tier() < node.tier() && isUnlocked(connId)) {
                return true;
            }
        }
        return false;
    }

    public boolean tryUnlock(ResourceLocation nodeId) {
        if (!canUnlock(nodeId)) return false;
        unlockedNodes.add(nodeId);
        skillPoints--;
        return true;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("SkillPoints", skillPoints);
        ListTag list = new ListTag();
        for (ResourceLocation id : unlockedNodes) {
            list.add(StringTag.valueOf(id.toString()));
        }
        tag.put("UnlockedNodes", list);
        return tag;
    }

    public void load(CompoundTag tag) {
        skillPoints = tag.getInt("SkillPoints");
        unlockedNodes.clear();
        ListTag list = tag.getList("UnlockedNodes", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            unlockedNodes.add(ResourceLocation.parse(list.getString(i)));
        }
    }

    public void copyFrom(PlayerSkillData other) {
        unlockedNodes.clear();
        unlockedNodes.addAll(other.unlockedNodes);
        skillPoints = other.skillPoints;
    }
}
