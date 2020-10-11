package tk.hiddenname.smarthome.controller

import org.jetbrains.annotations.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.model.Area
import tk.hiddenname.smarthome.service.database.AreaDatabaseService
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/areas"])
class AreaRestController(private val dbService: AreaDatabaseService) {

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun getAll(): List<Area> = dbService.all

    @PostMapping(value = ["/create"], produces = ["application/json"])
    fun create(@RequestBody @NotNull newArea: @Valid Area): Area {
        return dbService.create(newArea)
    }

    @DeleteMapping(value = ["/one/{id}"])
    fun delete(@PathVariable(name = "id") id: Long): ResponseEntity<Any> {
        dbService.delete(id)
        return ResponseEntity.noContent().build()
    }
}