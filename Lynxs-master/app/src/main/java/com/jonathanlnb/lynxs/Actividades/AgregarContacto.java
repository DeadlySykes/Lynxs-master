package com.jonathanlnb.lynxs.Actividades;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jonathanlnb.lynxs.Adaptadores.ContactoAdapter;
import com.jonathanlnb.lynxs.Herramientas.Tools;
import com.jonathanlnb.lynxs.R;
import com.jonathanlnb.lynxs.TDA.Contacto;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgregarContacto extends AppCompatActivity {
    @BindView(R.id.rContactos)
    RecyclerView rContactos;
    @BindView(R.id.vTexto)
    TextView vTexto;

    private static final int PICK_CONTACT_REQUEST = 200;
    private Uri contacto;
    private Typeface TF;
    private ArrayList<Contacto> contactos = new ArrayList<Contacto>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);
        ButterKnife.bind(this);
        TF = Typeface.createFromAsset(getAssets(), "GoogleSans-Regular.ttf");
        vTexto.setTypeface(TF);
        if(contactos.size() == 0)
            Tools.showMessage(this, getString(R.string.no_contactos));
    }

    private Integer getContacto(Uri uri){
        String id = null;
        Cursor contactCursor = getContentResolver().query(
                uri,
                new String[]{ContactsContract.Contacts._ID},
                null,
                null,
                null);
        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();
        return Integer.parseInt(id);
    }
    private String getTelefono(Uri uri) {
        String phone=null;
        String[] projeccion = new String[] { ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE };
        String selectionClause = ContactsContract.Data.DISPLAY_NAME +" = '"+getNombre(contacto)+"' AND "+ContactsContract.Data.MIMETYPE + "='" +
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC";

        Cursor c = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projeccion,
                selectionClause,
                null,
                sortOrder);
        if(c.moveToNext()){
            if(c.getString(1).equalsIgnoreCase(getNombre(contacto))){
                phone = c.getString(2);
            }
        }
        phone = phone.replaceAll(" ", "");
        return phone;
    }
    private String getNombre(Uri uri) {
        String name = null;
        ContentResolver contentResolver = getContentResolver();
        Cursor c = contentResolver.query(
                uri,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null,
                null,
                null);

        if(c.moveToFirst()){
            name = c.getString(0);
        }
        c.close();

        return name;
    }
    private String getCorreo(Uri uri) {
        Cursor cursor = null;
        String email = "";
        try {
            Uri result = contacto;
            String id = result.getLastPathSegment();
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[]{"" + getContacto(contacto)}, null);
            int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

            if (cursor.moveToFirst())
                email = cursor.getString(emailIdx);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return email;
    }
    private Bitmap getFoto(Uri uri) {
        Bitmap photo = null;
        String id = null;
        Cursor contactCursor = getContentResolver().query(
                uri,
                new String[]{ContactsContract.Contacts._ID},
                null,
                null,
                null);

        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0);
        }
        contactCursor.close();
        try {
            Uri contactUri = ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI,
                    Long.parseLong(id));

            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(
                    getContentResolver(),
                    contactUri);

            if (input != null) {
                photo = BitmapFactory.decodeStream(input);
                input.close();
            }

        } catch (IOException iox) {
            iox.printStackTrace();
        }
        return photo;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                contacto = data.getData();
                contactos.add(new Contacto(Integer.parseInt("" + getContacto(contacto)), getFoto(contacto), getNombre(contacto), getTelefono(contacto), getCorreo(contacto)));
                rContactos.setAdapter(new ContactoAdapter(contactos, AgregarContacto.this));
                rContactos.setLayoutManager(new LinearLayoutManager(AgregarContacto.this));
                Tools.showMessage(AgregarContacto.this, "Contacto agregado correctamente");
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @OnClick(R.id.bAgregarContacto)
    public void agregarContacto() {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }
}
