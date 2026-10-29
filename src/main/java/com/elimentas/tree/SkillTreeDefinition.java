package com.elimentas.tree;

import com.elimentas.core.ElimenTas;
import com.elimentas.skill.Element;
import com.elimentas.skill.fire.FireballSkill;
import com.elimentas.skill.ice.IceShardSkill;
import com.elimentas.skill.lightning.LightningBoltSkill;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class SkillTreeDefinition {

    private static final Map<ResourceLocation, SkillNode> NODES = new LinkedHashMap<>();

    public static void init() {
        buildFireConstellation();
        buildIceConstellation();
        buildLightningConstellation();
        ElimenTas.LOGGER.debug("SkillTreeDefinition: {} nodes defined.", NODES.size());
    }

    private static void buildFireConstellation() {
        float cx = -200, cy = 0;

        SkillNode core = node("fire_core", "Fire Core", Element.FIRE, 0, cx, cy, null);
        SkillNode fireball = node("fire_fireball", "Fireball", Element.FIRE, 1, cx - 60, cy - 80, FireballSkill.ID);
        SkillNode blaze = node("fire_blaze", "Blaze Aura", Element.FIRE, 1, cx + 60, cy - 80, null);
        SkillNode meteor = node("fire_meteor", "Meteor Strike", Element.FIRE, 2, cx, cy - 160, null);
        SkillNode inferno = node("fire_inferno", "Inferno", Element.FIRE, 2, cx - 100, cy + 60, null);
        SkillNode ember = node("fire_ember", "Ember Shield", Element.FIRE, 1, cx + 100, cy + 60, null);
        SkillNode phoenix = node("fire_phoenix", "Phoenix Rise", Element.FIRE, 3, cx, cy - 240, null);

        connect(core, fireball);
        connect(core, blaze);
        connect(core, inferno);
        connect(core, ember);
        connect(fireball, meteor);
        connect(blaze, meteor);
        connect(meteor, phoenix);
    }

    private static void buildIceConstellation() {
        float cx = 200, cy = 0;

        SkillNode core = node("ice_core", "Ice Core", Element.ICE, 0, cx, cy, null);
        SkillNode shard = node("ice_shard", "Ice Shard", Element.ICE, 1, cx - 70, cy - 70, IceShardSkill.ID);
        SkillNode frost = node("ice_frost", "Frost Nova", Element.ICE, 1, cx + 70, cy - 70, null);
        SkillNode blizzard = node("ice_blizzard", "Blizzard", Element.ICE, 2, cx, cy - 150, null);
        SkillNode wall = node("ice_wall", "Ice Wall", Element.ICE, 1, cx - 90, cy + 50, null);
        SkillNode freeze = node("ice_freeze", "Deep Freeze", Element.ICE, 2, cx + 90, cy + 50, null);
        SkillNode absolute = node("ice_absolute", "Absolute Zero", Element.ICE, 3, cx, cy - 230, null);

        connect(core, shard);
        connect(core, frost);
        connect(core, wall);
        connect(core, freeze);
        connect(shard, blizzard);
        connect(frost, blizzard);
        connect(blizzard, absolute);
    }

    private static void buildLightningConstellation() {
        float cx = 0, cy = 250;

        SkillNode core = node("lightning_core", "Lightning Core", Element.LIGHTNING, 0, cx, cy, null);
        SkillNode bolt = node("lightning_bolt", "Lightning Bolt", Element.LIGHTNING, 1, cx - 80, cy - 60, LightningBoltSkill.ID);
        SkillNode chain = node("lightning_chain", "Chain Lightning", Element.LIGHTNING, 1, cx + 80, cy - 60, null);
        SkillNode storm = node("lightning_storm", "Thunderstorm", Element.LIGHTNING, 2, cx, cy - 140, null);
        SkillNode dash = node("lightning_dash", "Thunder Dash", Element.LIGHTNING, 1, cx - 60, cy + 80, null);
        SkillNode surge = node("lightning_surge", "Surge", Element.LIGHTNING, 2, cx + 60, cy + 80, null);
        SkillNode wrath = node("lightning_wrath", "Storm's Wrath", Element.LIGHTNING, 3, cx, cy - 220, null);

        connect(core, bolt);
        connect(core, chain);
        connect(core, dash);
        connect(core, surge);
        connect(bolt, storm);
        connect(chain, storm);
        connect(storm, wrath);
    }

    private static SkillNode node(String path, String name, Element element, int tier,
                                   float x, float y, ResourceLocation skillId) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ElimenTas.MOD_ID, path);
        SkillNode node = new SkillNode(id, name, element, tier, x, y, skillId);
        NODES.put(id, node);
        return node;
    }

    private static void connect(SkillNode a, SkillNode b) {
        a.addConnection(b.id());
        b.addConnection(a.id());
    }

    public static SkillNode getNode(ResourceLocation id) {
        return NODES.get(id);
    }

    public static Collection<SkillNode> allNodes() {
        return Collections.unmodifiableCollection(NODES.values());
    }

    public static List<SkillNode> nodesByElement(Element element) {
        return NODES.values().stream()
                .filter(n -> n.element() == element)
                .toList();
    }
}
