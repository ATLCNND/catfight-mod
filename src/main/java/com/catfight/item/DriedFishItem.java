package com.catfight.item;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class DriedFishItem extends Item {
    public DriedFishItem(Settings settings) {
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
                // 随机奖励：金粒、经验、或者金锭
                java.util.Random rand = new java.util.Random();
                int roll = rand.nextInt(100);
                if (roll < 30) {
                    cat.dropStack(new ItemStack(net.minecraft.item.Items.GOLD_NUGGET, 1 + rand.nextInt(3)));
                } else if (roll < 50) {
                    cat.dropStack(new ItemStack(net.minecraft.item.Items.COD, 1));
                } else if (roll < 65) {
                    cat.dropStack(new ItemStack(net.minecraft.item.Items.STRING, 1 + rand.nextInt(2)));
                } else if (roll < 78) {
                    cat.dropStack(new ItemStack(net.minecraft.item.Items.FEATHER, 1 + rand.nextInt(2)));
                } else if (roll < 88) {
                    cat.dropStack(new ItemStack(net.minecraft.item.Items.GOLD_INGOT, 1));
                } else if (roll < 95) {
                    cat.dropStack(new ItemStack(net.minecraft.item.Items.IRON_INGOT, 1));
                } else if (roll < 98) {
                    cat.dropStack(new ItemStack(net.minecraft.item.Items.EMERALD, 1));
                } else {
                    cat.dropStack(new ItemStack(net.minecraft.item.Items.DIAMOND, 1));
                }
                // 爱心粒子
                cat.getWorld().sendEntityStatus(cat, (byte) 7);
            }
            return ActionResult.success(user.getWorld().isClient());
        }
        return ActionResult.PASS;
    }
}
