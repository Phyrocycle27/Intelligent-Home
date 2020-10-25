package tk.hiddenname.smarthome.model.task.trigger.objects

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import tk.hiddenname.smarthome.exception.InvalidTriggerObjectTypeException
import tk.hiddenname.smarthome.exception.TriggerObjectTypeNotSpecifiedException
import tk.hiddenname.smarthome.model.task.trigger.TriggerAction

class TriggerObjectTypeIdResolver : TypeIdResolverBase() {

    private var superType: JavaType? = null

    override fun init(baseType: JavaType) {
        superType = baseType
    }

    override fun idFromValue(o: Any): String? {
        return null
    }

    override fun idFromValueAndType(o: Any, aClass: Class<*>?): String? {
        return null
    }

    override fun getMechanism(): JsonTypeInfo.Id? {
        return null
    }

    @Throws(TriggerObjectTypeNotSpecifiedException::class, InvalidTriggerObjectTypeException::class)
    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        val action: TriggerAction = try {
            TriggerAction.valueOf(id)
        } catch (e: IllegalArgumentException) {
            throw InvalidTriggerObjectTypeException(id)
        }

        val subType = when (action) {
            TriggerAction.CHANGE_DIGITAL_SIGNAL -> ChangeDigitalSignalObject::class.java
            TriggerAction.NOT_SPECIFIED -> throw TriggerObjectTypeNotSpecifiedException()
        }

        return context.constructSpecializedType(superType, subType)
    }
}