package ru.tztservice.domain

import java.time.Instant
import java.util.*
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
        Index(name = "idx_start_time", columnList = "start_time"),
        Index(name = "idx_user_id", columnList = "user_id")
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
    @Column(name = "user_id")
    val userID: Long,
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long = -1L
)