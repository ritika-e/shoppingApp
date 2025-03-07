import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shoppingapp.domain.usecase.RegisterUserUseCase
import com.example.shoppingapp.presentation.auth.SignupViewModel
import com.example.shoppingapp.domain.usecase.Result
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
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class SignupViewModelTest {

    private lateinit var viewModel: SignupViewModel
    private val signupUseCase: RegisterUserUseCase = mockk()

    // Rule to execute tasks synchronously on the main thread
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Setting up the test environment before each test
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        // Initialize the ViewModel with the mocked RegisterUserUseCase
        viewModel = SignupViewModel(signupUseCase)

        // Set the Main dispatcher to a Test dispatcher
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `test validateFields when name is blank`() {
        // Given
        viewModel.name = ""
        viewModel.email = "test@example.com"
        viewModel.password = "password"
        viewModel.confirmPassword = "password"

        // When
        val isValid = viewModel.validateFields()

        // Then
        assertFalse(isValid)
        assertEquals("Name is required", viewModel.errorMessage.value)
    }

    @Test
    fun `test validateFields when email is blank`() {
        // Given
        viewModel.name = "John"
        viewModel.email = ""
        viewModel.password = "password"
        viewModel.confirmPassword = "password"

        // When
        val isValid = viewModel.validateFields()

        // Then
        assertFalse(isValid)
        assertEquals("Email is required", viewModel.errorMessage.value)
    }

    @Test
    fun `test validateFields when passwords do not match`() {
        // Given
        viewModel.name = "John"
        viewModel.email = "test@example.com"
        viewModel.password = "password1"
        viewModel.confirmPassword = "password2"

        // When
        val isValid = viewModel.validateFields()

        // Then
        assertFalse(isValid)
        assertEquals("Passwords do not match", viewModel.errorMessage.value)
    }

    @Test
    fun `test signUp when fields are valid and signup is successful`() = runBlocking {
        // Given
        viewModel.name = "John"
        viewModel.email = "test@example.com"
        viewModel.password = "password"
        viewModel.confirmPassword = "password"
        coEvery { signupUseCase.execute(viewModel.name, viewModel.email, viewModel.password, viewModel.role) } returns Result.Success

        // Observer for LiveData
        val observer: Observer<Boolean> = spyk()
        viewModel.isSignedUp.observeForever(observer)

        // When
        viewModel.signUp()

        // Then
        assertTrue(viewModel.isSignedUp.value ?: false)
        verify { observer.onChanged(true) }
    }

    @Test
    fun `test signUp when signup fails`() = runBlocking {
        // Given
        viewModel.name = "John"
        viewModel.email = "test@example.com"
        viewModel.password = "password"
        viewModel.confirmPassword = "password"
        coEvery { signupUseCase.execute(viewModel.name, viewModel.email, viewModel.password, viewModel.role) } returns Result.Failure("Signup failed")

        // Observer for error message
        val errorObserver: Observer<String?> = spyk()
        viewModel.errorMessage.observeForever(errorObserver)

        // When
        viewModel.signUp()

        // Then
        assertEquals("Signup failed. Please try again.", viewModel.errorMessage.value)
        verify { errorObserver.onChanged("Signup failed. Please try again.") }
    }

    @Test
    fun `test signUp when name is blank`() {
        // Given
        viewModel.name = ""
        viewModel.email = "test@example.com"
        viewModel.password = "password"
        viewModel.confirmPassword = "password"

        // When
        viewModel.signUp()

        // Then
        assertEquals("Name is required", viewModel.errorMessage.value)
    }

    @Test
    fun `test signUp when email is blank`() {
        // Given
        viewModel.name = "John"
        viewModel.email = ""
        viewModel.password = "password"
        viewModel.confirmPassword = "password"

        // When
        viewModel.signUp()

        // Then
        assertEquals("Email is required", viewModel.errorMessage.value)
    }

    @Test
    fun `test signUp when password is blank`() {
        // Given
        viewModel.name = "John"
        viewModel.email = "test@example.com"
        viewModel.password = ""
        viewModel.confirmPassword = "password"

        // When
        viewModel.signUp()

        // Then
        assertEquals("Password is required", viewModel.errorMessage.value)
    }

    @Test
    fun `test signUp when confirm password is blank`() {
        // Given
        viewModel.name = "John"
        viewModel.email = "test@example.com"
        viewModel.password = "password"
        viewModel.confirmPassword = ""

        // When
        viewModel.signUp()

        // Then
        assertEquals("Confirm Password is required", viewModel.errorMessage.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        // Reset the dispatcher after the test
        Dispatchers.resetMain()
    }
}
