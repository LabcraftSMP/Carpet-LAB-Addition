package carpetlabaddition.mixins;

import carpetlabaddition.CarpetLABAdditionSettings;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Redirect(method = "calculateComparatorOutput(Lnet/minecraft/inventory/Inventory;)I",
              at = @At(value="INVOKE", target = "Lnet/minecraft/inventory/Inventory;getMaxCount(Lnet/minecraft/item/ItemStack;)I"))
    private static int getLABMaxCount(Inventory inventory, ItemStack itemStack) {
        if (CarpetLABAdditionSettings.hoppersDontStackShulkers
                && itemStack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof ShulkerBoxBlock
        ) {
            return 1;
        }
        return inventory.getMaxCount(itemStack);
    }
}
