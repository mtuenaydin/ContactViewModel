package com.thiedge.android.contactviewmodel

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import com.thiedge.android.contactviewmodel.model.Contact
import com.thiedge.android.contactviewmodel.model.DefaultProfiles
import java.lang.Exception

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 *  representation from a local contact repository
 *  use this class if you want to write your own ViewModel
 *  otherwise use ContactViewModel
 */
class ContactRepository constructor(private var context: Context) {

    val contacts: MutableLiveData<List<Contact>> = MutableLiveData()
    private var executor: Executor
    private var defaultDataColumnName: String
    var profilesToReceive: Map<String, String>

    constructor(context: Context, executor: Executor, profilesToRetrieve: Map<String, String>, defaultDataColumnName : String = "data1") : this(context) {
        this.executor = executor
        this.profilesToReceive = profilesToRetrieve
        this.defaultDataColumnName = defaultDataColumnName
    }

    init {
        contacts.value = mutableListOf()
        executor  = Executors.newSingleThreadExecutor()
        profilesToReceive =  DefaultProfiles.profiles
        defaultDataColumnName  = "data1"
    }


    // query the contact
    fun getContacts(uri: Uri, projection: Array<String>?,
                    selection: String?, selectionArgs: Array<String>?, sortOrder: String){
        executor.execute {
            val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)

            cursor?.let {
                val list: MutableList<Contact> = mutableListOf()
                while (it.moveToNext()){
                    val contact = Contact()
                    contact.id = it.getLong(it.getColumnIndex(ContactsContract.Contacts._ID))
                    contact.name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    contact.photoUri = retrievePhoto(it.getString(
                        it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)))
                    contact.thumbnailUri = retrievePhoto(it.getString(
                        it.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)))

                    list.add(contact)
                }

                // fast loading for recyclerViews
                contacts.postValue(list)

                for (contact in list){
                    contact.profiles = retrieveProfiles(contact.id.toString())
                    contact.numbers = retrieveNumbers(contact.id.toString())
                }

                contacts.postValue(list)
            }

            cursor?.close()
        }
    }

    private fun retrievePhoto(photoUri: String?) = photoUri?.let { Uri.parse(it) }


    private fun retrieveNumbers(contactId: String): MutableList<String> {
        val phoneCursor = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(contactId),
            null)

        val numbers = mutableListOf<String>()

        while (phoneCursor != null && phoneCursor.moveToNext()){
            val number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            numbers.add(number)
        }

        phoneCursor?.close()
        return numbers
    }

    private fun retrieveProfiles(contactId: String) : MutableMap<String, String> {
        val profiles = mutableMapOf<String, String>()
        val projection = arrayOf(ContactsContract.Data.MIMETYPE) + profilesToReceive.values.distinct()
        val selection = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " IN ( " + placeholder(profilesToReceive.size) + ") "
        val selectionArgs = arrayOf(contactId) + profilesToReceive.keys
        val cursor = context.contentResolver.query(ContactsContract.Data.CONTENT_URI,
            projection, selection, selectionArgs, null)

        while (cursor != null && cursor.moveToNext()) {
            val type = cursor.getString(0)
            val index = cursor.getColumnIndex( if (profilesToReceive.containsKey(type))
                profilesToReceive[type]!! else defaultDataColumnName)

            if (index == -1)
                throw Exception("Couldn't find $type column in CONTENT_URI.")

            val data = cursor.getString(index)

            profiles[type] = data
        }

        cursor?.close()
        return profiles
    }

    private fun placeholder(count: Int): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("?")
        repeat(count-1) {
            stringBuilder.append(",?")
        }
        return stringBuilder.toString()
    }
}