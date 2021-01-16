package tk.hiddenname.smarthome.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(value = ["/config/client"])
class ServerConnectionRestController {

    private val log = LoggerFactory.getLogger(ServerConnectionRestController::class.java)

    @PostMapping(value = ["/edit"], produces = ["multipart/form-data"])
    fun edit(
        @RequestParam(value = "allowConnection", required = false) allowConnection: Boolean?,
        @RequestParam(value = "token", required = false) token: String?,
        @RequestParam(value = "serverHost", required = false) serverHost: String?
    ) {

    }
}