package carpetlabaddition.mixins;

import carpetlabaddition.LABSettings;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockAttachedEntity.class)
public abstract class BlockAttachedEntityMixin {
    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void ignoreShulkerBoxMove(MoverType moverType, Vec3 delta, CallbackInfo ci) {
        if (!LABSettings.shulkerBoxesDoNotBreakPaintings) return;
        if (moverType != MoverType.SHULKER_BOX) return;
        if (((Object) this) instanceof HangingEntity) ci.cancel();
    }
}
