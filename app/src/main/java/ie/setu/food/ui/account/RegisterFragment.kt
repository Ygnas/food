package ie.setu.food.ui.account

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ie.setu.food.R
import ie.setu.food.databinding.FragmentRegisterBinding
import ie.setu.food.ui.foodlist.FoodListFragmentDirections


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        setButtonListener()
        return binding.root
    }

    private fun setButtonListener() {
        binding.buttonRegisterAccount.setOnClickListener {
            val username = binding.editTextUser.text.toString()
            val password = binding.editTextPass.text.toString()
            if (username.isEmpty() && password.isEmpty()) {
                binding.loginError.text = getString(R.string.register_error_empty)
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                binding.loginError.text = getString(R.string.register_error_invalid_email)
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.loginError.text = getString(R.string.register_error_short)
                return@setOnClickListener
            }
            if (password != binding.editTextRepeatPass.text.toString()) {
                binding.loginError.text = getString(R.string.register_error_password)
                return@setOnClickListener
            }
            viewModel.register(
                binding.editTextUser.text.toString().lowercase(),
                binding.editTextPass.text.toString()
            )
            viewModel.errorStatus.observe(viewLifecycleOwner) {
                if (viewModel.errorStatus.value == true) {
                    binding.loginError.text = getString(R.string.register_error)
                } else {
                    binding.loginError.text = ""
                    Toast.makeText(requireContext(),
                        "Registered successfully",
                        Toast.LENGTH_SHORT).show()
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToNavHome())
                }
            }
        }
    }
}