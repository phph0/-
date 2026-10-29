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

    public static final int SLOT_COUNT = 4;

    private final Set<ResourceLocation> unlockedNodes = new HashSet<>();
    private final ResourceLocation[] equippedSlots = new ResourceLocation[SLOT_COUNT];
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

    public ResourceLocation getSlot(int slot) {
        if (slot < 0 || slot >= SLOT_COUNT) return null;
        return equippedSlots[slot];
    }

    public boolean equipSkill(int slot, ResourceLocation nodeId) {
        if (slot < 0 || slot >= SLOT_COUNT) return false;
        if (nodeId != null) {
            SkillNode node = SkillTreeDefinition.getNode(nodeId);
            if (node == null || node.skillId() == null) return false;
            if (!isUnlocked(nodeId)) return false;
            for (int i = 0; i < SLOT_COUNT; i++) {
                if (i != slot && nodeId.equals(equippedSlots[i])) return false;
            }
        }
        equippedSlots[slot] = nodeId;
        return true;
    }

    public ResourceLocation getSkillIdForSlot(int slot) {
        ResourceLocation nodeId = getSlot(slot);
        if (nodeId == null) return null;
        SkillNode node = SkillTreeDefinition.getNode(nodeId);
        return node != null ? node.skillId() : null;
    }

    public ResourceLocation[] equippedSlots() {
        return equippedSlots.clone();
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("SkillPoints", skillPoints);
        ListTag list = new ListTag();
        for (ResourceLocation id : unlockedNodes) {
            list.add(StringTag.valueOf(id.toString()));
        }
        tag.put("UnlockedNodes", list);
        ListTag slots = new ListTag();
        for (int i = 0; i < SLOT_COUNT; i++) {
            slots.add(StringTag.valueOf(equippedSlots[i] != null ? equippedSlots[i].toString() : ""));
        }
        tag.put("EquippedSlots", slots);
        return tag;
    }

    public void load(CompoundTag tag) {
        skillPoints = tag.getInt("SkillPoints");
        unlockedNodes.clear();
        ListTag list = tag.getList("UnlockedNodes", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            unlockedNodes.add(ResourceLocation.parse(list.getString(i)));
        }
        ListTag slots = tag.getList("EquippedSlots", Tag.TAG_STRING);
        for (int i = 0; i < SLOT_COUNT && i < slots.size(); i++) {
            String val = slots.getString(i);
            equippedSlots[i] = val.isEmpty() ? null : ResourceLocation.parse(val);
        }
    }

    public void copyFrom(PlayerSkillData other) {
        unlockedNodes.clear();
        unlockedNodes.addAll(other.unlockedNodes);
        skillPoints = other.skillPoints;
        System.arraycopy(other.equippedSlots, 0, equippedSlots, 0, SLOT_COUNT);
    }
}
