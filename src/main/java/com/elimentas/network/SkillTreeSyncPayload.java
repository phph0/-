package com.elimentas.network;

import com.elimentas.core.ElimenTas;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record SkillTreeSyncPayload(int skillPoints, List<String> unlockedNodes) implements CustomPacketPayload {

    public static final Type<SkillTreeSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ElimenTas.MOD_ID, "skill_tree_sync"));

    public static final StreamCodec<ByteBuf, SkillTreeSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, SkillTreeSyncPayload::skillPoints,
                    ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), SkillTreeSyncPayload::unlockedNodes,
                    SkillTreeSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
