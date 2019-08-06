package me.jhwang.test

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@SpringBootApplication
class ReactiveMongoDataApplication

fun main(args: Array<String>) {
	runApplication<ReactiveMongoDataApplication>(*args)
}
