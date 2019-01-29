package ru.worklog.domain

import java.time.Instant
import javax.persistence.*

/**
 * Пользователь сервиса
 * Содержит в себе имя и пароль
 * @author dkolmogortsev
 */
@Entity
@Table(
    name = "users", indexes = [
        Index(name = "idx_user_name", columnList = "user_name")
    ]
)
data class DomainUser(
    @Column(name = "user_name")
    val userName: String,
    @Column(name = "user_pswd")
    val password: String,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = -1L
)

@Entity
@Table(
    name = "time_entries", indexes = [
        Index(name = "idx_start_time", columnList = "start_time")
    ]
)
data class TimeEntry(
    @Column(name = "start_time")
    var startTimestamp: Instant,
    @Column(name = "end_time")
    var endTimestamp: Instant? = null,
    @Column(name = "short_desc")
    var shortDescription: String = "",
    @Column(name = "burn_comment")
    var burndownComment: String = "",
    @ManyToOne(fetch = FetchType.LAZY)
    val user: DomainUser,
    @OneToOne(fetch = FetchType.LAZY)
    var task: ExternalTask? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = -1L
)

@Entity
@Table(
    name = "ext_tasks"
)
data class ExternalTask(
    @Column(name = "ext_id")
    val extID: String,
    @Column(name = "ext_title")
    val title: String,
    @Column(name = "color")
    var colorCode: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    var user: DomainUser,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = -1L
)
