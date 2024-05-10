package carpetlabaddition.mixins;

import carpetlabaddition.CarpetLABAdditionSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = HopperBlockEntity.class, priority = 949) // Lithium is 950
public abstract class HopperBlockEntityMixin {
    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "isInventoryFull", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private static int isInventoryFullMaxCountCheck(ItemStack itemStack) {
        if (CarpetLABAdditionSettings.hoppersDontStackShulkers
                && itemStack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof ShulkerBoxBlock
        ) {
            return 0;
        }
        return itemStack.getMaxCount();
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(cancellable = true, method = "insert(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/HopperBlockEntity;)Z",
            at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/block/entity/HopperBlockEntity;isInventoryFull(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/util/math/Direction;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void insertLAB(World world, BlockPos pos, HopperBlockEntity blockEntity, CallbackInfoReturnable<Boolean> cir, Inventory inventory, Direction direction) {
        if (!CarpetLABAdditionSettings.hoppersDontStackShulkers) return;

        if (HopperBlockEntity.isInventoryFull(inventory, direction)) {
            cir.setReturnValue(false);
            return;
        }
        for (int i = 0; i < blockEntity.size(); ++i) {
            ItemStack itemStack = blockEntity.getStack(i);
            if (itemStack.isEmpty()) continue;
            int j = itemStack.getCount();
            ItemStack itemStack2 = HopperBlockEntity.transfer(blockEntity, inventory, blockEntity.removeStack(i, 1), direction);
            if (itemStack2.isEmpty()) {
                inventory.markDirty();
                cir.setReturnValue(true);
                return;
            }
            itemStack.setCount(j);
            if (j != 1) continue;
            blockEntity.setStack(i, itemStack);
        }
        cir.setReturnValue(false);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;canMergeItems(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"))
    private static boolean canMergeLAB(ItemStack first, ItemStack second) {
        if (CarpetLABAdditionSettings.hoppersDontStackShulkers && ((
                first.getItem() instanceof BlockItem firstBlock
                        && firstBlock.getBlock() instanceof ShulkerBoxBlock
        ) || (
                first.getItem() instanceof BlockItem secondBlock
                        && secondBlock.getBlock() instanceof ShulkerBoxBlock
        ))) {
            return false;
        }
        return first.getCount() <= first.getMaxCount() && ItemStack.areItemsAndComponentsEqual(first, second);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "extract(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/Hopper;)Z",
            at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Direction;DOWN:Lnet/minecraft/util/math/Direction;", shift = At.Shift.AFTER), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void extractLAB(World world, Hopper hopper, CallbackInfoReturnable<Boolean> cir, BlockPos blockPos, BlockState blockState, Inventory inventory) {
        if (!CarpetLABAdditionSettings.hoppersDontStackShulkers) return;

        Direction direction = Direction.DOWN;
        for (int i : HopperBlockEntity.getAvailableSlots(inventory, direction)) {
            if (!HopperBlockEntity.extract(hopper, inventory, i, direction)) continue;
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(false);
    }
}