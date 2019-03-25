package dev.maturano.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dev.maturano.agenda.activity.ContatoActivity;
import dev.maturano.agenda.adapter.ContactListAdapter;
import dev.maturano.agenda.dao.ContactDAO;
import dev.maturano.agenda.model.Contact;

public class MainActivity extends AppCompatActivity {
    private Contact contact;
    private ContactDAO contactDAO;
    private List<Contact> contactList = new ArrayList<Contact>();
    private RecyclerView recyclerView;
    private ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(getApplicationContext(), ContatoActivity.class));
            }
        });

/*
        Contact c = new Contact("Leonardo", "leonardo@maturano.dev", "+552730035412");
        new ContactDAO(this).insertContact( c );
*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.contactDAO = new ContactDAO(getApplicationContext());
        this.contactList =  this.contactDAO.getContactList();
        this.contactListAdapter = new ContactListAdapter(this.contactList);

        this.recyclerView = findViewById(R.id.recycler_view_main);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(this.contactListAdapter);

        this.recyclerView.addOnItemTouchListener( new RecyclerTouchListener(getApplicationContext(),
                this.recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Contact contact = contactList.get(position);
                Intent intent = new Intent(getApplicationContext(), ContatoActivity.class);
                intent.putExtra("contact", contact);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Contact contact = contactList.get(position);
                Toast.makeText(getApplicationContext(), contact.getPhone(),Toast.LENGTH_LONG).show();
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
