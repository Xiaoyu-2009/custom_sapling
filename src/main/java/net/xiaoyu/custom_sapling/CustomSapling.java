package net.xiaoyu.custom_sapling;

import net.minecraft.client.renderer.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

@Mod(CustomSapling.MOD_ID)
public class CustomSapling {
    public static final String MOD_ID = "custom_sapling";
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    
    public static final RegistryObject<Block> CUSTOM_SAPLING = BLOCKS.register("custom_sapling", 
        () -> new CustomSaplingBlock(
            BlockBehaviour.Properties.of(Material.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
        )
    );
    
    public static final RegistryObject<Item> CUSTOM_SAPLING_ITEM = ITEMS.register("custom_sapling", 
        () -> new BlockItem(CUSTOM_SAPLING.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    
    public CustomSapling() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        modEventBus.addListener(this::clientSetup);
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(CUSTOM_SAPLING.get(), RenderType.cutout());
        });
    }
}