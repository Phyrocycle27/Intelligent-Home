package tk.hiddenname.smarthome.controller

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import tk.hiddenname.smarthome.model.Area
import tk.hiddenname.smarthome.service.database.AreaDatabaseService
import javax.validation.Valid
import javax.validation.constraints.Min

@Validated
@RestController
@RequestMapping(value = ["/areas"])
open class AreaRestController(private val dbService: AreaDatabaseService) {

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun getAll() = dbService.getAll()

    @GetMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun getOne(@Min(1) @PathVariable(name = "id", required = true) id: Long) = dbService.getOne(id)

    @PostMapping(value = ["/create"], produces = ["application/json"])
    fun create(@Valid @RequestBody(required = true) newArea: Area) = dbService.create(newArea)

    @DeleteMapping(value = ["/one/{id}"])
    fun delete(@Min(1) @PathVariable(name = "id", required = true) id: Long): ResponseEntity<Any> {
        dbService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping(value = ["/one/{id}"], produces = ["application/json"])
    fun update(@Valid @RequestBody(required = true) newArea: Area,
               @Min(1) @PathVariable(name = "id", required = true) id: Long) = dbService.update(id, newArea)
}