package dev.sterner.malum.common.block.totem;

import com.sammy.lodestone.systems.block.LodestoneEntityBlock;
import dev.sterner.malum.common.blockentity.totem.TotemPoleBlockEntity;
import dev.sterner.malum.common.registry.MalumBlockEntityRegistry;
import dev.sterner.malum.common.registry.MalumSpiritTypeRegistry;
import dev.sterner.malum.common.spirit.MalumSpiritType;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

@SuppressWarnings("deprecation")
public class TotemPoleBlock<T extends TotemPoleBlockEntity> extends LodestoneEntityBlock<T> {
    public final Block logBlock;
    public final boolean corrupted;
    public TotemPoleBlock(Block logBlock, AbstractBlock.Settings settings, boolean corrupted) {
        super(settings);
        this.logBlock = logBlock;
        this.corrupted = corrupted;
        this.setDefaultState(this.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return logBlock.getPickStack(world, pos, state);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TotemPoleBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, MalumSpiritTypeRegistry.SPIRIT_TYPE_PROPERTY);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof TotemPoleBlockEntity pole) {
            return Math.min(MalumSpiritTypeRegistry.getIndexForSpiritType(pole.type) + 1, 15);
        }
        return 0;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? checkType(type, MalumBlockEntityRegistry.TOTEM_POLE, (w, p, s, b) -> b.clientTick()) : null;
    }

    public void onStrip(BlockState blockState, World world, BlockPos blockPos) {
    }
}