package tk.hiddenname.smarthome.entity.task.trigger.objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import tk.hiddenname.smarthome.entity.task.trigger.TriggerAction;
import tk.hiddenname.smarthome.exception.InvalidTriggerObjectTypeException;

public class TriggerObjectTypeIdResolver extends TypeIdResolverBase {

    private JavaType superType;

    @Override
    public void init(JavaType baseType) {
        superType = baseType;
    }

    @Override
    public String idFromValue(Object o) {
        return null;
    }

    @Override
    public String idFromValueAndType(Object o, Class<?> aClass) {
        return null;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return null;
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        TriggerAction action;
        Class<?> subType = null;

        try {
            action = TriggerAction.valueOf(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidTriggerObjectTypeException();
        }

        switch (action) {
            case CHANGE_DIGITAL_SIGNAL:
                subType = ChangeDigitalSignalObject.class;
                break;
        }

        return context.constructSpecializedType(superType, subType);
    }
}
