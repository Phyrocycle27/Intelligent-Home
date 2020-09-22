package tk.hiddenname.smarthome.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.hiddenname.smarthome.entity.Area;
import tk.hiddenname.smarthome.service.database.AreaDatabaseService;

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
}
