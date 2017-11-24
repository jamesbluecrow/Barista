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

    @JvmStatic
    fun mockContactIntent(name: String) {
        val result = createContactPickStub(createContact(name))
        intending(captureContact()).respondWith(result)
    }

    private fun createContactPickStub(uri: Uri): Instrumentation.ActivityResult {
        val resultData = Intent()
        resultData.data = uri

        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun createContact(name: String): Uri {

        val operationList = ArrayList<ContentProviderOperation>()
        operationList.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build())

        operationList.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, name)
                .build())

        return getInstrumentation().targetContext.contentResolver.applyBatch(ContactsContract.AUTHORITY, operationList)[0].uri
    }

}
