# ContactViewModel
An Android Library to access the local contact list via a simple ViewModel

# What you need
You need to add the following permission to your manifest

```xml
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

and add the following dependency to your gradle file
```javascript
dependencies {
  
  implementation "androidx.lifecycle:lifecycle-extensions:2.0.0+"
  kapt "androidx.lifecycle:lifecycle-compiler:2.0.0+"
}
```

This Libraray is using LiveData (Version 2.0.0 or higher is required)

```javascript
implementation "androidx.lifecycle:lifecycle-extensions:2.0.0"
```


# How to use it?
The easiest and quickest way to get all contact names can be achieved as followed:

```kotlin
// MainActivity#onCreate
val viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
var contacts: LiveData<List<Contact>> = viewModel.getContacts() 
```

If you the default columns aren’t enough for you, you can change the default projection:

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

The Contact class contains a map which stores the profiles from the contact, where the key is the MimeType and the value the column which stores the account. 

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

Don't forget to <3 if you liked the Library
