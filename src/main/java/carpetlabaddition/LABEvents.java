package carpetlabaddition;

import carpet.script.CarpetEventServer.Event;
import carpet.script.value.BlockValue;
import carpet.script.value.EntityValue;
import carpet.script.value.FormattedTextValue;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

public class LABEvents extends Event {
    public static void noop() {} //to load events before scripts do

    public LABEvents(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }

    public boolean onPlayerMessageBroadcast(ServerPlayerEntity player, Text message) { return false; }
    public static final LABEvents PLAYER_MESSAGE_BROADCAST = new LABEvents("player_message_broadcast", 2, false) {
        public boolean onPlayerMessageBroadcast(ServerPlayerEntity player, Text message) {
            return handler.call(() ->
                    Arrays.asList(
                            new EntityValue(player),
                            FormattedTextValue.of(message)
                    ), player::getCommandSource
            );
        }
    };

    public void onPlayerEditsSign(ServerPlayerEntity player, BlockPos pos) {}
    public static final LABEvents PLAYER_EDITS_SIGN = new LABEvents("player_edits_sign", 2, false) {
        public void onPlayerEditsSign(ServerPlayerEntity player, BlockPos pos) {
            handler.call(() ->
                    Arrays.asList(
                            new EntityValue(player),
                            new BlockValue(player.getServerWorld(), pos)
                    ), player::getCommandSource
            );
        }
    };
}
