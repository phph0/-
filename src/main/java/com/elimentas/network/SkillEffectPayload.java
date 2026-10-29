package com.elimentas.network;

import com.elimentas.core.ElimenTas;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SkillEffectPayload(String skillId, double x, double y, double z) implements CustomPacketPayload {

    public static final Type<SkillEffectPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ElimenTas.MOD_ID, "skill_effect"));

    public static final StreamCodec<ByteBuf, SkillEffectPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, SkillEffectPayload::skillId,
                    ByteBufCodecs.DOUBLE, SkillEffectPayload::x,
                    ByteBufCodecs.DOUBLE, SkillEffectPayload::y,
                    ByteBufCodecs.DOUBLE, SkillEffectPayload::z,
                    SkillEffectPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
