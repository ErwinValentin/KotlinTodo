package com.valentingonzalez.android.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TaskPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private var fragmentList = ArrayList<Fragment>()
    private var fragmentTitle = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle.get(position)
    }

    fun addItem(fragment: Fragment, title: String){
        fragmentList.add(fragment)
        fragmentTitle.add(title)
    }
}