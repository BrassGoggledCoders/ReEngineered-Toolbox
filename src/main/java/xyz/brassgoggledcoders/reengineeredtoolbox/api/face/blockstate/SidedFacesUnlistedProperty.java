package xyz.brassgoggledcoders.reengineeredtoolbox.api.face.blockstate;

import com.teamacronymcoders.base.util.TextUtils;
import net.minecraftforge.common.property.IUnlistedProperty;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.face.Face;

public class SidedFacesUnlistedProperty implements IUnlistedProperty<Face[]> {
    @Override
    public String getName() {
        return "sided_faces";
    }

    @Override
    public boolean isValid(Face[] value) {
        boolean valid = true;
        if (value != null) {
            for (Face face : value) {
                valid &= face != null;
            }
        } else {
            valid = false;
        }
        return valid;
    }

    @Override
    public Class<Face[]> getType() {
        return Face[].class;
    }

    @Override
    public String valueToString(Face[] value) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            stringBuilder.append(Integer.toString(i))
                    .append("=")
                    .append(TextUtils.getRegistryLocation(value[i]))
                    .append(",");
        }
        stringBuilder.delete(stringBuilder.lastIndexOf(","), stringBuilder.length());
        return stringBuilder.toString();
    }
}
