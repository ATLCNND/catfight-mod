package com.catfight.entity;

import com.catfight.sound.ModSounds;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CatFightTracker {
    private static final Map<UUID, FightData> catFightMap = new HashMap<>();
    private static final Map<UUID, Integer> forbidFightMap = new HashMap<>();
    private static final Random RANDOM = new Random();

    private static final double TRIGGER_RANGE = 4.0D;
    private static final double LEAVE_RANGE = 6.0D;
    private static final int SOUND_INTERVAL = 35;
    private static final int MAIN_ATTACK_INTERVAL = 60;
    private static final double MAIN_ATTACK_CHANCE = 0.22;
    private static final int ATTACK_COOLDOWN = 40;

    private static class FightData {
        CatEntity target;
        int attackCooldown;
        FightData(CatEntity target) {
            this.target = target;
            this.attackCooldown = 0;
        }
    }

    public static void tick(World world) {
        if (world.isClient()) return;

        List<CatEntity> allCats = new java.util.ArrayList<>();
        for (net.minecraft.entity.player.PlayerEntity player : world.getPlayers()) {
            Box scanBox = new Box(player.getBlockPos()).expand(40);
            allCats.addAll(world.getEntitiesByClass(CatEntity.class, scanBox, cat -> true));
        }

        // 禁斗倒计时
        for (CatEntity cat : allCats) {
            UUID catUuid = cat.getUuid();
            Integer remain = forbidFightMap.get(catUuid);
            if (remain != null) {
                if (remain <= 1) {
                    forbidFightMap.remove(catUuid);
                    cat.setSilent(false);
                } else {
                    forbidFightMap.put(catUuid, remain - 1);
                }
            }
        }

        for (CatEntity cat : allCats) {
            UUID catUuid = cat.getUuid();

            // 禁斗中的猫跳过
            if (forbidFightMap.containsKey(catUuid)) {
                catFightMap.remove(catUuid);
                continue;
            }

            FightData fightData = catFightMap.get(catUuid);

            if (fightData != null) {
                CatEntity target = fightData.target;

                if (!target.isAlive() || cat.squaredDistanceTo(target) > LEAVE_RANGE * LEAVE_RANGE) {
                    catFightMap.remove(catUuid);
                    continue;
                }

                cat.lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.FEET, target.getPos());
                target.lookAt(net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.FEET, cat.getPos());

                if (cat.age % SOUND_INTERVAL == 0) {
                    cat.playSound(ModSounds.randomArgue(), 1.0F, 0.9F + RANDOM.nextFloat() * 0.2F);
                }

                if (fightData.attackCooldown > 0) fightData.attackCooldown--;
                if (cat.age % MAIN_ATTACK_INTERVAL == 0 && fightData.attackCooldown <= 0 && RANDOM.nextDouble() < MAIN_ATTACK_CHANCE) {
                    cat.tryAttack(target);
                    fightData.attackCooldown = ATTACK_COOLDOWN;
                }
            } else {
                // 禁斗中的猫不重新配对
                if (forbidFightMap.containsKey(catUuid)) continue;

                Box searchBox = new Box(cat.getBlockPos()).expand(TRIGGER_RANGE);
                List<CatEntity> nearbyCats = world.getEntitiesByClass(CatEntity.class, searchBox, 
                    other -> other != cat && !forbidFightMap.containsKey(other.getUuid()));
                if (!nearbyCats.isEmpty()) {
                    CatEntity enemy = nearbyCats.get(0);
                    catFightMap.put(catUuid, new FightData(enemy));
                }
            }
        }
    }

    public static void setForbidFight(CatEntity cat, int tick) {
        forbidFightMap.put(cat.getUuid(), tick);
        cat.setSilent(true);
        removeFromFight(cat);
        cat.getWorld().addParticle(net.minecraft.particle.ParticleTypes.HAPPY_VILLAGER,
            cat.getX(), cat.getY() + 0.8, cat.getZ(), 0, 0, 0);
        cat.getWorld().sendEntityStatus(cat, (byte) 7);
    }

    public static void removeFromFight(CatEntity cat) {
        catFightMap.remove(cat.getUuid());
        cat.setHeadYaw(cat.getYaw());
    }

    public static void clearData() {
        catFightMap.clear();
        forbidFightMap.clear();
    }
}
