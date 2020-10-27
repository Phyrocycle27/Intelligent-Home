package tk.hiddenname.smarthome.model

import javax.persistence.*

@MappedSuperclass
abstract class AbstractJpaPersistable {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
}