package app.taufiq.devbyte.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.taufiq.devbyte.R
import app.taufiq.devbyte.databinding.FragmentDevbyteBinding
import app.taufiq.devbyte.domain.DevbyteVideos
import app.taufiq.devbyte.viewmodels.DevbytesViewmodel


class DevbyteFragment : Fragment() {

    /**
     * one way to delay creation of the viewmodel until an appropriate lifecycle method is to use
     * lazy.This requires that viewmodel not be referenced before onActivityCreated, which we
     * do in this fragment
     * */
    private val viewModel: DevbytesViewmodel by lazy {
        val activity = requireNotNull(this.activity) {
            " You can only access the viewmodel after onCreateActivity"
        }

        @Suppress("DEPRECATION")
        ViewModelProviders.of(this, DevbytesViewmodel.Factory(activity.application))
            .get(DevbytesViewmodel::class.java)

    }


    private var viewModelAdapter: DevByteAdapter? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner, Observer<List<DevbyteVideos>> {
            it.apply {
                viewModelAdapter?.videos = videos
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentDevbyteBinding>(
            inflater,
            R.layout.fragment_devbyte,
            container,
            false
        )




        // set lifecycle owner so databinding can observe Livedata
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }


}