package com.thiedge.android.contactviewmodel.model

import android.provider.ContactsContract


/**
 *  contains some default profiles the contact might have
 *  key (first) = MimeType
 *  value (second) = columnName
 */
class DefaultProfiles {
    companion object {
        /**
         *  WhatsApp, Signal an Telegram are using Data1 to reach the user directly
         *  Data2 is representing the apps name and data3 is some sort of action text so
         *  the user knows what action can be done with data1 (for example Message +49 XXX XXXX)
         *  data4 was only used by telegram and the information was the same as in data1 and may represent
         *  the primary user id (if the user has more then one number)
         */
        val WHATSAPP = Pair(
            "vnd.android.cursor.item/vnd.com.whatsapp.profile",
            "data1"
        )
        val TELEGRAM = Pair(
            "vnd.android.cursor.item/vnd.org.telegram.messenger.android.profile",
            "data1"
        )
        val SIGNAL = Pair(
            "vnd.android.cursor.item/vnd.org.thoughtcrime.securesms.contact",
            "data1"
        )
        val MAIL = Pair(
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Email.ADDRESS
        )
        val NUMBER = Pair(
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val profiles = mapOf(WHATSAPP, TELEGRAM, SIGNAL, MAIL, NUMBER)
    }
}