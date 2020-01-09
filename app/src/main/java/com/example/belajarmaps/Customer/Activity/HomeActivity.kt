package com.example.belajarmaps.Customer.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.belajarmaps.Customer.Fragment.AccountFragment
import com.example.belajarmaps.Customer.Fragment.HomeFragment
import com.example.belajarmaps.Customer.Fragment.MyTicketFragment
import com.example.belajarmaps.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        loadFragment(HomeFragment())
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bn_main)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.home_menu -> fragment = HomeFragment()
            R.id.myticket_menu -> fragment = MyTicketFragment()
            R.id.account_menu -> fragment = AccountFragment()
        }
        return loadFragment(fragment)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val exit = Intent(Intent.ACTION_MAIN)
        exit.addCategory(Intent.CATEGORY_HOME)
        exit.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(exit)
        finish()
    }
}
