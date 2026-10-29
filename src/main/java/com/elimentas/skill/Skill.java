package com.elimentas.skill;

import net.minecraft.resources.ResourceLocation;

public interface Skill {

    ResourceLocation id();

    Element element();

    int cooldownTicks();

    void execute(SkillContext context);
}
