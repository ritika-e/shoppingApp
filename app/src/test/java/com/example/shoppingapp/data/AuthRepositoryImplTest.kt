import android.util.Log
import com.example.shoppingapp.data.auth.AuthRepositoryImpl
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private val firebaseAuth = mockk<FirebaseAuth>()
    private val firestore = mockk<FirebaseFirestore>()
    private val authRepository = AuthRepositoryImpl(firebaseAuth, firestore)

    private val mockUser = mockk<FirebaseUser>()
    private val authResult = mockk<AuthResult> {
        every { user } returns mockUser
    }

    @Before
    fun setup() {
        // Mock static Log methods
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
    }

    /*@Test
    fun `test login success`() = runBlocking {
        val email = "test@example.com"
        val password = "password"

        // Mock FirebaseUser to return a UID and other properties
        val mockUser = mockk<FirebaseUser> {
            every { uid } returns "some-uid"  // Return a mocked UID
            every { displayName } returns "Test User" // Optionally mock displayName if needed
        }

        // Mock Task to simulate a successful result
        val task = mockk<Task<AuthResult>> {
            every { isSuccessful } returns true    // Simulate success
            every { isComplete } returns true      // Mark task as complete
            every { isCanceled } returns false     // Mark task as not canceled
            every { result } returns authResult    // Return the authResult containing mockUser
            every { exception } returns null       // No exception for success
        }

        // Mock FirebaseAuth to return the task when signInWithEmailAndPassword is called
        coEvery { firebaseAuth.signInWithEmailAndPassword(email, password) } returns task

        // Call the login method
        val result = authRepository.login(email, password)

        // Print the result for debugging
        println("Login result: $result")

        // Assert that the result is a success
        assertTrue(result.isSuccess)
    }
*/
    @Test
    fun `test login failure`() = runBlocking {
        val email = "wrong@example.com"
        val password = "wrongPassword"

        // Mock Task to simulate a failure
        val task = mockk<Task<AuthResult>> {
            every { isSuccessful } returns false      // Simulate failure
            every { exception } returns Exception("Login failed") // Simulate exception
        }

        // Mock FirebaseAuth to return the task when signInWithEmailAndPassword is called
        coEvery { firebaseAuth.signInWithEmailAndPassword(email, password) } returns task

        // Call the login method
        val result = authRepository.login(email, password)

        // Assert that the result is a failure
        assertTrue(result.isFailure)
    }
}
