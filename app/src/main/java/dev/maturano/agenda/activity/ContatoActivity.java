package dev.maturano.agenda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import dev.maturano.agenda.MainActivity;
import dev.maturano.agenda.R;
import dev.maturano.agenda.constant.DatabaseConstants;
import dev.maturano.agenda.dao.ContactDAO;
import dev.maturano.agenda.model.Contact;

public class ContatoActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewHolder mViewHolder = new ViewHolder();
    private ContactPOJO mContact = new ContactPOJO();
    private Contact contact;
    private ContactDAO contactDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);
        this.loadViewHolder();

        Intent intent = getIntent();
        this.contact = (Contact) intent.getSerializableExtra("contact");
        if (this.contact != null) {
            this.mViewHolder.edit_contact_name.setText(contact.getName());
            this.mViewHolder.edit_contact_email.setText(contact.getEmail());
            this.mViewHolder.edit_contact_phone.setText(contact.getPhone());
        } else {
            this.contact = new Contact();
        }
        this.contactDAO = new ContactDAO(getApplicationContext());
        this.mViewHolder.button_save.setOnClickListener(this);
    }

    private void loadViewHolder() {
        this.mViewHolder.edit_contact_name = (EditText) findViewById(R.id.edit_nome);
        this.mViewHolder.edit_contact_email = (EditText) findViewById(R.id.edit_email);
        this.mViewHolder.edit_contact_phone = (EditText) findViewById(R.id.edit_phone);
        this.mViewHolder.button_save = (Button) findViewById(R.id.button_save);
        this.mViewHolder.progressBar_contact = (ProgressBar) findViewById(R.id.progressBar_contact);
        this.mViewHolder.layout_contact = (LinearLayout) findViewById(R.id.layout_activity_contact);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_save) {
            this.mContact.name = this.mViewHolder.edit_contact_name.getText().toString();
            this.mContact.email = this.mViewHolder.edit_contact_email.getText().toString();
            this.mContact.phone = this.mViewHolder.edit_contact_phone.getText().toString();

            if (TextUtils.isEmpty(this.mContact.name)) {
                Snackbar.make(this.mViewHolder.layout_contact, R.string.digite_seu_nome, Snackbar.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(this.mContact.email)) {
                Snackbar.make(this.mViewHolder.layout_contact, R.string.digite_seu_email, Snackbar.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(this.mContact.phone)) {
                Snackbar.make(this.mViewHolder.layout_contact, R.string.digite_seu_telefone, Snackbar.LENGTH_LONG).show();
                return;
            }

            this.enableProgressBar(View.VISIBLE, false);
            this.contact.setName(this.mContact.name);
            this.contact.setEmail(this.mContact.email);
            this.contact.setPhone(this.mContact.phone);

            if (this.contact.getId() > 0) {
                this.updateContact();
            } else {
                this.saveContact();
            }
        }
    }

    private void saveContact() {
        showToastSave(contactDAO.insertContact(this.contact));
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void updateContact() {
        showToastSave(this.contactDAO.updateContact(this.contact));
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void showToastSave(Long resultSave) {
        if (resultSave > 0) {
            Toast.makeText(getApplicationContext(), R.string.save_success, Toast.LENGTH_LONG).show();
        } else {
            Snackbar.make(this.mViewHolder.layout_contact, R.string.save_unsuccess, Snackbar.LENGTH_LONG).show();
            this.enableProgressBar(View.GONE, true);
            Log.i("myApp", "Não foi possível Salvar contato = ".concat(this.contact.toString()));
        }
    }

    private void enableProgressBar(int visible, boolean clickableSaveButton) {
        this.mViewHolder.progressBar_contact.setVisibility(visible);
        this.mViewHolder.button_save.setClickable(clickableSaveButton);
    }

    private class ContactPOJO {
        public String name, email, phone;
    }

    private class ViewHolder {
        public EditText edit_contact_name, edit_contact_email, edit_contact_phone;
        public Button button_save;
        public LinearLayout layout_contact;
        public ProgressBar progressBar_contact;
    }
}
