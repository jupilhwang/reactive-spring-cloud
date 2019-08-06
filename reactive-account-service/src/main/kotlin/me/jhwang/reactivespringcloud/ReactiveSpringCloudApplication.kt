package me.jhwang.reactivespringcloud

import feign.Logger
import feign.Response
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
class ReactiveSpringCloudApplication

fun main(args: Array<String>) {
    runApplication<ReactiveSpringCloudApplication>(*args)
}

//@Configuration
//class MyWebBuilderConfiguration {
//
//    @Bean
//    @LoadBalanced
//    fun loadbalancedWebClientBuilder(): WebClient.Builder = WebClient.builder()
//}

@RestController
@RequestMapping("/account")
class MyClass {
    companion object {
        var log = LoggerFactory.getLogger(this::class.java)
    }
//    @Autowired
    //var webClientBuilder: WebClient.Builder

    @Autowired
    lateinit var lbFilterFunction: LoadBalancerExchangeFilterFunction

    @GetMapping("/{id}")
    fun doSomthing(@PathVariable id: Int)  =
//        webClientBuilder.build().get().uri("http://customer-service/customer/${id}").retrieve().bodyToMono(String::class.java)
        WebClient.builder().baseUrl("http://customer-service")
                .filter(lbFilterFunction).build().get().uri("/customer/${id}")
                .retrieve().bodyToMono(Customer::class.java)

    @Autowired
    lateinit var myFeignClient: MyFeignClass

    @GetMapping("/feign/{id}")
    fun feignSomthing(@PathVariable id: Int) = myFeignClient.doSomthing(id)
}

@FeignClient("customer-service")
interface MyFeignClass {
    @GetMapping("/customer/{id}")
    fun doSomthing(@PathVariable id: Int): Customer
}

@Configuration
class FeignLogConfigure {

    @Bean
    fun feignLoggerLevel() = Logger.Level.FULL
}