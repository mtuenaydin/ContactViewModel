# ContactViewModel
An Android Library to access the local contact list via a simple ViewModel

# What you need
You need to add the following permission to your manifest

```xml
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

and add the following dependency to your gradle file
```javascript
repositories {
    maven {
        url  "https://dl.bintray.com/mtunaydin/android"
    }
}

dependencies {
  implementation 'com.thiedge.android:contact-viewmodel:0.6'
}
```
Note: You must migrate to AndroidX to use this library

This Libraray is using ViewModel and LiveData (Version 2.0.0 or higher is required)

```javascript
implementation "androidx.lifecycle:lifecycle-extensions:2.0.0"
```

# How to use it?
The easiest and quickest way to receive all contacts (with their default profiles)

```kotlin
// MainActivity#onCreate
val viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
var contacts: LiveData<List<Contact>> = viewModel.getContacts()
```
This will load the contacts in an asynchronous thread with the following projections
* CONTACT_ID
* DISPLAY_NAME_PRIMARY
* PHOTO_URI
* PHOTO_THUMBNAIL_URI

To change the default projection, you can parametrize the ```getContent``` method with your own projection

```kotlin
val myNewProjection: Array<String> = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
var contacts: LiveData<List<Contact>> = viewModel.getContacts(projection = myNewProjection)
```

```getContacts()``` will return ```LiveData<List<Contact>>``` so you can access the data when the data is loaded

```kotlin
viewModel.contacts.observe(this, Observer {
            myListAdapter.submitList(it)
        })
```

The Contact class contains a map which stores the profiles from the contact. The key is representing the MimeType and the value the column which stores the account data.

Example

```kotlin
val MAIL = Pair(
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Email.ADDRESS
        )
```

This Library supports per default the following profile types
* Number
* Mail
* WhatsApp
* Telegram
* Signal

you can easily extend this list by adding your own profile map

```kotlin
val myProfiles = mapOf(DefaultProfiles.NUMBER,
                        DefaultProfiles.MAIL,
                        Pair("vnd.android.cursor.item/vnd.org.telegram.messenger.android.profile", "data1"))

viewModel.setProfilesToReceive(myProfiles)
```
If you want to check if the contact has WhatsApp

```kotlin
//ContactAdapter#onBindViewHolder
val contact = getItem(position)
if (contact.hasWhatsApp()) {
  // do stuff
}
...
```
to check your custom profile types

```kotlin
if (contact.hasProfile("vnd.android.cursor.item/email_v2") {
  // do stuff
}
...
```

# Feedback
If you have any suggestion to make the library easier to user or extend the functionality feel free to open an issue or by directly making a pull request :)

Don't forget to :star: this repo if you liked the Library
