package com.catfight.item;

import com.catfight.entity.CatFightTracker;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class SuperDriedFishItem extends Item {
    public SuperDriedFishItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, net.minecraft.entity.LivingEntity entity, Hand hand) {
        if (entity instanceof CatEntity cat && cat.isTamed()) {
            if (!user.getWorld().isClient()) {
                stack.decrement(1);
                CatFightTracker.setForbidFight(cat, Integer.MAX_VALUE);
                cat.setSitting(true);
                cat.setInSittingPose(true);
                cat.getWorld().sendEntityStatus(cat, (byte) 7);
                cat.dropStack(new ItemStack(net.minecraft.item.Items.GOLDEN_APPLE, 1));
            }
            return ActionResult.success(user.getWorld().isClient());
        }
        return ActionResult.PASS;
    }
}
