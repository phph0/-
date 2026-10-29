package com.elimentas.skill;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public record SkillContext(
        ServerPlayer player,
        ServerLevel level,
        Vec3 lookDirection
) {
}
