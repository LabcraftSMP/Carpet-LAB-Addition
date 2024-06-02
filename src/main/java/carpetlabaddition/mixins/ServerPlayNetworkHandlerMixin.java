package carpetlabaddition.mixins;

import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static carpetlabaddition.LABEvents.PLAYER_MESSAGE_BROADCAST;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "handleDecoratedMessage", cancellable = true, at = @At("HEAD"))
    public void interceptChatMessageLAB(SignedMessage message, CallbackInfo ci) {
        if (PLAYER_MESSAGE_BROADCAST.onPlayerMessageBroadcast(player, message.getContent())) {
            ci.cancel();
        }
    }
}
