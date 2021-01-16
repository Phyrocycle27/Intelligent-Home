package tk.hiddenname.smarthome.service.hardware.impl

import tk.hiddenname.smarthome.model.hardware.GPIO

interface GPIOService {

    fun delete(id: Long)

    fun save(id: Long, gpio: GPIO, reverse: Boolean)

    fun update(id: Long, reverse: Boolean)
}