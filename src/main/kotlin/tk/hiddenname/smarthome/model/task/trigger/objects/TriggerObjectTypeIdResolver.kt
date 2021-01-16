package tk.hiddenname.smarthome.model.task.trigger.objects

import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import tk.hiddenname.smarthome.model.task.trigger.TriggerAction

class TriggerObjectTypeIdResolver : TypeIdResolverBase() {

    private var superType: JavaType? = null

    override fun init(baseType: JavaType) {
        superType = baseType
    }

    override fun idFromValue(o: Any) = null

    override fun idFromValueAndType(o: Any, aClass: Class<*>?) = null

    override fun getMechanism() = null

    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        val subType = when (TriggerAction.getTriggerAction(id)) {
            TriggerAction.CHANGE_DIGITAL_SIGNAL -> ChangeDigitalSignalObject::class.java
        }

        return context.constructSpecializedType(superType, subType)
    }
}