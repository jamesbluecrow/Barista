package com.schibsted.spain.barista.sample

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_contact.*


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
                        val nameIndex = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)
                        contact_name.text = cursor.getString(nameIndex)

                        val idIndex = cursor.getColumnIndex(ContactsContract.Data._ID)
                        contact_phone.text = getPhoneNumber(cursor.getString(idIndex))
                    }
                    cursor.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getPhoneNumber(id: String): String? {
        val people = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.Data.RAW_CONTACT_ID + "= " + id, null, null)

        if (people.moveToFirst()) {
            val number = people.getString(people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            people.close()
            return number
        }

        return null
    }
}