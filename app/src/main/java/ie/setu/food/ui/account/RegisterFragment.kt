package ie.setu.food.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ie.setu.food.databinding.FragmentRegisterBinding


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
            viewModel.register(
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
    }
}