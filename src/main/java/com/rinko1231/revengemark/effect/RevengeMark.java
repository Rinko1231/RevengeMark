package com.rinko1231.revengemark.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RevengeMark extends MobEffect {
    public RevengeMark() {
        // MobEffectCategory.NEUTRAL 表示效果既不利也不有利。设置颜色可以是任何颜色。
        super(MobEffectCategory.NEUTRAL, 0xFFFFFF); // 这里设置了白色作为效果颜色
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // 留空或不实现任何逻辑，以避免效果产生影响
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        // 返回 false，表示效果不会有任何实际作用
        return false;
    }

}