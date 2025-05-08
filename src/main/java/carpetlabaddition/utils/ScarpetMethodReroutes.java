package carpetlabaddition.utils;

import carpet.script.value.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.DynamicRegistryManager;

public class ScarpetMethodReroutes {
    private static NbtElement listValueToTag(ListValue value, boolean force, DynamicRegistryManager regs) {
        NbtList tag = new NbtList();
        value.getItems().forEach(v -> tag.add(valueToTag(v, force, regs)));
        return tag;
    }

    private static NbtElement mapValueToTag(MapValue value, boolean force, DynamicRegistryManager regs) {
        NbtCompound tag = new NbtCompound();
        value.getMap().forEach((k, v) -> {
            if (!force && !(k instanceof StringValue))
                throw new NBTSerializableValue.IncompatibleTypeException(k);

            tag.put(k.getString(), valueToTag(v, force, regs));
        });
        return tag;
    }

    public static NbtElement valueToTag(Value value, boolean force, DynamicRegistryManager regs) {
        if (value instanceof ListValue listValue) return listValueToTag(listValue, force, regs);
        if (value instanceof MapValue mapValue) return mapValueToTag(mapValue, force, regs);

        return value.toTag(force, regs);
    }
}
