package com.elimentas.input;

import com.elimentas.core.ElimenTas;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    private static final String CATEGORY = "key.categories." + ElimenTas.MOD_ID;

    public static final KeyMapping OPEN_SKILL_TREE = new KeyMapping(
            "key." + ElimenTas.MOD_ID + ".open_skill_tree",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            CATEGORY
    );

    public static final KeyMapping SKILL_SLOT_1 = new KeyMapping(
            "key." + ElimenTas.MOD_ID + ".skill_slot_1",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            CATEGORY
    );

    public static final KeyMapping SKILL_SLOT_2 = new KeyMapping(
            "key." + ElimenTas.MOD_ID + ".skill_slot_2",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            CATEGORY
    );

    public static final KeyMapping SKILL_SLOT_3 = new KeyMapping(
            "key." + ElimenTas.MOD_ID + ".skill_slot_3",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            CATEGORY
    );

    public static final KeyMapping SKILL_SLOT_4 = new KeyMapping(
            "key." + ElimenTas.MOD_ID + ".skill_slot_4",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            CATEGORY
    );

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(OPEN_SKILL_TREE);
        event.register(SKILL_SLOT_1);
        event.register(SKILL_SLOT_2);
        event.register(SKILL_SLOT_3);
        event.register(SKILL_SLOT_4);
        ElimenTas.LOGGER.debug("Key bindings registered.");
    }
}
