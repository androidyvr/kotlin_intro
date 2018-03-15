@file:Suppress("UseExpressionBody", "UNUSED_PARAMETER", "unused", "UNUSED_VARIABLE", "ConstantConditionIf", "UNUSED_VALUE")

package ca.androidyvr.demos.kotlin


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.airg.android.logging.Logger
import com.airg.android.permission.PermissionHandlerClient
import com.airg.android.permission.PermissionsHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val LOG = Logger.tag("MAIN")
    private var permissionDialog: AlertDialog? = null

    private lateinit var permissionHandler: PermissionsHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textOne.setText(R.string.activityCreated)
        buttonOne.setOnClickListener { buttonOneClicked() }
        inputOne.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                copyCat.text = p0
            }
        })

        showContacts.setOnClickListener { this.showContacts() }
    }

    private fun showContacts() {
        permissionHandler = PermissionsHandler.with(this, object : PermissionHandlerClient {
            override fun showPermissionRationaleDialog(requestCode: Int, permissions: MutableCollection<String>, listener: DialogInterface.OnClickListener): AlertDialog {
                permissionDialog = AlertDialog.Builder(this@MainActivity)
                        .setTitle(R.string.contactsPermission)
                        .setMessage(R.string.contactsPermissionRationale)
                        .setPositiveButton(android.R.string.ok, listener)
                        .setNegativeButton(android.R.string.cancel, listener)
                        .show()
                return permissionDialog!!
            }

            override fun onPermissionRationaleDialogDimissed(requestCode: Int) {
                permissionDialog = null
            }

            override fun onPermissionsGranted(requestCode: Int, granted: MutableSet<String>?) {
                LOG.d("${granted!!.size} permissions granted")
                startActivity(Intent(this@MainActivity, ContactsActivity::class.java))
            }

            override fun onPermissionDeclined(requestCode: Int, denied: MutableSet<String>?) {
                Toast.makeText(this@MainActivity, R.string.permissionDenied, Toast.LENGTH_SHORT).show()
            }
        })

        permissionHandler.check(REQUEST_READ_CONTACTS, android.Manifest.permission.READ_CONTACTS)
    }

    override fun onPause() {
        permissionDialog?.dismiss()
        permissionDialog = null
        super.onPause()
    }

    fun buttonOneClicked() {
        LOG.d("Don't click it!")
        Toast.makeText(this, Singleton.getRandomWarning(), Toast.LENGTH_SHORT).show()
    }

    open class SimpleTextWatcher : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    }

    companion object {
        private val REQUEST_READ_CONTACTS = 100
    }
}
