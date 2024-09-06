package com.rinko1231.revengemark.event;

import com.rinko1231.revengemark.RevengeMark;
import com.rinko1231.revengemark.config.RevengeMarkConfig;
import net.minecraftforge.event.TickEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;


@Mod.EventBusSubscriber(modid = "revengemark")
public class PlayerDeathLoader {

    private static final Map<Mob, Integer> glowingMobs = new HashMap<>(); // 保存发光中的实体和剩余时间


    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {

            // 检查死亡原因是否由实体造成
            Entity source = event.getSource().getEntity();
            if (source instanceof Mob mob) {// 检查实体是否不在黑名单中

                if (!isBlacklistMob(mob)) {
                    Component originalName = mob.getCustomName();//killer
                    boolean hasOriginalName = false;
                    if (originalName != null) {
                        hasOriginalName = true;
                    }

                    if (hasOriginalName == false) {
                        // 如果原来没有名字，将实体命名为
                        String conquerorName = RevengeMarkConfig.getKillerCustomNameL() + player.getDisplayName().getString() + RevengeMarkConfig.getKillerCustomNameR();
                        mob.setCustomName(Component.literal(conquerorName));
                        mob.setCustomNameVisible(true); // 显示名字
                        //施加复仇标记
                        mob.addEffect(new MobEffectInstance(RevengeMark.REVENGE_MARK.get(), RevengeMarkConfig.getConfigRenameDuration()));
                        if (RevengeMarkConfig.getIsGlowing() == true) {// 施加发光效果
                            mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, RevengeMarkConfig.getConfigRenameDuration()));
                        }
                        glowingMobs.put(mob, RevengeMarkConfig.getConfigRenameDuration());
                        MinecraftForge.EVENT_BUS.register(new TickHandler());//这会去除加上的名字
                    }
                    else
                    if (RevengeMarkConfig.getIsGlowing() == true) {// 施加发光效果
                        mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, RevengeMarkConfig.getConfigRenameDuration()));
                    }
                }
            }
        }
    }
    public static boolean isBlacklistMob(LivingEntity entity) {
        return entity instanceof Monster
                && RevengeMarkConfig.getInstance().getMobBlacklist().contains(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString());
    }

    public static boolean hasRevengeMarkEffect(LivingEntity entity) {
        return entity.hasEffect(RevengeMark.REVENGE_MARK.get());
    }

    // 用于处理计时和移除效果的Tick事件
    public static class TickHandler {
        @SubscribeEvent
        public void onWorldTick(TickEvent.ServerTickEvent event) {
            glowingMobs.entrySet().removeIf(entry -> {
                Mob mob = entry.getKey();

                // 检查实体是否已经死亡或没有 Revenge Mark 效果
                if (mob.isDeadOrDying() || !hasRevengeMarkEffect(mob)) {
                    // 恢复名字并移除发光效果
                    if (RevengeMarkConfig.getIsGlowing()) {
                        mob.removeEffect(MobEffects.GLOWING); // 移除发光效果
                    }

                    if (mob.hasCustomName()) {
                        mob.setCustomName(null); // 恢复名字
                        mob.setCustomNameVisible(false);
                    }

                    return true; // 从Map中移除该实体
                }

                return false; // 保留该实体
            });

            // 如果没有更多发光的实体，取消 Tick 事件的注册
            if (glowingMobs.isEmpty()) {
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }


}