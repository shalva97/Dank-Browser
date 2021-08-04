package com.example.dankbrowser.gecko_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.dankbrowser.R
import com.example.dankbrowser.databinding.FragmentGeckoBinding
import com.example.dankbrowser.task_view.TaskViewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSessionSettings
import javax.inject.Inject

@AndroidEntryPoint
class GeckoFragment : Fragment(R.layout.fragment_gecko) {

    private val viewModel: GeckoViewModel by viewModels()

    @Inject
    lateinit var geckoRuntime: GeckoRuntime

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGeckoBinding.bind(view)

        val session1 = GeckoSession()

        session1.open(geckoRuntime)

        with(binding)
        {
            browserGV.setSession(session1)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.loadUrlAction.collect {
                    session1.loadUri(it.url)


//        val session2 = GeckoSession(
//            GeckoSessionSettings.Builder()
//                .contextId("asdf")
//                .build()
//        ).apply {
//            loadUri("http://youtube.com/")
//
//        }
//
//
                }
            }
        }

    }
}