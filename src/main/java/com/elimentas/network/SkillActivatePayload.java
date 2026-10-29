package com.elimentas.network;

import com.elimentas.core.ElimenTas;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SkillActivatePayload(int slotIndex) implements CustomPacketPayload {

    public static final Type<SkillActivatePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ElimenTas.MOD_ID, "skill_activate"));

    public static final StreamCodec<ByteBuf, SkillActivatePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, SkillActivatePayload::slotIndex,
                    SkillActivatePayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
