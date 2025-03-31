package com.example.shoppingapp.data.admin

import com.example.shoppingapp.domain.model.Customer
import com.example.shoppingapp.domain.model.Order
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class CustomerRepositoryImplTest {

  /*  private lateinit var customerRepository: CustomerRepositoryImpl
    private val firebaseFirestore: FirebaseFirestore = mock()
    private val querySnapshot: QuerySnapshot = mock() // Corrected
    private val documentSnapshot: DocumentSnapshot = mock()
    private val collectionReference: CollectionReference = mock() // Mocking CollectionReference

    private val task: Task<QuerySnapshot> = mock()

    @Before
    fun setUp(): Unit = runBlocking {
        customerRepository = CustomerRepositoryImpl(firebaseFirestore)

        // Mocking the collection() call
        whenever(firebaseFirestore.collection("users")).thenReturn(collectionReference)

        // Mocking the behavior of get() returning a Task<QuerySnapshot>
        whenever(collectionReference.get()).thenReturn(task)

        // Mocking the await() method to return the mocked QuerySnapshot
        whenever(task.await()).thenAnswer {
            // Simulate Firestore behavior and return the mocked QuerySnapshot when await() is called
            querySnapshot
        }
    }

    // Test getAllCustomers function
    @Test
    fun testGetAllCustomersSuccess() = runTest {
        // Prepare a mock customer document
        val mockCustomer = Customer("1", "John Doe", "john@example.com")

        // Mocking the documents in the querySnapshot to return a list containing a single customer document
        val documentSnapshot: DocumentSnapshot = mock()
        whenever(querySnapshot.documents).thenReturn(listOf(documentSnapshot))

        // Mocking the documentSnapshot to return the mockCustomer when toObject() is called
        whenever(documentSnapshot.toObject(Customer::class.java)).thenReturn(mockCustomer)

        val result = customerRepository.getAllCustomers()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals(mockCustomer, result.getOrNull()?.first())
    }

    @Test
    fun testGetAllCustomersFailure() = runTest {
        // Simulate an error in get()
        whenever(collectionReference.get()).thenReturn(task)
        whenever(task.await()).thenThrow(RuntimeException("Error fetching customers"))

        val result = customerRepository.getAllCustomers()

        assertTrue(result.isFailure)
    }

    @Test
    fun testGetCustomerByIdSuccess() = runTest {
        // Mocking the behavior of whereEqualTo() to return a query snapshot containing a customer
        val customerId = "123"
        val customer = Customer(customerId, "Jane Doe", "jane@example.com")
        val documentSnapshot: DocumentSnapshot = mock()
        whenever(collectionReference.whereEqualTo("customerId", customerId)).thenReturn(collectionReference)
        whenever(collectionReference.get()).thenReturn(task)
        whenever(task.await()).thenReturn(querySnapshot)
        whenever(querySnapshot.documents).thenReturn(listOf(documentSnapshot))
        whenever(documentSnapshot.toObject(Customer::class.java)).thenReturn(customer)

        val result = customerRepository.getCustomerById(customerId)

        assertEquals(customer, result)
    }

    @Test
    fun testGetCustomerByIdFailure() = runTest {
        val customerId = "123"
        whenever(collectionReference.whereEqualTo("customerId", customerId)).thenReturn(collectionReference)
        whenever(collectionReference.get()).thenReturn(task)
        whenever(task.await()).thenThrow(RuntimeException("Error fetching customer"))

        val result = customerRepository.getCustomerById(customerId)

        assertTrue(result == null)
    }


    // Test getOrdersByCustomerId function
    @Test
    fun testGetOrdersByCustomerIdSuccess() = runTest {
        val mockOrder = Order("1", listOf(), 100.0, 100.0, "Completed", "2025-03-30", "1")
        whenever(firebaseFirestore.collection("orders").whereEqualTo("userId", "1").get())
            .thenReturn(task)
        whenever(querySnapshot.size()).thenReturn(1)
        whenever(querySnapshot.documents).thenReturn(listOf(documentSnapshot))
        whenever(documentSnapshot.toObject(Order::class.java)).thenReturn(mockOrder)

        val result = customerRepository.getOrdersByCustomerId("1")

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun testGetOrdersByCustomerIdFailure() = runTest {
        whenever(firebaseFirestore.collection("orders").whereEqualTo("userId", "1").get())
            .thenThrow(RuntimeException("Error fetching orders"))

        val result = customerRepository.getOrdersByCustomerId("1")

        assertTrue(result.isFailure)
    }

    // Test deleteCustomer function
    @Test
    fun testDeleteCustomerSuccess() = runTest {
        whenever(firebaseFirestore.collection("customers").document("1").delete()).thenReturn(mock())

        val result = customerRepository.deleteCustomer("1")

        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
    }

    @Test
    fun testDeleteCustomerFailure() = runTest {
        whenever(firebaseFirestore.collection("customers").document("1").delete())
            .thenThrow(RuntimeException("Error deleting customer"))

        val result = customerRepository.deleteCustomer("1")

        assertTrue(result.isFailure)
        assertFalse(result.getOrNull() ?: false)
    }*/
}
