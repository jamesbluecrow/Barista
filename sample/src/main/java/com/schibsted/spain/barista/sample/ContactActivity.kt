package com.schibsted.spain.barista.sample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_contact.*
import java.io.File

class ContactActivity : AppCompatActivity() {

    companion object {
        val PICK_CONTACT_REQUEST_CODE = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        pick_contact.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.Contacts.CONTENT_TYPE
            startActivityForResult(intent, PICK_CONTACT_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_CONTACT_REQUEST_CODE) {
            data?.let {
                try {
                    val uri = data.data
                    val cursor = contentResolver.query(uri, null, null, null, null)
                    if (cursor.moveToFirst()) {
                        val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

                        contact_name.text = cursor.getString(nameIndex)
                        contact_phone.text = cursor.getString(phoneIndex)
                    }
                    cursor.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getPictureUri(): Uri {
        val path = applicationContext.cacheDir.path + "/test.jpg"
        return FileProvider.getUriForFile(this,
                applicationContext.packageName + ".barista.sample.provider",
                File(path))
    }
}