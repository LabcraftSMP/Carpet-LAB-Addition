package carpetlabaddition;

import carpet.script.CarpetEventServer.Event;
import carpet.script.value.BlockValue;
import carpet.script.value.EntityValue;
import carpet.script.value.FormattedTextValue;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;

public class LABEvents extends Event {
    public static void noop() {} //to load events before scripts do

    public LABEvents(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }

    public boolean onPlayerMessageBroadcast(ServerPlayer player, Component message) { return false; }
    public static final LABEvents PLAYER_MESSAGE_BROADCAST = new LABEvents("player_message_broadcast", 2, false) {
        public boolean onPlayerMessageBroadcast(ServerPlayer player, Component message) {
            return handler.call(() ->
                    Arrays.asList(
                            new EntityValue(player),
                            FormattedTextValue.of(message)
                    ), player::createCommandSourceStack
            );
        }
    };

    public void onPlayerEditsSign(ServerPlayer player, BlockPos pos) {}
    public static final LABEvents PLAYER_EDITS_SIGN = new LABEvents("player_edits_sign", 2, false) {
        public void onPlayerEditsSign(ServerPlayer player, BlockPos pos) {
            handler.call(() ->
                    Arrays.asList(
                            new EntityValue(player),
                            new BlockValue(player.level(), pos)
                    ), player::createCommandSourceStack
            );
        }
    };
}
