package com.example.dankbrowser

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.dankbrowser.data.TabEntity
import com.example.dankbrowser.data.TaskEntity
import io.realm.Realm
import io.realm.RealmConfiguration
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val realmName: String = "testing"
    private val config: RealmConfiguration
    private val realm: Realm


    init {
        Realm.init(appContext)
        config = RealmConfiguration.Builder().name(realmName).deleteRealmIfMigrationNeeded().build()
        realm = Realm.getInstance(config)
    }

    @Test
    fun uselessDatabaseTest() {
        val tab = TabEntity(
            "youtube", "default", "httttp"
        )

        realm.executeTransaction {
            it.insert(tab)
        }

        val results = realm.where(TabEntity::class.java).findAll()

        assert(results.first()?.url == "youtube")
        realm.executeTransaction { it.deleteAll() }
    }


    @Test
    fun saveListToDatabase() {
        val task = TaskEntity("asdf", "default").apply {
            tabs.add(TabEntity("youtube", "default", "httttp"))
            tabs.add(TabEntity("facebook", "default", "httttp"))
        }

        realm.executeTransaction {
            it.insert(task)
        }

        val result = realm.where(TaskEntity::class.java).findAll()

        assert(result.first()?.tabs?.size == 2)

        realm.executeTransaction {
            it.deleteAll()
        }

    }

}