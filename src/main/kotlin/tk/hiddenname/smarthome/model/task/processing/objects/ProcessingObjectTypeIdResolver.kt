package tk.hiddenname.smarthome.model.task.processing.objects

import com.fasterxml.jackson.databind.DatabindContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import tk.hiddenname.smarthome.exception.invalid.InvalidProcessingActionException
import tk.hiddenname.smarthome.model.task.processing.ProcessingAction

class ProcessingObjectTypeIdResolver : TypeIdResolverBase() {

    private var superType: JavaType? = null

    override fun init(baseType: JavaType) {
        superType = baseType
    }

    override fun idFromValue(o: Any) = null

    override fun idFromValueAndType(o: Any, aClass: Class<*>?) = null

    override fun getMechanism() = null

    @Throws(InvalidProcessingActionException::class)
    override fun typeFromId(context: DatabindContext, id: String): JavaType {
        val subType = when (ProcessingAction.getProcessingAction(id)) {
            ProcessingAction.SET_PWM_SIGNAL -> SetPwmSignalObject::class.java
            ProcessingAction.SET_DIGITAL_SIGNAL -> SetDigitalSignalObject::class.java
        }

        return context.constructSpecializedType(superType, subType)
    }
}