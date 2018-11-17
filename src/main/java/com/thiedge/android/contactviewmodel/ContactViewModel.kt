package com.thiedge.android.contactviewmodel

import android.app.Application
import android.net.Uri
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.thiedge.android.contactviewmodel.model.Contact

/**
 *
 */
class ContactViewModel constructor(application: Application)
    : AndroidViewModel(application) {

    private var contactRepository = ContactRepository(application)

    var contacts: LiveData<List<Contact>> = contactRepository.contacts
        private set

    fun getContacts(uri: Uri = ContactsContract.Contacts.CONTENT_URI,
                    projection: Array<String>? = DEFAULT_CONTACT_PROJECTION,
                    selection: String? = null,
                    selectionArgs: Array<String>? = null,
                    sortOrder: String = ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC") {
        contactRepository.getContacts(uri, projection, selection, selectionArgs, sortOrder)
    }

    fun setProfilesToReceive(profiles: Map<String, String>){
        contactRepository.profilesToReceive = profiles
    }

    companion object {
        val DEFAULT_CONTACT_PROJECTION: Array<String> = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
        )
    }
}