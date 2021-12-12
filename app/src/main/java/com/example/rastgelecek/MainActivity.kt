package com.example.rastgelecek

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        var liste: ArrayList<String> = ArrayList()
    }

    lateinit var listView: ListView
    lateinit var editText: EditText
    lateinit var button_dataAllRemove: TextView
    val KEY = "key"
    var random: Random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById<ListView>(R.id.listView)
        editText = findViewById<EditText>(R.id.editText)
        button_dataAllRemove = findViewById(R.id.textView_dataAll)

        //Tüm veriyi silme butonu
        button_dataAllRemove.setOnClickListener {
            ShowMessage("Uyarı", "Tüm veriyi siliyosun bak dikkat et")
            deleteAllData()
        }

        getListData()
        if (getListData() == null) {
            Toast.makeText(this, "Boş liste", Toast.LENGTH_SHORT).show()
        } else {
            val list = getListData()
            for (x in list!!) {
                listViewFit()
            }
        }
        kaydetButton.setOnClickListener {
            val yazi: String = editText.text.toString().trim()
            if (yazi != "") {
                liste.add(yazi)
                listViewFit()
            } else {
                ShowMessage(
                    "Tekrar Uyarıyommm",
                    "Faruk ben Akif bida boş veri girip beni Ertuğrula rezil etme rafık :)"
                )
                ShowMessage("Uyarı-1", "Faruk ben Ertuğrul boş girme bak unutkanlık var herhalde")
            }

        }

        kuraCekButton.setOnClickListener {
            if (liste.size > 0) {
                val index = random.nextInt(liste.size)
                ShowMessage("Kura Sonucu", "Sonuç: " + liste.get(index))
            } else {
                ShowMessage("Uyarıı", "Neyin kurasını çekcen bişey yok ki kaydetmemişsin hiç!!")
            }
        }
    }

    private fun ShowMessage(title: String, message: String) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("Tamam abi", null)
        alert.show()
    }

    override fun onPause() {
        super.onPause()
        setListData(liste)
    }

    private fun setListData(list: ArrayList<String>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(KEY, json)
        editor.apply()
    }


    private fun getListData(): ArrayList<String>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        val gson = Gson()
        val json = prefs.getString(KEY, null)
        val turnsType = object : TypeToken<ArrayList<String>>() {}.type
        val turns = Gson().fromJson<ArrayList<String>>(json, turnsType)
        if (turns != null)
            liste = turns
        return turns
    }

    private fun listViewFit() {
        val adapter = ArrayAdapter<String>(
            this, R.layout.support_simple_spinner_dropdown_item,
            liste
        )
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun deleteAllData() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().remove(KEY).apply()
        Toast.makeText(this, "Tüm Veriler Başarıyla Silindi!", Toast.LENGTH_SHORT).show()
        liste.clear()
        listViewFit()
    }

    //gson-list cevirme icin gerekli
    inline fun <reified T> Gson.fromJson(json: String) =
        fromJson<T>(json, object : TypeToken<T>() {}.type)

}

