package carpetlabaddition.utils;

import carpet.script.value.*;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class ScarpetMethodReroutes {
    private static Tag listValueToTag(ListValue value, boolean force, RegistryAccess regs) {
        ListTag tag = new ListTag();
        value.getItems().forEach(v -> tag.add(valueToTag(v, force, regs)));
        return tag;
    }

    private static Tag mapValueToTag(MapValue value, boolean force, RegistryAccess regs) {
        CompoundTag tag = new CompoundTag();
        value.getMap().forEach((k, v) -> {
            if (!force && !(k instanceof StringValue))
                throw new NBTSerializableValue.IncompatibleTypeException(k);

            tag.put(k.getString(), valueToTag(v, force, regs));
        });
        return tag;
    }

    public static Tag valueToTag(Value value, boolean force, RegistryAccess regs) {
        if (value instanceof ListValue listValue) return listValueToTag(listValue, force, regs);
        if (value instanceof MapValue mapValue) return mapValueToTag(mapValue, force, regs);

        return value.toTag(force, regs);
    }
}
