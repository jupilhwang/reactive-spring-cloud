package me.jhwang.cloudgateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
//@EnableSwagger2WebFlux
class ReactiveCloudGatewayApplication

fun main(args: Array<String>) {
	runApplication<ReactiveCloudGatewayApplication>(*args)
}
