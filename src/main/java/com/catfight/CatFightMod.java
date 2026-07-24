package com.catfight;

import com.catfight.entity.CatFightTracker;
import com.catfight.item.ModItems;
import com.catfight.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatFightMod implements ModInitializer {
    public static final String MOD_ID = "catfight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModSounds.register();
        ModItems.register();
        LOGGER.info("Cat Fight Mod Loaded! Training stick & particle enabled.");

        ServerTickEvents.END_WORLD_TICK.register(world -> CatFightTracker.tick(world));
        ServerWorldEvents.UNLOAD.register((server, world) -> CatFightTracker.clearData());
    }
}
