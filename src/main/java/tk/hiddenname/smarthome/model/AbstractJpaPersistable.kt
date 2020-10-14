package tk.hiddenname.smarthome.model

import javax.persistence.*

@MappedSuperclass
abstract class AbstractJpaPersistable {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(updatable = false, nullable = false)
        var id: Long = 0L
}