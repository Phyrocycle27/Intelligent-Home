package tk.hiddenname.smarthome.model.timetable

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.vladmihalcea.hibernate.type.array.ListArrayType
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import org.hibernate.annotations.Parameter
import org.hibernate.annotations.Type
import java.time.DayOfWeek
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "timetable_by_days_of_week")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@JsonPropertyOrder(value = ["id", "mode", "days_of_week"])
class TimetableByDaysOfWeek(
    timetableMode: TimetableMode? = null,

    @field:NotEmpty(message = "days of week list can not be empty", groups = [TimetableValidationGroup::class])
    @Type(
        type = "com.vladmihalcea.hibernate.type.array.ListArrayType",
        parameters = [Parameter(value = "day_of_week", name = ListArrayType.SQL_ARRAY_TYPE)]
    )
    @Column(name = "days_of_week", columnDefinition = "day_of_week[]")
    @LazyCollection(LazyCollectionOption.FALSE)
    val daysOfWeek: MutableList<DayOfWeek> = mutableListOf()
) : Timetable(timetableMode)