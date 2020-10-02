package tk.hiddenname.smarthome.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tk.hiddenname.smarthome.entity.Area;
import tk.hiddenname.smarthome.service.database.AreaDatabaseService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = {"/areas"})
@AllArgsConstructor
public class AreaRestController {

    private final AreaDatabaseService dbService;

    @GetMapping(value = {"/all"}, produces = {"application/json"})
    public List<Area> getAll() {
        return dbService.getAll();
    }

    @PostMapping(value = {"/create"}, produces = {"application/json"})
    public Area create(@Valid @RequestBody Area newArea) {
        return dbService.create(newArea);
    }
}
