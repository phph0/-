package com.elimentas.network;

import com.elimentas.core.ElimenTas;
import com.elimentas.data.ClientSkillData;
import com.elimentas.data.ModAttachments;
import com.elimentas.data.PlayerDataHandler;
import com.elimentas.data.PlayerSkillData;
import com.elimentas.effect.EffectPlayer;
import com.elimentas.skill.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ElimenTas.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkModule {

    public static void init(IEventBus modBus) {
        ElimenTas.LOGGER.debug("NetworkModule initialized.");
    }

    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ElimenTas.MOD_ID);

        registrar.playToServer(
                SkillActivatePayload.TYPE,
                SkillActivatePayload.STREAM_CODEC,
                NetworkModule::handleSkillActivate
        );

        registrar.playToClient(
                SkillEffectPayload.TYPE,
                SkillEffectPayload.STREAM_CODEC,
                NetworkModule::handleSkillEffect
        );

        registrar.playToServer(
                NodeUnlockPayload.TYPE,
                NodeUnlockPayload.STREAM_CODEC,
                NetworkModule::handleNodeUnlock
        );

        registrar.playToClient(
                SkillTreeSyncPayload.TYPE,
                SkillTreeSyncPayload.STREAM_CODEC,
                NetworkModule::handleSkillTreeSync
        );

        registrar.playToServer(
                SkillEquipPayload.TYPE,
                SkillEquipPayload.STREAM_CODEC,
                NetworkModule::handleSkillEquip
        );

        registrar.playToClient(
                CooldownSyncPayload.TYPE,
                CooldownSyncPayload.STREAM_CODEC,
                NetworkModule::handleCooldownSync
        );
    }

    private static void handleSkillActivate(SkillActivatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            if (!(player.level() instanceof ServerLevel serverLevel)) return;

            int slot = payload.slotIndex();
            PlayerSkillData data = player.getData(ModAttachments.SKILL_DATA);
            ResourceLocation skillId = data.getSkillIdForSlot(slot);
            if (skillId == null) return;

            Skill skill = SkillRegistry.get(skillId);
            if (skill == null) return;

            if (CooldownManager.isOnCooldown(player, skill)) return;

            CooldownManager.startCooldown(player, skill);

            SkillContext skillContext = new SkillContext(
                    player, serverLevel,
                    player.getLookAngle()
            );
            skill.execute(skillContext);

            PacketDistributor.sendToPlayer(player,
                    new CooldownSyncPayload(skillId.toString(),
                            skill.cooldownTicks(), skill.cooldownTicks()));

            ElimenTas.LOGGER.debug("Player {} activated skill {}", player.getName().getString(), skillId);
        });
    }

    private static void handleSkillEffect(SkillEffectPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            EffectPlayer.play(payload.skillId(), payload.x(), payload.y(), payload.z());
        });
    }

    private static void handleNodeUnlock(NodeUnlockPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;

            ResourceLocation nodeId = ResourceLocation.parse(payload.nodeId());
            PlayerSkillData data = player.getData(ModAttachments.SKILL_DATA);

            if (data.tryUnlock(nodeId)) {
                PlayerDataHandler.syncToClient(player);
                ElimenTas.LOGGER.debug("Player {} unlocked node {}", player.getName().getString(), nodeId);
            }
        });
    }

    private static void handleSkillEquip(SkillEquipPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;

            PlayerSkillData data = player.getData(ModAttachments.SKILL_DATA);
            ResourceLocation nodeId = payload.nodeId().isEmpty() ? null : ResourceLocation.parse(payload.nodeId());

            if (data.equipSkill(payload.slotIndex(), nodeId)) {
                PlayerDataHandler.syncToClient(player);
                ElimenTas.LOGGER.debug("Player {} equipped slot {} with {}",
                        player.getName().getString(), payload.slotIndex(), payload.nodeId());
            }
        });
    }

    private static void handleSkillTreeSync(SkillTreeSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientSkillData.sync(payload.skillPoints(), payload.unlockedNodes(), payload.equippedSlots());
        });
    }

    private static void handleCooldownSync(CooldownSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientSkillData.syncCooldown(payload.skillId(), payload.remaining(), payload.total());
        });
    }

    public static void sendEffectToNearby(ServerLevel level, Vec3 pos, double radius, SkillEffectPayload payload) {
        for (ServerPlayer player : level.players()) {
            if (player.distanceToSqr(pos) <= radius * radius) {
                PacketDistributor.sendToPlayer(player, payload);
            }
        }
    }
}
