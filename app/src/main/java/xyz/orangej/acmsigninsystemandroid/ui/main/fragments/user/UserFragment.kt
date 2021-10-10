package xyz.orangej.acmsigninsystemandroid.ui.main.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment

class UserFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            Column {
                Text(text = "Hello")
                Button(onClick = {
                    Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show()
                }) {
                    Text(text = "Hello")
                }
            }
        }
    }
}