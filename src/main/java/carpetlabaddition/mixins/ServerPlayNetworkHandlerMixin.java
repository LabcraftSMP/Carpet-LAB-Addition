package carpetlabaddition.mixins;

import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.server.filter.FilteredMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static carpetlabaddition.LABEvents.PLAYER_EDITS_SIGN;
import static carpetlabaddition.LABEvents.PLAYER_MESSAGE_BROADCAST;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "handleDecoratedMessage", cancellable = true, at = @At("HEAD"))
    public void interceptChatMessageLAB(SignedMessage message, CallbackInfo ci) {
        if (PLAYER_MESSAGE_BROADCAST.onPlayerMessageBroadcast(player, message.getContent()))
            ci.cancel();
    }

    @Inject(method = "onSignUpdate", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/SignBlockEntity;tryChangeText(Lnet/minecraft/entity/player/PlayerEntity;ZLjava/util/List;)V",
            shift = At.Shift.AFTER
    ))
    private void onSignUpdateLAB(UpdateSignC2SPacket packet, List<FilteredMessage> signText, CallbackInfo ci) {
        PLAYER_EDITS_SIGN.onPlayerEditsSign(player, packet.getPos());
    }
}
