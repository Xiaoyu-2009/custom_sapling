package net.xiaoyu.custom_sapling;

import net.minecraft.world.level.material.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.*;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.*;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(CustomSapling.MOD_ID)
public class CustomSapling {
    public static final String MOD_ID = "custom_sapling";
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    
    public static final DeferredBlock<Block> CUSTOM_SAPLING = BLOCKS.register("custom_sapling", 
        () -> new CustomSaplingBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY)
        )
    );
    
    public static final DeferredItem<BlockItem> CUSTOM_SAPLING_ITEM = ITEMS.registerSimpleBlockItem("custom_sapling", CUSTOM_SAPLING);
    
    public CustomSapling(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        modEventBus.register(this);
    }
    
    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(CUSTOM_SAPLING_ITEM.get().getDefaultInstance());
        }
    }
}