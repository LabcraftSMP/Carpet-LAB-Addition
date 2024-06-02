package carpetlabaddition.mixins;

import carpetlabaddition.LABSettings;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
    @Unique
    private static int getLABMaxCount(ItemStack itemStack) {
        if (LABSettings.hoppersDontStackShulkers
            && itemStack.getItem() instanceof BlockItem blockItem
            && blockItem.getBlock() instanceof ShulkerBoxBlock
        ) {
            return 0;
        }
        return itemStack.getMaxCount();
    }

    @Redirect(method = "isFull", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private int isFullMaxCountCheck(ItemStack itemStack) {
        return getLABMaxCount(itemStack);
    }

    @Redirect(method = "isInventoryFull", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private static int isInventoryFullMaxCountCheck(ItemStack itemStack) {
        return getLABMaxCount(itemStack);
    }

    @Inject(method = "canMergeItems", at = @At("HEAD"), cancellable = true)
    private static void canMergeItemsLAB(ItemStack first, ItemStack second, CallbackInfoReturnable<Boolean> cir) {
        if (LABSettings.hoppersDontStackShulkers && ((
            first.getItem() instanceof BlockItem firstBlock
            && firstBlock.getBlock() instanceof ShulkerBoxBlock
        ) || (
            first.getItem() instanceof BlockItem secondBlock
            && secondBlock.getBlock() instanceof ShulkerBoxBlock
        ))) {
            cir.setReturnValue(false);
        }
    }
}
