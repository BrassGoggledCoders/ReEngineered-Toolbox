package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

public class TypedSlotRenderProperties {
    public static ITypedSlotRenderProperties getProperties(ITypedSlot<?> typedSlot) {
        if (typedSlot.getType().getRenderProperties() instanceof ITypedSlotRenderProperties renderProperties) {
            return renderProperties;
        } else {
            return null;
        }
    }
}
