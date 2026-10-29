package com.elimentas.tree;

import com.elimentas.skill.Element;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SkillNode {

    private final ResourceLocation id;
    private final String name;
    private final Element element;
    private final int tier;
    private final float x;
    private final float y;
    private final List<ResourceLocation> connections;
    private final ResourceLocation skillId;

    public SkillNode(ResourceLocation id, String name, Element element, int tier,
                     float x, float y, ResourceLocation skillId) {
        this.id = id;
        this.name = name;
        this.element = element;
        this.tier = tier;
        this.x = x;
        this.y = y;
        this.connections = new ArrayList<>();
        this.skillId = skillId;
    }

    public SkillNode addConnection(ResourceLocation targetId) {
        connections.add(targetId);
        return this;
    }

    public ResourceLocation id() { return id; }
    public String name() { return name; }
    public Element element() { return element; }
    public int tier() { return tier; }
    public float x() { return x; }
    public float y() { return y; }
    public List<ResourceLocation> connections() { return connections; }
    public ResourceLocation skillId() { return skillId; }
}
