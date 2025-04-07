package com.example.shoppingapp.data.admin

import android.util.Log
import com.example.shoppingapp.domain.model.Customer
import com.example.shoppingapp.domain.model.Order
import com.example.shoppingapp.domain.repositories.CustomerRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CustomerRepositoryImpl(private val firebaseFirestore: FirebaseFirestore): CustomerRepository {

    // Get all customers
   /* override suspend fun getAllCustomers(): Result<List<Customer>> {
        return try {
            val querySnapshot = firebaseFirestore.collection("users").get().await()
              val customers = querySnapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Customer::class.java)?.also {
                        Log.d("CustomerRepository", "Deserialized customer: ${it.name}, ${it.email}")
                    }
                } catch (e: Exception) {
                    Log.e("CustomerRepository", "Error deserializing customer: ${e.message}")
                    null
                }
            }
            Result.success(customers) // Return customers as a success result
        } catch (e: Exception) {
            Log.e("CustomerRepository", "Error fetching customers from Firestore: ${e.message}")
            Result.failure(e) // Return failure in case of an error
        }
    }*/
    override suspend fun getAllCustomers(): Result<List<Customer>> {
        return try {
            // Modify the query to filter by role as "customer"
            val querySnapshot = firebaseFirestore.collection("users")
                .whereEqualTo("role", "customer") // Only fetch customers with role "customer"
                .get()
                .await()

            // Deserialize documents into Customer objects
            val customers = querySnapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Customer::class.java)?.also {
                        Log.d("CustomerRepository", "Deserialized customer: ${it.name}, ${it.email}")
                    }
                } catch (e: Exception) {
                    Log.e("CustomerRepository", "Error deserializing customer: ${e.message}")
                    null
                }
            }

            // Return the list of customers as a success result
            Result.success(customers)
        } catch (e: Exception) {
            Log.e("CustomerRepository", "Error fetching customers from Firestore: ${e.message}")
            Result.failure(e) // Return failure in case of an error
        }
    }



    // Get customer by ID
    override suspend fun getCustomerById(customerId: String): Customer? {
        return try {
            val documentSnapshot = firebaseFirestore.collection("users")
                .whereEqualTo("userId", customerId)
                .get()
                .await()

            // Check if the document exists
            if (documentSnapshot.isEmpty) {
                 Log.d("CustomerRepository", "No customer found for ID: $customerId")
                null
            } else {
                // Log if the document doesn't exist
                val customer = documentSnapshot.documents.firstOrNull()?.toObject(Customer::class.java)
                Log.d("CustomerRepository", "Customer found: ${customer?.name}, ${customer?.email}")
                customer
            }
        } catch (e: Exception) {
            // Log any errors during the fetch
            Log.e("CustomerRepository", "Error fetching customer with ID: $customerId - ${e.message}")
            null
        }
    }




    override suspend fun getOrdersByCustomerId(customerId: String): Result<List<Order>> {
        return try {
            Log.d("CustomerRepository", "Fetching orders for customer ID: $customerId")

            val querySnapshot = firebaseFirestore.collection("orders")
                .whereEqualTo("userId", customerId)
                .get()
                .await()

            Log.d("CustomerRepository", "Received query snapshot for orders: ${querySnapshot.size()} documents found.")

            val orders = querySnapshot.documents.mapNotNull { doc ->
                try {
                    val order = doc.toObject(Order::class.java)
                    Log.d("CustomerRepository", "Deserialized order ID: ${order?.orderId}, Total: ${order?.totalAmount}")
                    order
                } catch (e: Exception) {
                    Log.e("CustomerRepository", "Error deserializing order: ${e.message}")
                    null
                }
            }

            Log.d("CustomerRepository", "Successfully fetched ${orders.size} orders.")
            Result.success(orders)
        } catch (e: Exception) {
            Log.e("CustomerRepository", "Error fetching orders for customer ID: $customerId - ${e.message}")
            Result.failure(e)
        }
    }


    // Delete customer by ID
    override suspend fun deleteCustomer(customerId: String): Result<Boolean> {
        return try {
            // Attempt to delete the customer from Firestore
            firebaseFirestore.collection("customers")
                .document(customerId)
                .delete()
                .await()

            // Return true if the deletion is successful
            Result.success(true)
        } catch (e: Exception) {
            // In case of failure, return the exception
            Result.failure(e)
        }
    }
}
