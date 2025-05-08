package carpetlabaddition;

import carpet.script.CarpetContext;
import carpet.script.Context;
import carpet.script.annotation.ScarpetFunction;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.NBTSerializableValue;
import carpet.script.value.Value;
import carpetlabaddition.utils.ScarpetMethodReroutes;
import net.minecraft.nbt.NbtElement;

@SuppressWarnings("unused")
public class LABFunctions {
    @ScarpetFunction(maxParams = 2)
    public NBTSerializableValue encode_snbt(Context context, Value input, boolean... force) {
        boolean shouldForce = force != null && force.length == 1 && force[0];
        NbtElement tag;
        
        try {
            tag = ScarpetMethodReroutes.valueToTag(input, shouldForce, ((CarpetContext) context).registryAccess());
        } catch (NBTSerializableValue.IncompatibleTypeException exception) {
            throw new InternalExpressionException("cannot reliably encode to a tag the value of '" + exception.val.getPrettyString() + "'");
        }

        return new NBTSerializableValue(tag);
    }
}
