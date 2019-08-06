package me.jhwang.test

import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.remove
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import javax.annotation.PostConstruct

@Component
class CustomerRepository (private val template: ReactiveMongoTemplate)  {

    companion object {
        val initialCustomers  = listOf (
                Customer(1, "jhwang"),
                Customer(2, "lahui"),
                Customer(3, "seolhyun", Customer.Telephone("82", "1029637530"))
        )
        val logger = LoggerFactory.getLogger(this::class.java)
    }

    @PostConstruct
    fun initializeRepository()  =
            initialCustomers.map(Customer::toMono)
                    .map(this::create)
                    .map(Mono<Customer>::subscribe)

    fun create(customer: Mono<Customer>) = template.save(customer)

    fun findById(id: Int) = template.findById<Customer>(id)

    fun deleteById(id: Int) = template.remove<Customer>(
            Query(where("_id").isEqualTo(id))
    )

    fun findCustomer(nameFilter: String) = template.find<Customer>(
            Query(where("name").regex(".*$nameFilter*", "i"))
    )
}
