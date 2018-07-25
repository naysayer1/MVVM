package com.naysayer.iseeclinic.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.naysayer.iseeclinic.R
import com.naysayer.iseeclinic.fragments.tests.LasikTestFragment
import com.naysayer.iseeclinic.fragments.UserInfoFragment
import com.naysayer.iseeclinic.login.usual.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private lateinit var mUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mUser = FirebaseAuth.getInstance().currentUser!!

        val toggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            R.id.action_about -> return true
            R.id.action_log_out -> {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(LoginActivity.newIntent(this))
                    finish()
                }
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_account -> {
                replaceFragment(UserInfoFragment())
            }
            R.id.nav_tests -> {
                replaceFragment(LasikTestFragment())
            }
            R.id.nav_specialists -> {
            }
            R.id.nav_price -> {
            }
            R.id.nav_send_email -> {
                sendEmail()
            }
            R.id.nav_call -> {
                callUs()
            }
            R.id.nav_our_location -> {
                ourLocation()
            }
            R.id.nav_share -> {
            }
            R.id.nav_rate -> {
            }
            R.id.nav_settings -> {
            }
        }

        drawer_layout.closeDrawer(Gravity.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit()
    }


    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "plain/text"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("jon@example.com")) //TODO create email for app
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Вопрос")
        startActivity(emailIntent)
    }

    private fun callUs() {
        val number = Uri.parse("tel:88126599999")
        val callIntent = Intent(Intent.ACTION_DIAL, number)
        startActivity(callIntent)
    }

    private fun ourLocation() {
        val location = Uri.parse("geo:59.851416, 30.281929?q=59.851416, 30.281929" +
                "(Центр Микрохтрургии Глаза+\"Я Вижу!\")") //TODO посмотреть какие конкретно координаты указаны у нас, чтоб в картах открывалось страница с "я вижу!"
        val mapIntent = Intent(Intent.ACTION_VIEW, location)
        startActivity(mapIntent)
    }
}
