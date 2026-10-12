package carpetlabaddition.mixins;

import carpetlabaddition.LABSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HangingEntity.class)
public abstract class HangingEntityMixin {
    @WrapOperation(
            method = "hasLevelCollision", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;noBlockCollision(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Z"
    ))
    private boolean ignoreOpenShulkers(Level level, Entity entity, AABB popBox, Operation<Boolean> original) {
        if (!LABSettings.shulkerBoxesDoNotBreakPaintings)
            return original.call(level, entity, popBox);

        Iterable<Pair<BlockPos, VoxelShape>> blockCollisions = () -> new BlockCollisions<>(
                level,
                CollisionContext.of(entity),
                popBox,
                false,
                (pos, shape) -> Pair.of(pos, shape)
        );

        for (Pair<BlockPos, VoxelShape> collision : blockCollisions) {
            if (!collision.getSecond().isEmpty()) {
                BlockPos pos = collision.getFirst();
                if (popBox.intersects(pos)) return false;
                if (!(level.getBlockState(pos).getBlock() instanceof ShulkerBoxBlock)) return false;
            }
        }

        return true;
    }
}
