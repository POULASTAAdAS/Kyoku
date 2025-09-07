package com.poulastaa.kyoku.auth.database.entity

import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy

@MappedSuperclass
class BaseIdEntity<T : Number>(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: T = -1 as T,
) {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass

        if (thisEffectiveClass != oEffectiveClass) return false
        other as BaseIdEntity<*>

        return id == other.id
    }

    final override fun hashCode() = if (this is HibernateProxy)
        this.hibernateLazyInitializer.persistentClass.hashCode()
    else javaClass.hashCode()
}