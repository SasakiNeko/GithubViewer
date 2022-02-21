package com.sasaki.githubviewer.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sasaki.githubviewer.databinding.ActivityMainBinding
import com.sasaki.githubviewer.presentation.repositorysearch.RepositoryDetailFragment
import com.sasaki.githubviewer.presentation.repositorysearch.RepositorySearchFragment
import com.sasaki.githubviewer.presentation.repositorysearch.SerializableRepositoryInfo

class MainActivity : AppCompatActivity(), RepositorySearchFragment.OnRepositoryItemClickListener {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addFragment(createRepositorySearchFragment())
    }

    private fun createRepositorySearchFragment(): RepositorySearchFragment {
        return RepositorySearchFragment.newInstance()
    }

    private fun addFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // BackStackを設定
        fragmentTransaction.add(binding.container.id, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // BackStackを設定
        fragmentTransaction.replace(binding.container.id, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun notifyRepositoryItemClick(repositoryInfo: SerializableRepositoryInfo) {
        val repositoryDetailFragment = RepositoryDetailFragment.newInstance().apply {
            arguments = Bundle().apply {
                putSerializable(
                    RepositoryDetailFragment.REPOSITORY_INFO,
                    repositoryInfo
                )
            }
        }
        replaceFragment(repositoryDetailFragment)
    }
}