package com.schibsted.spain.barista.intents

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentProviderOperation
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.intent.Intents.intending
import com.schibsted.spain.barista.intents.BaristaCameraIntentMatchers.captureImage
import com.schibsted.spain.barista.intents.BaristaContactIntentMatchers.captureContact


object BaristaIntents {
    private val DEFAULT_SIZE = 100

    @JvmStatic
    @JvmOverloads
    fun mockAndroidCamera(width: Int = DEFAULT_SIZE, height: Int = DEFAULT_SIZE) {
        val result = createImageCaptureStub()
        intending(captureImage(width, height)).respondWith(result)
    }

    private fun createImageCaptureStub(): Instrumentation.ActivityResult {
        val resultBundle = Bundle()

        val resultData = Intent()
        resultData.putExtras(resultBundle)

        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    @JvmField
    val BARISTA_TEST_CONTACT_NAME = "Barista Test Contact"

    @JvmField
    val BARISTA_TEST_CONTACT_PHONE = "612677130"

    @JvmField
    val BARISTA_TEST_CONTACT_EMAIL = "barista@github.com"

    @JvmStatic
    @JvmOverloads
    fun mockContactIntent(name: String = BARISTA_TEST_CONTACT_NAME,
                          phone: String = BARISTA_TEST_CONTACT_PHONE,
                          address: String = BARISTA_TEST_CONTACT_EMAIL): List<Uri> {
        val uris = createContact(name, phone, address)
        val result = createContactPickStub(uris[0])
        intending(captureContact()).respondWith(result)
        return uris
    }

    fun createContactPickStub(uri: Uri): Instrumentation.ActivityResult {
        val resultData = Intent()
        resultData.data = uri

        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    fun createContact(name: String, number: String, address: String): List<Uri> {
        val contactOperationsList = ArrayList<ContentProviderOperation>()

        contactOperationsList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build())

        // NAME
        contactOperationsList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, name)
                .build())

        val arrayOfContentProviderResults = getInstrumentation().targetContext.contentResolver.applyBatch(ContactsContract.AUTHORITY, contactOperationsList)
        val uri = arrayOfContentProviderResults[0].uri


        val uris = arrayListOf(uri)

        // PHONES
        val phoneUris = addPhone(uri.lastPathSegment, number)

        // EMAILS
        val emailUris = addEmail(uri.lastPathSegment, address)

        uris.addAll(phoneUris)
        uris.addAll(emailUris)

        return uris
    }

    private fun addPhone(rawContactId: String, number: String): List<Uri> {
        val phoneTypes = arrayListOf<Int>()

        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_PAGER)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_CAR)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_ISDN)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_MAIN)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_RADIO)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_TELEX)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT)
        phoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_MMS)

        val phonesOperationsList = ArrayList<ContentProviderOperation>()
        phoneTypes.map {
            phonesOperationsList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, it)
                    .build())
        }
        return getInstrumentation().targetContext.contentResolver.applyBatch(ContactsContract.AUTHORITY, phonesOperationsList).map { it.uri }
    }

    private fun addEmail(rawContactId: String, address: String): List<Uri> {
        val emailTypes = arrayListOf<Int>()

        emailTypes.add(ContactsContract.CommonDataKinds.Email.TYPE_HOME)
        emailTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
        emailTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER)
        emailTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)

        val emailsOperationsList = ArrayList<ContentProviderOperation>()
        emailTypes.map {
            emailsOperationsList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, address)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, it)
                    .build())
        }
        return getInstrumentation().targetContext.contentResolver.applyBatch(ContactsContract.AUTHORITY, emailsOperationsList).map { it.uri }
    }

    @JvmStatic
    fun clearContactData(uris: List<Uri>) {
        uris.forEach {
            getInstrumentation().targetContext.contentResolver.delete(it, null, null)
        }
    }

    @JvmStatic
    fun clearContactData(uri: Uri) {
        getInstrumentation().targetContext.contentResolver.delete(uri, null, null)
    }

}
