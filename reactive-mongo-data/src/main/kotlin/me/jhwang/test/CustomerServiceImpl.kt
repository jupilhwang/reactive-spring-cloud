package me.jhwang.test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CustomerService {
    fun getCustomer(id: Int): Mono<Customer>
    fun createCustomer(customer: Mono<Customer>): Mono<Customer>
    fun deleteCustomer(id: Int): Mono<Boolean>
    fun searchCustomer(nameFilter: String): Flux<Customer>
}

@Component
class CustomerServiceImpl: CustomerService {
    @Autowired
    lateinit var customerRepository: CustomerRepository

    override fun getCustomer(id: Int): Mono<Customer> = customerRepository.findById(id)
    override fun createCustomer(customer: Mono<Customer>): Mono<Customer> = customerRepository.create(customer)
    override fun deleteCustomer(id: Int): Mono<Boolean> = customerRepository.deleteById(id).map { it.deletedCount > 0 }
    override fun searchCustomer(nameFilter: String): Flux<Customer> = customerRepository.findCustomer(nameFilter)
}