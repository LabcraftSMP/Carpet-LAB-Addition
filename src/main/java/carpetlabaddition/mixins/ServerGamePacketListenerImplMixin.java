package carpetlabaddition.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static carpetlabaddition.LABEvents.PLAYER_EDITS_SIGN;
import static carpetlabaddition.LABEvents.PLAYER_MESSAGE_BROADCAST;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "lambda$handleChat$0", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;filterTextPacket(Ljava/lang/String;)Ljava/util/concurrent/CompletableFuture;"),
            cancellable = true)
    public void interceptChatMessage(CallbackInfo ci, @Local(name = "signedMessage") PlayerChatMessage signedMessage) {
        if (PLAYER_MESSAGE_BROADCAST.onPlayerMessageBroadcast(player, signedMessage.decoratedContent()))
            ci.cancel();
    }

    @Inject(method = "updateSignText", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/SignBlockEntity;updateSignText(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/entity/SignTextSlot;Ljava/util/List;)V",
            shift = At.Shift.AFTER
    ))
    private void interceptSignUpdate(ServerboundSignUpdatePacket packet, List<FilteredText> lines, CallbackInfo ci) {
        PLAYER_EDITS_SIGN.onPlayerEditsSign(player, packet.pos());
    }
}
