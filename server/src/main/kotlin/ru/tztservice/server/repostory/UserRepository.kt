package ru.tztservice.server.repostory

import org.springframework.data.mongodb.repository.MongoRepository
import ru.tztservice.server.domain.DomainUser

interface UserRepository : MongoRepository<DomainUser, String>