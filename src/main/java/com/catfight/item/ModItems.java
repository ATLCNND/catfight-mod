package com.catfight.item;

import com.catfight.CatFightMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item TRAINING_STICK = new TrainingStickItem(new Item.Settings().maxCount(1));
    public static final Item DRIED_FISH = new DriedFishItem(new Item.Settings().maxCount(16));
    public static final Item SUPER_DRIED_FISH = new SuperDriedFishItem(new Item.Settings().maxCount(8));

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(CatFightMod.MOD_ID, "training_stick"), TRAINING_STICK);
        Registry.register(Registries.ITEM, new Identifier(CatFightMod.MOD_ID, "dried_fish"), DRIED_FISH);
        Registry.register(Registries.ITEM, new Identifier(CatFightMod.MOD_ID, "super_dried_fish"), SUPER_DRIED_FISH);
        
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(TRAINING_STICK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(DRIED_FISH);
            entries.add(SUPER_DRIED_FISH);
        });
    }
}
