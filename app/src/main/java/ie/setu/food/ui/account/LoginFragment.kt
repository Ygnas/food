package ie.setu.food.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ie.setu.food.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: AccountViewModel

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
                    binding.loginError.setText("Error")
                } else {
                    binding.loginError.setText("")
                }
            }
        }

        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }
}