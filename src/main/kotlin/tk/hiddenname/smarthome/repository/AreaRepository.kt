package tk.hiddenname.smarthome.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tk.hiddenname.smarthome.model.Area

@Repository
interface AreaRepository : JpaRepository<Area, Long>