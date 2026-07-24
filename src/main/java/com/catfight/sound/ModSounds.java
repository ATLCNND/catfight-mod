package com.catfight.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    private static final SoundEvent[] ALL_SOUNDS = new SoundEvent[9];

    public static void register() {
        for (int i = 0; i < 9; i++) {
            String name = "cat.cat_argue" + (i + 1);
            Identifier id = new Identifier("catfight", name);
            ALL_SOUNDS[i] = Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
        }
    }

    public static SoundEvent randomArgue() {
        return ALL_SOUNDS[new java.util.Random().nextInt(ALL_SOUNDS.length)];
    }
}
