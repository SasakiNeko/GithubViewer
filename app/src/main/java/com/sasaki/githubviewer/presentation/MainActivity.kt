package com.sasaki.githubviewer.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingComponent
import com.sasaki.githubviewer.R
import com.sasaki.githubviewer.databinding.ActivityMainBinding
import com.sasaki.githubviewer.presentation.repositorysearch.RepositorySearchFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repositorySearchFragment = createRepositorySearchFragment()
    }

    private fun createRepositorySearchFragment(): RepositorySearchFragment {
        return RepositorySearchFragment.newInstance()
    }
}