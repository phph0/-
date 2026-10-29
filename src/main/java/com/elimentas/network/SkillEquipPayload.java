package com.elimentas.network;

import com.elimentas.core.ElimenTas;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SkillEquipPayload(int slotIndex, String nodeId) implements CustomPacketPayload {

    public static final Type<SkillEquipPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ElimenTas.MOD_ID, "skill_equip"));

    public static final StreamCodec<ByteBuf, SkillEquipPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, SkillEquipPayload::slotIndex,
                    ByteBufCodecs.STRING_UTF8, SkillEquipPayload::nodeId,
                    SkillEquipPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
