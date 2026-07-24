package com.catfight.item;

import com.catfight.entity.CatFightTracker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TrainingStickItem extends Item {
    public static final int FORBID_FIGHT_DURATION = 3600;

    public TrainingStickItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof CatEntity cat && attacker instanceof PlayerEntity) {
            if (cat.isTamed()) {
                CatFightTracker.setForbidFight(cat, FORBID_FIGHT_DURATION);
            }
            return true;
        }
        return super.postHit(stack, target, attacker);
    }
}
