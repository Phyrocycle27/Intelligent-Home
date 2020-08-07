package tk.hiddenname.smarthome.entity.task.processing.objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import tk.hiddenname.smarthome.entity.task.processing.ProcessingAction;

public class ProcessingObjectTypeIdResolver extends TypeIdResolverBase {

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
        ProcessingAction action;
        Class<?> subType = null;

        try {
            action = ProcessingAction.valueOf(id);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ProcessingObject type");
        }

        switch (action) {
            case SET_SIGNAL:
                subType = SetSignalObject.class;
                break;
        }

        return context.constructSpecializedType(superType, subType);
    }
}
