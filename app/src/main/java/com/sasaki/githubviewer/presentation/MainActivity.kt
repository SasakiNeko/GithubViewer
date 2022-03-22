package com.sasaki.githubviewer.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sasaki.githubviewer.R
import com.sasaki.githubviewer.databinding.ActivityMainBinding
import com.sasaki.githubviewer.presentation.issuesearch.IssueDetailFragment
import com.sasaki.githubviewer.presentation.issuesearch.IssueSearchFragment
import com.sasaki.githubviewer.presentation.repositorysearch.RepositoryDetailFragment
import com.sasaki.githubviewer.presentation.repositorysearch.RepositorySearchFragment
import com.sasaki.githubviewer.presentation.repositorysearch.SerializableRepositoryInfo

class MainActivity : AppCompatActivity(), RepositorySearchFragment.OnRepositoryItemClickListener,
    IssueSearchFragment.OnIssueItemClickListener {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addFragment(createRepositorySearchFragment())
        setupBottomView()
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

    private fun setupBottomView() {

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_repository_search -> {
                    replaceFragment(RepositorySearchFragment.newInstance())
                    true
                }
                R.id.action_source_view -> true
                R.id.action_issue_view -> {
                    replaceFragment(IssueSearchFragment.newInstance())
                    true
                }
                R.id.action_notification_list -> true
                else -> true
            }
        }
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

    override fun notifyIssueItemClick(ownerName: String, repositoryName: String, issueNumber: Int) {
        val issueDetailFragment = IssueDetailFragment.newInstance().apply {
            arguments = Bundle().apply {
                putString(IssueDetailFragment.OWNER_NAME, ownerName)
                putString(IssueDetailFragment.REPOSITORY_NAME, repositoryName)
                putInt(
                    IssueDetailFragment.ISSUE_NUMBER,
                    issueNumber
                )
            }
        }
        replaceFragment(issueDetailFragment)
    }
}