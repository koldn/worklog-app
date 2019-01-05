package ru.tztservice.domain

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