package com.elimentas.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

public class PlayerSkillDataSerializer implements IAttachmentSerializer<CompoundTag, PlayerSkillData> {

    @Override
    public PlayerSkillData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
        PlayerSkillData data = new PlayerSkillData();
        data.load(tag);
        return data;
    }

    @Override
    public @Nullable CompoundTag write(PlayerSkillData data, HolderLookup.Provider provider) {
        return data.save();
    }
}
