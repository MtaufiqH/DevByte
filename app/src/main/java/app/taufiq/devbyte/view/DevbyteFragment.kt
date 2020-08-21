package app.taufiq.devbyte.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.taufiq.devbyte.R
import app.taufiq.devbyte.adapter.DevbyteAdapter
import app.taufiq.devbyte.adapter.VideoClick
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


    private var viewModelAdapter: DevbyteAdapter? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner, Observer<List<DevbyteVideos>> {
            it.apply {
                viewModelAdapter?.videos = it
            }
        })

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


        // set lifecycle owner so databinding can observe Livedatao
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewmodel = viewModel

        viewModelAdapter = DevbyteAdapter(VideoClick {
            val packageManager = context?.packageManager ?: return@VideoClick

            // try to generate a direct intent into youtube
            var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
            if (intent.resolveActivity(packageManager) == null) {
                // YouTube app isn't found, use the web url
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            }

            startActivity(intent)

        })


        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }


        viewModel.eventNetworkError.observe(
            viewLifecycleOwner,
            Observer<Boolean> { isNetworkError ->
                if (isNetworkError) onNetworkError()
            })


        return binding.root
    }


    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "NetworkError", Toast.LENGTH_SHORT).show()
            viewModel.onNetworkErrorShown()
        }
    }


    private val DevbyteVideos.launchUri: Uri
        get() {
            val httpUri = Uri.parse(url)
            return Uri.parse("vnd.youtube:${httpUri.getQueryParameter("v")}")
        }


}