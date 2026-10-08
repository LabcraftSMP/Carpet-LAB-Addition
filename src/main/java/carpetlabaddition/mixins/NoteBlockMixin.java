package carpetlabaddition.mixins;

import carpetlabaddition.LABSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.entity.SignTextSlot;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
    @Unique private static final Pattern carpetLABAddition$MARKER_PATTERN = Pattern.compile(" *\\[ *BROADCAST *(\\d+)? *] *");

    @Unique
    private static Component carpetLABAddition$FlattenText(SignText text, boolean skipFirstLine) {
        MutableComponent message = Component.empty()
                .withColor(text.getColor().getTextColor());

        for (Component line : text.getMessages(false)) {
            if (skipFirstLine) {
                skipFirstLine = false;
                continue;
            }
            message.append(line);
        }

        return message;
    }

    @Inject(method = "playNote", at = @At("HEAD"), cancellable = true)
    private void broadcastSign(Entity source, BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        if (!LABSettings.noteBlocksBroadcastSigns) return;
        if (!(level.getBlockState(pos.above()).getBlock() instanceof StandingSignBlock)) return;
        if (!(level.getBlockEntity(pos.above()) instanceof SignBlockEntity blockEntity)) return;

        SignText frontText = blockEntity.getText(SignTextSlot.FRONT);
        String markerLine = frontText.getMessages(false).getFirst().getString().toUpperCase();
        Matcher matcher = carpetLABAddition$MARKER_PATTERN.matcher(markerLine);
        if (!matcher.find()) return;

        ci.cancel();

        MutableComponent message = Component.empty();
        message.append(carpetLABAddition$FlattenText(frontText, true));
        message.append(carpetLABAddition$FlattenText(blockEntity.getText(SignTextSlot.BACK), false));
        if (message.getString().isBlank()) return;

        int rangeSqr = Mth.square(matcher.group(1) instanceof String rangeCapture ?
                Mth.clamp(Integer.parseInt(rangeCapture), 1, 48) :
                24
        );
        for (Player player : level.players()) {
            if (player.level().dimension() != level.dimension()) continue;
            if (pos.distToCenterSqr(player.position()) > rangeSqr) continue;

            player.sendSystemMessage(message);
        }
    }
}
