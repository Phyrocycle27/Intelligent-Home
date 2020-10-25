package tk.hiddenname.smarthome.model.task.processing.objects

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import tk.hiddenname.smarthome.exception.InvalidProcessingObjectTypeException
import tk.hiddenname.smarthome.exception.ProcessingObjectTypeNotSpecifiedException
import tk.hiddenname.smarthome.model.task.processing.ProcessingAction

class ProcessingObjectTypeIdResolver : TypeIdResolverBase() {

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

    @Throws(ProcessingObjectTypeNotSpecifiedException::class, InvalidProcessingObjectTypeException::class)
    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        val action: ProcessingAction = try {
            ProcessingAction.valueOf(id)
        } catch (e: IllegalArgumentException) {
            throw InvalidProcessingObjectTypeException(id)
        }

        val subType = when (action) {
            ProcessingAction.SET_PWM_SIGNAL -> SetPwmSignalObject::class.java
            ProcessingAction.SET_DIGITAL_SIGNAL -> SetDigitalSignalObject::class.java
            ProcessingAction.NOT_SPECIFIED -> throw ProcessingObjectTypeNotSpecifiedException()
        }

        return context.constructSpecializedType(superType, subType)
    }
}