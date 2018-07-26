package com.tronipm.matt.fiscalize.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.adapters.SenadorResumoListAdapter;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorResumo;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;
import com.tronipm.matt.fiscalize.utils.MLRoundedImageView;

import java.util.Locale;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class SenadorFragmentResumoGastos extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EntidadeSenador senador;
    private EntidadeSenadorResumo resumo;
    private ListView listView = null;
    private TextView textView = null;

    public View currentView;
//    private OnFragmentInteractionListener mListener;

    public SenadorFragmentResumoGastos() {
        // Required empty public constructor
    }

    public void setSenador(EntidadeSenador senador) {
        this.senador = senador;
    }

    public void setResumo(EntidadeSenadorResumo resumo) {
        this.resumo = resumo;
    }

    public static SenadorFragmentResumoGastos newInstance(EntidadeSenador senador, EntidadeSenadorResumo resumo) {
        SenadorFragmentResumoGastos fragment = new SenadorFragmentResumoGastos();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, senador);
        args.putSerializable(ARG_PARAM2, resumo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            senador = (EntidadeSenador) getArguments().getSerializable(ARG_PARAM1);
            resumo = (EntidadeSenadorResumo) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_senador_resumo_gastos, container, false);

        listView = (ListView) currentView.findViewById(R.id.list);
        textView = (TextView) currentView.findViewById(R.id.textView);

        return currentView;
    }

    public void populate() {
        textView.setText(resumo.titulo);
        listView.setAdapter(new SenadorResumoListAdapter(getActivity(), senador, resumo.tabela.linhas));
    }

    @Override
    public void onResume() {
        super.onResume();

        this.populate();
    }
}
