package tk.hiddenname.smarthome.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.model.Area
import tk.hiddenname.smarthome.service.database.AreaDatabaseService
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/areas"])
class AreaRestController(private val dbService: AreaDatabaseService) {

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun getAll(): List<Area> = dbService.getAll()

    @GetMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun getOne(@PathVariable(name = "id", required = true) id: Long): Area = dbService.getOne(id)

    @PostMapping(value = ["/create"], produces = ["application/json"])
    fun create(@RequestBody(required = true) newArea: @Valid Area): Area {
        return dbService.create(newArea)
    }

    @DeleteMapping(value = ["/one/{id}"])
    fun delete(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Any> {
        dbService.delete(id)
        return ResponseEntity.noContent().build()
    }
}