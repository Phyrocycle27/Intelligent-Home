package tk.hiddenname.smarthome.model

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentityGenerator
import java.io.Serializable

class CustomGenerator : IdentityGenerator() {

    override fun generate(s: SharedSessionContractImplementor?, obj: Any?): Serializable {
        return super.generate(s, obj)
    }
}