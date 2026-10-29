package com.elimentas.network;

import com.elimentas.core.ElimenTas;
import com.elimentas.effect.EffectPlayer;
import com.elimentas.skill.*;
import com.elimentas.skill.fire.FireballSkill;
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

    private static final ResourceLocation[] SLOT_SKILLS = {
            FireballSkill.ID,
            null, null, null
    };

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
    }

    private static void handleSkillActivate(SkillActivatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            if (!(player.level() instanceof ServerLevel serverLevel)) return;

            int slot = payload.slotIndex();
            if (slot < 0 || slot >= SLOT_SKILLS.length) return;

            ResourceLocation skillId = SLOT_SKILLS[slot];
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

            ElimenTas.LOGGER.debug("Player {} activated skill {}", player.getName().getString(), skillId);
        });
    }

    private static void handleSkillEffect(SkillEffectPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            EffectPlayer.play(payload.skillId(), payload.x(), payload.y(), payload.z());
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
