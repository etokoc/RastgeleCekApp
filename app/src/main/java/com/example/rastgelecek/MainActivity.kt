package com.example.rastgelecek

import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
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

        //Listview'ı doldurma
        getListData()
        if (getListData() != null) {
            val list = getListData()
            for (x in list!!) {
                listViewFit()
            }
        }
        //Satır tıklama olayı
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            ShowMessage(
                "Uyarı",
                " ''" + liste.get(position) + "'' Değerini Silmek İstediğine Emin Misin ?",
                liste.get(position),
                "satir"
            )
        })

        //Tüm veriyi silme butonu
        button_dataAllRemove.setOnClickListener {
            ShowMessage("Uyarı", "Tüm veriyi siliyosun bak dikkat et")
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
                ,"kura","kura")
                ShowMessage("Uyarı-1", "Faruk ben Ertuğrul boş girme bak unutkanlık var herhalde","kura","kura")
            }

        }

        kuraCekButton.setOnClickListener {
            if (liste.size > 0) {
                val index = random.nextInt(liste.size)
                ShowMessage("Kura Sonucu", "Sonuç: " + liste.get(index),"veri","kura")
            } else {
                ShowMessage("Uyarıı", "Neyin kurasını çekcen bişey yok ki kaydetmemişsin hiç!!","veri","kura")
            }
        }
    }

    private fun ShowMessage(title: String, message: String) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("Tamam abi") { dialogInterface: DialogInterface, i: Int ->
            deleteAllData()
        }
        alert.setNegativeButton("Silme abi") { dialogInterface: DialogInterface, i: Int ->
        }
        alert.show()
    }

    private fun ShowMessage(title: String, message: String, veri: String, type: String) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(title)
        alert.setMessage(message)
        if (type == "satir") {
            alert.setPositiveButton("Tamam abi") { dialogInterface: DialogInterface, i: Int ->
                deleteRow(veri)
            }
            alert.setNegativeButton("Silme abi") { dialogInterface: DialogInterface, i: Int ->
            }

        } else if (type == "kura") {
            alert.setPositiveButton("Tamam abi") { dialogInterface: DialogInterface, i: Int ->
            }
        }
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

    private fun deleteRow(veri: String) {
        liste.remove(veri)
        listViewFit()
        deleteAllData(veri)
        setListData(liste)

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

    private fun deleteAllData(veri: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit().remove(KEY).apply()
        Toast.makeText(this, "" + veri + " Başarıyla Silindi", Toast.LENGTH_SHORT).show()
        listViewFit()
    }

    //gson-list cevirme icin gerekli
    inline fun <reified T> Gson.fromJson(json: String) =
        fromJson<T>(json, object : TypeToken<T>() {}.type)

}

