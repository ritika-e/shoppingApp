import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shoppingapp.domain.usecase.LoginUserUseCase
import com.example.shoppingapp.presentation.auth.LoginViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val loginUserUseCase: LoginUserUseCase = mockk()

    // Rule to execute tasks synchronously on the main thread
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Setting up the test environment before each test
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        // Initialize the ViewModel with the mocked LoginUserUseCase
     //   viewModel = AuthViewModel(loginUserUseCase)

        // Set the Main dispatcher to a Test dispatcher
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `test validateLoginInput when email is blank`() {
        // Given
        viewModel.validationError.value = null
        viewModel.showDialog.value = false

        // When
        val isValid = viewModel.validateLoginInput("", "password")

        // Then
        assertFalse(isValid)
        assertEquals("Email cannot be blank", viewModel.validationError.value)
        assertTrue(viewModel.showDialog.value == true)
    }

    @Test
    fun `test validateLoginInput when password is blank`() {
        // Given
        viewModel.validationError.value = null
        viewModel.showDialog.value = false

        // When
        val isValid = viewModel.validateLoginInput("test@example.com", "")

        // Then
        assertFalse(isValid)
        assertEquals("Password cannot be blank", viewModel.validationError.value)
        assertTrue(viewModel.showDialog.value == true)
    }

    @Test
    fun `test loginUser when login is successful`() = runBlocking {
        // Given
        val email = "test@example.com"
        val password = "password"
        coEvery { loginUserUseCase.execute(email, password) } just Runs

        // Observer for isLoading
        val isLoadingObserver: Observer<Boolean> = spyk()
        viewModel.isLoading.observeForever(isLoadingObserver)

        // Observer for isLoggedIn
        val isLoggedInObserver: Observer<Boolean> = spyk()
        viewModel.isLoggedIn.observeForever(isLoggedInObserver)

        // When
        viewModel.loginUser(email, password)

        // Then
        verify { isLoadingObserver.onChanged(true) }
        verify { isLoggedInObserver.onChanged(true) }
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `test loginUser when credentials are invalid`() = runBlocking {
        // Given
        val email = "test@example.com"
        val password = "wrongPassword"

        // Mocking the behavior to throw an exception with a message
       // coEvery { loginUserUseCase.execute(email, password) } throws FirebaseAuthInvalidCredentialsException("Invalid credentials")

        // Observer for error state
        val errorObserver: Observer<String?> = spyk()
        viewModel.error.observeForever(errorObserver)

        // Observer for isLoading
        val isLoadingObserver: Observer<Boolean> = spyk()
        viewModel.isLoading.observeForever(isLoadingObserver)

        // When
        viewModel.loginUser(email, password)

        // Then
        verify { isLoadingObserver.onChanged(true) }
        verify { errorObserver.onChanged("Invalid credentials. Please try again.") }
        assertFalse(viewModel.isLoading.value ?: true)
    }
    @Test
    fun `test loginUser when user already exists`() = runBlocking {
        // Given
        val email = "test@example.com"
        val password = "password"
      //  coEvery { loginUserUseCase.execute(email, password) } throws FirebaseAuthUserCollisionException("User exists")

        // Observer for error state
        val errorObserver: Observer<String?> = spyk()
        viewModel.error.observeForever(errorObserver)

        // Observer for isLoading
        val isLoadingObserver: Observer<Boolean> = spyk()
        viewModel.isLoading.observeForever(isLoadingObserver)

        // When
        viewModel.loginUser(email, password)

        // Then
        verify { isLoadingObserver.onChanged(true) }
        verify { errorObserver.onChanged("This email is already registered.") }
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @Test
    fun `test loginUser when unexpected error occurs`() = runBlocking {
        // Given
        val email = "test@example.com"
        val password = "password"
        coEvery { loginUserUseCase.execute(email, password) } throws Exception("Unexpected error")

        // Observer for error state
        val errorObserver: Observer<String?> = spyk()
        viewModel.error.observeForever(errorObserver)

        // Observer for isLoading
        val isLoadingObserver: Observer<Boolean> = spyk()
        viewModel.isLoading.observeForever(isLoadingObserver)

        // When
        viewModel.loginUser(email, password)

        // Then
        verify { isLoadingObserver.onChanged(true) }
        verify { errorObserver.onChanged("Unexpected error") }
        assertFalse(viewModel.isLoading.value ?: true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        // Reset the dispatcher after the test
        Dispatchers.resetMain()
    }
}
