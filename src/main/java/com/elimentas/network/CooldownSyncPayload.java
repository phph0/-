package com.elimentas.network;

import com.elimentas.core.ElimenTas;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CooldownSyncPayload(String skillId, int remaining, int total) implements CustomPacketPayload {

    public static final Type<CooldownSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ElimenTas.MOD_ID, "cooldown_sync"));

    public static final StreamCodec<ByteBuf, CooldownSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, CooldownSyncPayload::skillId,
                    ByteBufCodecs.INT, CooldownSyncPayload::remaining,
                    ByteBufCodecs.INT, CooldownSyncPayload::total,
                    CooldownSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
