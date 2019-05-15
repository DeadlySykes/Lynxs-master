package com.jonathanlnb.lynxs.Adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jonathanlnb.lynxs.Actividades.AgregarContacto;
import com.jonathanlnb.lynxs.R;
import com.jonathanlnb.lynxs.TDA.Contacto;

import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {
    private List<Contacto> listaContactos;
    private Context context;
    private Contacto c;
    private Typeface TF;


    public ContactoAdapter(List<Contacto> listaContactos, Context context) {
        this.listaContactos = listaContactos;
        this.context = context;
        TF = Typeface.createFromAsset(context.getAssets(), "GoogleSans-Regular.ttf");

    }

    public ContactoAdapter.ContactoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_contacto, parent, false);
        return new ContactoViewHolder(v);
    }

    public void onBindViewHolder(ContactoAdapter.ContactoViewHolder holder, int position) {
        if (listaContactos.get(position).getImagen() == null)
            holder.iContacto.setImageResource(R.drawable.lynx);
        else
            holder.iContacto.setImageBitmap(listaContactos.get(position).getImagen());
        holder.vNombre.setText(listaContactos.get(position).getNombre());
        holder.vTelefono.setText("" + listaContactos.get(position).getTelefono());
        holder.vCorreo.setText(listaContactos.get(position).getCorreo());
    }

    public int getItemCount() {
        if (listaContactos != null)
            return listaContactos.size();
        else
            return 0;
    }

    public class ContactoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private ImageView iContacto;
        private TextView vNombre;
        private TextView vTelefono;
        private TextView vCorreo;

        public ContactoViewHolder(View itemView) {
            super(itemView);
            iContacto = (ImageView) itemView.findViewById(R.id.iContacto);
            vNombre = (TextView) itemView.findViewById(R.id.vNombre);
            vTelefono = (TextView) itemView.findViewById(R.id.vTelefono);
            vCorreo = (TextView) itemView.findViewById(R.id.vCorreo);
            vNombre.setTypeface(TF);
            vTelefono.setTypeface(TF);
            vCorreo.setTypeface(TF);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            MenuItem llamada = menu.add(Menu.NONE, 1, 1, "Llamar");
            llamada.setOnMenuItemClickListener(listenerOnLlamarMenu);
            MenuItem mensaje = menu.add(Menu.NONE, 1, 1, "Mensaje Txt");
            mensaje.setOnMenuItemClickListener(listenerOnMensajeMenu);
            MenuItem correo = menu.add(Menu.NONE, 1, 1, "Mandar Correo");
            correo.setOnMenuItemClickListener(listenerOnCorreoMenu);
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Eliminar");
            delete.setOnMenuItemClickListener(listenerOnDeleteMenu);

        }

        MenuItem.OnMenuItemClickListener listenerOnDeleteMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                c = listaContactos.get(getAdapterPosition());
                AlertDialog.Builder msg = new AlertDialog.Builder(context);
                msg.setTitle("Eliminar");
                msg.setMessage("¿Seguro que quieres eliminar al contacto\"" + c.getNombre() + ": " + c.getTelefono() + "\"?");
                msg.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    listaContactos.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                msg.show();
                return true;
            }
        };
        MenuItem.OnMenuItemClickListener listenerOnLlamarMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                c = listaContactos.get(getAdapterPosition());
                AlertDialog.Builder msg = new AlertDialog.Builder(context);
                msg.setTitle("Llamar");
                msg.setMessage("¿Seguro que quieres llamar al contacto \"" + c.getNombre() + ": " + c.getTelefono() + "\"?");
                msg.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intTel = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + c.getTelefono()));
                        try {
                            context.startActivity(intTel);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                msg.show();
                return true;
            }
        };
        MenuItem.OnMenuItemClickListener listenerOnMensajeMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                c = listaContactos.get(getAdapterPosition());
                AlertDialog.Builder msg = new AlertDialog.Builder(context);
                msg.setTitle("Mensaje Txt");
                msg.setMessage("¿Seguro que quieres mandar un mensaje al contacto \"" + c.getNombre() + ": " + c.getTelefono() + "\"?");
                msg.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(c.getTelefono(), null, "Hola " + c.getNombre() + " ahora eres mi contacto de confianza en la App LYNXs ", null, null);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                msg.show();
                return true;
            }
        };
        MenuItem.OnMenuItemClickListener listenerOnCorreoMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                c = listaContactos.get(getAdapterPosition());
                if (c.getCorreo().length() > 9) {
                    AlertDialog.Builder msg = new AlertDialog.Builder(context);
                    msg.setTitle("Correo");
                    msg.setMessage("¿Seguro que quieres mandar un correo al contacto \"" + c.getNombre() + ": " + c.getCorreo() + "\"?");
                    msg.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String emails[] = {c.getCorreo()};
                            Intent intEmail = new Intent(Intent.ACTION_SEND);
                            intEmail.setType("text/plain");
                            intEmail.putExtra(Intent.EXTRA_SUBJECT, "Hola, Ahora eres mi contacto de confianza");
                            intEmail.putExtra(Intent.EXTRA_EMAIL, emails);
                            intEmail.putExtra(Intent.EXTRA_CC, emails);
                            intEmail.putExtra(Intent.EXTRA_TEXT, "Hola " + c.getNombre() + " ahora eres mi contacto de confianza en la App LYNXs **Correo Enviado Por La App LYNXs**");
                            context.startActivity(intEmail);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    msg.show();
                } else
                    Toast.makeText(context, "El Contacto no cuenta con correo electronico.", Toast.LENGTH_SHORT).show();
                return true;
            }
        };
    }
}
