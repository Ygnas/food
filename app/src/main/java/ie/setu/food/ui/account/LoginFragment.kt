package ie.setu.food.ui.account

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import ie.setu.food.R
import ie.setu.food.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: AccountViewModel
    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(requireContext(), "LoggedIn successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        setButtonListener()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        viewModel.liveFirebaseUser.observe(this)
        { firebaseUser ->
            if (firebaseUser != null) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToNavHome())
            }
        }
    }

    private fun setButtonListener() {
        binding.buttonLogin.setOnClickListener {
            viewModel.login(
                binding.editTextUser.text.toString().lowercase(),
                binding.editTextPass.text.toString()
            )
            viewModel.errorStatus.observe(viewLifecycleOwner) {
                if (viewModel.errorStatus.value == true) {
                    binding.loginError.text = getString(R.string.register_error)
                } else {
                    binding.loginError.text = ""
                    Toast.makeText(
                        requireContext(),
                        "LoggedIn successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.googleLogin.setOnClickListener {
            signInIntent()
        }

        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun signInIntent() {
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.AppTheme)
            .setLogo(R.drawable.baseline_cookie_24)
            .build()

        signInLauncher.launch(signInIntent)
    }
}