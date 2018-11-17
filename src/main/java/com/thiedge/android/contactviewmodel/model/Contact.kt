package com.thiedge.android.contactviewmodel.model

import android.net.Uri

/**
 * contact representation
 */
class Contact {
    var id: Long = 0L // CONTACT_ID
    var name: String = String() // display_name
    var numbers: MutableList<String> = mutableListOf()
    var profiles = mutableMapOf<String, String>()
    var photoUri: Uri? = null
    var thumbnailUri: Uri? = null

    fun hasProfile(type:String): Boolean = profiles.containsKey(type)
    fun hasWhatsApp(): Boolean = profiles.containsKey(DefaultProfiles.WHATSAPP.first)
    fun hasTelegram(): Boolean = profiles.containsKey(DefaultProfiles.TELEGRAM.first)
    fun hasSignal(): Boolean = profiles.containsKey(DefaultProfiles.SIGNAL.first)
    fun hasMail(): Boolean = profiles.containsKey(DefaultProfiles.MAIL.first)
    fun hasNumber(): Boolean = profiles.containsKey(DefaultProfiles.NUMBER.first)
}