package me.jhwang.test

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.ServerResponse.status
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.toMono
import java.net.URI

@Component
class CustomerHandler(val customerService: CustomerService) {
    fun get(serverRequest: ServerRequest) =
            customerService.getCustomer(serverRequest.pathVariable("id").toInt())
                    .flatMap { ok().body(BodyInserters.fromObject(it)) }
                    .switchIfEmpty(status(HttpStatus.NOT_FOUND).build())

    fun post(serverRequest: ServerRequest) =
            customerService.createCustomer(serverRequest.bodyToMono())
                    .flatMap {
                        ServerResponse.created(URI.create("/customer/${it.id}")).build() }

    fun delete(serverRequest: ServerRequest) =
            customerService.deleteCustomer(serverRequest.pathVariable("id").toInt())
                    .flatMap {
                        if(it) ok().build()
                        else status(HttpStatus.NOT_FOUND).build()
                    }

    fun search(serverRequest: ServerRequest) =
            ok().body(customerService.searchCustomer(serverRequest.queryParam("nameFilter").orElse("")), Customer::class.java)
//                    .flatMap { ok().body(fromObject(it)) }

}

@Component
class CustomerRouter(private val customerHandler: CustomerHandler) {
    @Bean
    fun customerRoutes() = router {
        "/customer".nest {
            GET("/{id}", customerHandler::get)
            POST("/", customerHandler::post)
            DELETE("/{id}", customerHandler::delete)
        }
        "/customers".nest {
            GET("/", customerHandler::search)
        }
    }
}
