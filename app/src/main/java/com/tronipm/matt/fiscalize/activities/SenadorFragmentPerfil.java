package com.tronipm.matt.fiscalize.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;
import com.tronipm.matt.fiscalize.utils.MLRoundedImageView;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SenadorFragmentPerfil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SenadorFragmentPerfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SenadorFragmentPerfil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private EntidadeSenador senador;

//    private OnFragmentInteractionListener mListener;

    public SenadorFragmentPerfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param senador Parameter 1.
     * @return A new instance of fragment SenadorFragmentPerfil.
     */
    // TODO: Rename and change types and number of parameters
    public static SenadorFragmentPerfil newInstance(EntidadeSenador senador) {
        SenadorFragmentPerfil fragment = new SenadorFragmentPerfil();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, senador);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            senador = (EntidadeSenador) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_senador_fragment_perfil, container, false);
        populate(view);
        return view;
    }

    private void populate(View view) {

        try {
            Picasso.get().load(senador.getLinkFoto()).into((MLRoundedImageView) view.findViewById(R.id.imageView));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ImageView profilePic = (ImageView)view.findViewById(R.id.imageView);

        TextView nome = (TextView)view.findViewById(R.id.textView_nome);
        nome.setText(senador.getNomeCivil());
        TextView partido = (TextView)view.findViewById(R.id.textView_partido);
        partido.setText(senador.getPartido());
        TextView dtNascimento = (TextView)view.findViewById(R.id.textView7);
        dtNascimento.setText(senador.getDataNascimento());
        TextView naturalidade = (TextView)view.findViewById(R.id.textView9);
        naturalidade.setText(senador.getNaturalidade());
        TextView gabinete = (TextView)view.findViewById(R.id.textView11);
        gabinete.setText(senador.getGabinete());
        TextView telefones = (TextView)view.findViewById(R.id.textView13);
        telefones.setText(senador.getTelefones());
        TextView fax = (TextView)view.findViewById(R.id.textView15);
        fax.setText(senador.getFax());
        TextView email = (TextView)view.findViewById(R.id.textView17);
        email.setText(senador.getEmail());
        TextView site = (TextView)view.findViewById(R.id.textView19);
        site.setText(senador.getSitePessoal());
        TextView end = (TextView)view.findViewById(R.id.textView21);
        end.setText(senador.getEscritorioApoio());
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
