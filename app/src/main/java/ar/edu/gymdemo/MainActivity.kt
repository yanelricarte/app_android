package ar.edu.gymdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.edu.gymdemo.databinding.ActivityMainBinding
import ar.edu.gymdemo.ui.AsistenciaFragment
import ar.edu.gymdemo.ui.DatosFragment

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Fragment inicial
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(b.fragmentContainer.id, DatosFragment())
                .commit()
        }

        b.bottomNav.setOnItemSelectedListener { item ->
            val frag = when (item.itemId) {
                R.id.nav_asistencia -> AsistenciaFragment()
                else -> DatosFragment()
            }
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(b.fragmentContainer.id, frag)
                .commit()
            true
        }
    }
}
