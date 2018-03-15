package ca.androidyvr.demos.kotlin

import android.net.Uri
import android.provider.ContactsContract
import ca.androidyvr.demos.kotlin.model.Person
import com.airg.android.logging.Logger
import com.airg.android.logging.TaggedLogger
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.util.*
import java.util.concurrent.Callable

/**
 * Created by MahramF.
 */

// Yeah, it's that easy!
object Singleton {
    private val LOG: TaggedLogger = Logger.tag("SINGLETON")
    private val RND = Random(System.currentTimeMillis())

    private val STRING_RESOURCE_IDS = arrayOf(
            R.string.dontClickMe,
            R.string.containYourself,
            R.string.stopIt,
            R.string.goAway
    )

    val stringResources: Array<CharSequence>

    init {
        val context = KotlinDemoApplication.instance()

        stringResources = Array(STRING_RESOURCE_IDS.size,
                { i -> context.getString(STRING_RESOURCE_IDS[i]) })

        LOG.d("""Singleton initialized with ${stringResources.size} warning strings.""")
    }

    fun getRandomWarning(): CharSequence = stringResources[RND.nextInt(stringResources.size)]

    fun getWarning(idx: Int): CharSequence = stringResources[idx]

    fun loadContacts(): Single<List<Person>>? = Single.fromCallable {
        val list = arrayListOf<Person>()

        KotlinDemoApplication.instance()
                .contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                        ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI),
                null, null, null).use {
            if (!it.moveToFirst()) return@use

            val idxName = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val idxNumber = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)
            val idxPhoto = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)

            do {
                val person = Person(name = it.getString(idxName),
                        number = it.getString(idxNumber),
                        image = it.getString(idxPhoto))
                LOG.d("""Contact: $person""")
                list.add(person)
            } while (it.moveToNext())
        }

        list.toList()
    }

}
