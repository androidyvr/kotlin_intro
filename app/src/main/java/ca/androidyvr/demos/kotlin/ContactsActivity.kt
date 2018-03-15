package ca.androidyvr.demos.kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ca.androidyvr.demos.kotlin.model.Person
import com.airg.android.logging.Logger
import com.bumptech.glide.Glide
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : AppCompatActivity() {

    private val LOG = Logger.tag("CONTACTS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        list.adapter = ContactsAdapter()
    }

    private class PersonItem(view: View) : RecyclerView.ViewHolder(view) {

        val image: ImageView
        val name: TextView
        val number: TextView

        init {
            image = view.findViewById(R.id.image)
            name = view.findViewById(R.id.name)
            number = view.findViewById(R.id.number)
        }
    }

    private inner class ContactsAdapter : RecyclerView.Adapter<PersonItem>(), SingleObserver<List<Person>> {
        val inflater: LayoutInflater = LayoutInflater.from(this@ContactsActivity)
        val people: MutableList<Person> = mutableListOf()

        init {
            Singleton.loadContacts()!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonItem =
                PersonItem(inflater.inflate(R.layout.item_person, parent, false))

        override fun getItemCount(): Int = people.size

        override fun onBindViewHolder(holder: PersonItem, position: Int) {
            val person = people[position]
            holder.name.text = person.name
            holder.number.text = person.number
            holder.image.setImageBitmap(null)

            if (null == person.image)
                return

            Glide.with(this@ContactsActivity)
                    .load(person.image)
                    .into(holder.image)
        }

        override fun onSuccess(list: List<Person>) {
            LOG.d("""Received ${list.size} contacts""")
            title = getString(R.string.nContacts, list.size)
            people.clear()
            people.addAll(list)
            notifyDataSetChanged()
        }

        override fun onSubscribe(d: Disposable) {
            LOG.d("Loading contacts...")
        }

        override fun onError(e: Throwable) {
            LOG.e(e, "Error loading contacts")
        }
    }
}
