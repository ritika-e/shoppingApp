import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shoppingapp.domain.usecase.ResetPasswordUseCase
import com.example.shoppingapp.presentation.auth.ForgetPasswordViewModel
import com.example.shoppingapp.presentation.auth.ForgetPasswordViewModel.ResetPasswordState
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

class ForgetPasswordViewModelTest {

    private lateinit var viewModel: ForgetPasswordViewModel
    private val resetPasswordUseCase: ResetPasswordUseCase = mockk()

    // Rule to execute tasks synchronously on the main thread
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Setting up the test environment before each test
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        // Initialize the ViewModel with the mocked ResetPasswordUseCase
        viewModel = ForgetPasswordViewModel(resetPasswordUseCase)

        // Set the Main dispatcher to a Test dispatcher
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `test sendPasswordResetEmail when email is blank`() {
        // Given
        val email = ""

        // When
        viewModel.sendPasswordResetEmail(email)

        // Then
        val state = viewModel.resetPasswordState.value
        assert(state is ResetPasswordState.Error)
        assertEquals("Email cannot be empty", (state as ResetPasswordState.Error).message)
    }

    @Test
    fun `test sendPasswordResetEmail when reset password is successful`() = runBlocking {
        // Given
        val email = "test@example.com"
        coEvery { resetPasswordUseCase.execute(email) } returns Unit // Mock successful execution

        // Observer for LiveData
        val observer: Observer<ResetPasswordState> = spyk()
        viewModel.resetPasswordState.observeForever(observer)

        // When
        viewModel.sendPasswordResetEmail(email)

        // Then
        val state = viewModel.resetPasswordState.value
        assert(state is ResetPasswordState.Success)
        assertEquals("Password reset email sent successfully", (state as ResetPasswordState.Success).message)
        verify { observer.onChanged(any()) }
    }

    @Test
    fun `test sendPasswordResetEmail when reset password fails`() = runBlocking {
        // Given
        val email = "test@example.com"
        val errorMessage = "Network error"
        coEvery { resetPasswordUseCase.execute(email) } throws Exception(errorMessage) // Mock failure

        // Observer for LiveData
        val observer: Observer<ResetPasswordState> = spyk()
        viewModel.resetPasswordState.observeForever(observer)

        // When
        viewModel.sendPasswordResetEmail(email)

        // Then
        val state = viewModel.resetPasswordState.value
        assert(state is ResetPasswordState.Error)
        assertEquals("Error: $errorMessage", (state as ResetPasswordState.Error).message)
        verify { observer.onChanged(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        // Reset the dispatcher after the test
        Dispatchers.resetMain()
    }
}
