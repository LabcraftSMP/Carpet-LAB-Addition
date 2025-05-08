# Carpet LAB Addition

Carpet features created for the LAB SMP, as we need them. Currently only contains features for Scarpet.

<hr />

Scarpet events:

`__on_player_message_broadcast(player, message)`
- Triggers right before a player message is broadcast to the server.
- Can be cancelled by returning 'cancel', which prevents the message from being sent.
- Useful for making changes to chats before they're sent.

`__on_player_edits_sign(player, block)`
- Fires after a player edits a sign, after the vanilla text changes take place.
- Can be used to add custom sign formatting.
- [Example script](https://github.com/chililisoup/Scarpet-Scripts/blob/main/world/format_sign.sc)

Scarpet functions:

`encode_snbt(expr, force?)`
- Essentially the same as `encode_nbt(expr, force?)`, except it doesn't error in 1.20.5+
- This is done by allowing heterogeneous lists, to bring it in line with vanilla's [new NBT format](https://minecraft.wiki/w/Java_Edition_1.21.5#Changes_2)
