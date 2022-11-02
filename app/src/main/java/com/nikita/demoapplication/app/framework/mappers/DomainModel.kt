package com.nikita.demoapplication.app.framework.mappers

interface DomainModel<T> {

        fun toDomain():T
}