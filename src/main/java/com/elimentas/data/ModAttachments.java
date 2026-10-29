package com.elimentas.data;

import com.elimentas.core.ElimenTas;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ElimenTas.MOD_ID);

    public static final Supplier<AttachmentType<PlayerSkillData>> SKILL_DATA =
            ATTACHMENTS.register("skill_data", () ->
                    AttachmentType.builder(PlayerSkillData::new)
                            .serialize(new PlayerSkillDataSerializer())
                            .copyOnDeath()
                            .build()
            );
}
