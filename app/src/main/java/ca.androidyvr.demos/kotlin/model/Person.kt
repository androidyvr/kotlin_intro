@file:Suppress("unused", "MemberVisibilityCanPrivate", "JoinDeclarationAndAssignment", "UNUSED_VALUE", "LiftReturnOrAssignment", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNUSED_VARIABLE")

package ca.androidyvr.demos.kotlin.model

import android.net.Uri
import java.util.*

/**
 * Created by MahramF.
 */


// primary constructor built-in to the class definition
class Person(val name: String, val number: String? = null, var address: String? = null,
             var image: Uri? = null) {

    // alternate constructor has to call primary
    constructor(name: String, number: String?, image: Uri?)
            : this(name = name,
            image = image,
            number = number,
            address = null)

    constructor(name: String, number: String?, image: String?)
            : this(name, number,
            if (null == image) null else Uri.parse(image))

    // isn't this cool?
    val nameAndNumber: String
        get() = """$name $number"""

    override fun toString(): String {
        return nameAndNumber
    }

    // Thank you! Thank you very much!
    fun addressOrDefault(default: String): String = address ?: default
}