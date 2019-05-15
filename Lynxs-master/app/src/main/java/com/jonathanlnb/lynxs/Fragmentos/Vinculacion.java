package com.jonathanlnb.lynxs.Fragmentos;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jonathanlnb.lynxs.Actividades.Principal;
import com.jonathanlnb.lynxs.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Vinculacion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Vinculacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Vinculacion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.lLayout)
    RelativeLayout lLayout;
    @BindView(R.id.cAcceso)
    CardView cAcceso;
    @BindView(R.id.cTipoAcceso)
    CardView cTipoAcceso;
    @BindView(R.id.lDispositivos)
    ListView lDispositivos;
    @BindView(R.id.vInfo)
    TextView vInfo;
    @BindView(R.id.vAgregar)
    TextView vAgregar;

    private ArrayList<BluetoothDevice> dispositivos;
    private Typeface TF;
    private Animation animationA;
    private Animation animationB;
    private CountDownTimer contador = null;
    private Context context;


    public Vinculacion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Vinculacion.
     */
    // TODO: Rename and change types and number of parameters
    public static Vinculacion newInstance(String param1, String param2) {
        Vinculacion fragment = new Vinculacion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vinculacion, container, false);
        ButterKnife.bind(this, v);
        context = getContext();
        TF = Typeface.createFromAsset(context.getAssets(), "GoogleSans-Regular.ttf");
        vAgregar.setTypeface(TF);
        vInfo.setTypeface(TF);
        animationA = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        animationB = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animationA.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cAcceso.setVisibility(View.GONE);
                lLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        cAcceso.startAnimation(animationB);
        contador = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dispositivos = ((Principal) getActivity()).getDispositivos();
                ArrayList<String> aux = new ArrayList<String>();
                for (int i = 0; i < dispositivos.size(); i++) {
                    aux.add(dispositivos.get(i).getName() + "\n" + dispositivos.get(i).getAddress());
                }
                lDispositivos.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, aux));
            }
        }.start();
        dispositivos = ((Principal) getActivity()).getDispositivos();
        ArrayList<String> aux = new ArrayList<String>();
        for (int i = 0; i < dispositivos.size(); i++) {
            aux.add(dispositivos.get(i).getName() + "\n" + dispositivos.get(i).getAddress());
        }
        lDispositivos.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, aux));
        lDispositivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BluetoothDevice device =
                        (BluetoothDevice) dispositivos.get(position);
                ((Principal) getActivity()).setDispositivo(device);
                cAcceso.startAnimation(animationA);
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
