package net.xiaoyu.custom_sapling;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.*;

public class CustomSaplingBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
    
    public CustomSaplingBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, Integer.valueOf(0)));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, Random random, BlockPos pos, BlockState state) {
        return true;
    }
    
    @Override
    public void performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state) {
        this.growTree(level, pos, state, random);
    }
    
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.setValue(STAGE, Integer.valueOf(1)), 4);
        } else {
            this.growTree(level, pos, state, random);
        }

        super.randomTick(state, level, pos, random);
    }

    public void growTree(ServerLevel level, BlockPos pos, BlockState state, Random random) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        BlockPos belowBelowPos = belowPos.below();
        BlockState belowBelowState = level.getBlockState(belowBelowPos);
        
        BlockState trunkState;
        if (!belowBelowState.isAir()) {
            trunkState = belowBelowState;
        } else {
            trunkState = belowState;
        }

        BlockState leavesState = belowState;

        TreeConfiguration config = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(trunkState), 
            new StraightTrunkPlacer(4, 2, 0),
            BlockStateProvider.simple(leavesState),
            new BlobFoliagePlacer(
                ConstantInt.of(2), 
                ConstantInt.of(0), 
                3
            ),
            new TwoLayersFeatureSize(1, 0, 1)
        ).dirt(BlockStateProvider.simple(belowState)).ignoreVines().build();

        level.setBlock(pos, level.getFluidState(pos).createLegacyBlock(), 4);

        boolean success = Feature.TREE.place(
            new FeaturePlaceContext<>(
                Optional.empty(),
                level,
                level.getChunkSource().getGenerator(),
                random,
                pos,
                config
            )
        );

        if (!success) {
            level.setBlock(pos, state, 4);
        }
    }
}