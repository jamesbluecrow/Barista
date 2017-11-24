package com.schibsted.spain.barista.rule

import com.schibsted.spain.barista.intents.BaristaIntents
import com.schibsted.spain.barista.intents.BaristaIntents.clearContactData
import com.schibsted.spain.barista.intents.BaristaIntents.mockContactIntent
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ContactMockRule @JvmOverloads constructor(val name: String = BaristaIntents.BARISTA_TEST_CONTACT_NAME,
                                                val phone: String = BaristaIntents.BARISTA_TEST_CONTACT_PHONE,
                                                val address: String = BaristaIntents.BARISTA_TEST_CONTACT_EMAIL) : TestRule {
    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                val uris = mockContactIntent(name, phone, address)

                try {
                    base.evaluate()
                } finally {
                    clearContactData(uris)
                }
            }
        }
    }
}