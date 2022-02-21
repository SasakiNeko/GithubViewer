package com.sasaki.githubviewer.presentation.repositorysearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sasaki.githubviewer.R
import com.sasaki.githubviewer.databinding.FragmentRepositoryDetailBinding


class RepositoryDetailFragment : Fragment() {

    lateinit var binding: FragmentRepositoryDetailBinding

    private var repositoryInfo: SerializableRepositoryInfo? = null

    companion object {
        const val REPOSITORY_INFO = "REPOSITORY_INFO"

        @JvmStatic
        fun newInstance() = RepositoryDetailFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            repositoryInfo = it.getSerializable(REPOSITORY_INFO) as? SerializableRepositoryInfo
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositoryDetailBinding.inflate(inflater, container, false)

        setupBackButton()
        setData(repositoryInfo)
        return binding.root
    }

    private fun setData(repositoryInfo: SerializableRepositoryInfo?) {
        repositoryInfo?.apply {
            binding.repositoryNameWithOwner.text = nameWithOwner
            binding.url.text = url
            binding.stars.text = getString(R.string.stars, stargazerCount)
            binding.watching.text = getString(R.string.watching, watchingCount)
            binding.forks.text = getString(R.string.forks, forkCount)
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}