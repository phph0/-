package com.elimentas.core;

import com.elimentas.skill.fire.FireballProjectile;
import com.elimentas.skill.ice.IceShardProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ElimenTas.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<FireballProjectile>> FIREBALL_PROJECTILE =
            ENTITIES.register("fireball_projectile", () ->
                    EntityType.Builder.<FireballProjectile>of(FireballProjectile::new, MobCategory.MISC)
                            .sized(0.4f, 0.4f)
                            .clientTrackingRange(8)
                            .updateInterval(2)
                            .build(ElimenTas.MOD_ID + ":fireball_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<IceShardProjectile>> ICE_SHARD_PROJECTILE =
            ENTITIES.register("ice_shard_projectile", () ->
                    EntityType.Builder.<IceShardProjectile>of(IceShardProjectile::new, MobCategory.MISC)
                            .sized(0.3f, 0.3f)
                            .clientTrackingRange(8)
                            .updateInterval(2)
                            .build(ElimenTas.MOD_ID + ":ice_shard_projectile"));

    public static void init(IEventBus modBus) {
        ENTITIES.register(modBus);
    }
}
