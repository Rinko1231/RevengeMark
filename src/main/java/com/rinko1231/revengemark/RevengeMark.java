package com.rinko1231.revengemark;

import com.mojang.logging.LogUtils;
import com.rinko1231.revengemark.config.RevengeMarkConfig;
import com.rinko1231.revengemark.event.PlayerDeathLoader;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// 使用 @Mod 注解，并指定你的模组 ID
@Mod("revengemark")
public class RevengeMark {

    // 模组的日志记录器，便于在控制台输出调试信息
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "revengemark");
    // 自定义效果注册
    public static final RegistryObject<MobEffect> REVENGE_MARK = MOB_EFFECTS.register("revenge_mark", com.rinko1231.revengemark.effect.RevengeMark::new);


    // 构造函数 - 这个是模组的启动入口
    public RevengeMark() {
        // 注册事件总线 (Event Bus)
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RevengeMarkConfig.setup();
        MOB_EFFECTS.register(modEventBus);

        // 注册玩家死亡事件监听器
        MinecraftForge.EVENT_BUS.register(PlayerDeathLoader.class);


        LOGGER.info("revengemark 模组已初始化！");
    }
}