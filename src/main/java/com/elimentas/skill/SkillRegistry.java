package com.elimentas.skill;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class SkillRegistry {

    private static final Map<ResourceLocation, Skill> SKILLS = new HashMap<>();

    public static void register(Skill skill) {
        SKILLS.put(skill.id(), skill);
    }

    public static Skill get(ResourceLocation id) {
        return SKILLS.get(id);
    }

    public static Collection<Skill> all() {
        return Collections.unmodifiableCollection(SKILLS.values());
    }
}
