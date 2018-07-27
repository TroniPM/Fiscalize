package com.tronipm.matt.fiscalize.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.activities.SenadorResumoActivity;
import com.tronipm.matt.fiscalize.adapters.ExpandableItem;
import com.tronipm.matt.fiscalize.adapters.ExpandableListAdapter;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorBalancete;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class SenadorFragmentGastos extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    private EntidadeSenador senador;
    private String ano;

    public View currentView = null;

    //    private OnFragmentInteractionListener mListener;
    public void setSenador(EntidadeSenador senador) {
        this.senador = senador;
    }

    public void setAno(String ano) {
        if (ano == null) {
            ano = senador.getAnosDisponiveis().get(senador.getAnosDisponiveis().size() - 1);
        }
        this.ano = ano;
    }

    public SenadorFragmentGastos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param senador Parameter 1.
     * @return A new instance of fragment SenadorFragmentGastos.
     */
    public static SenadorFragmentGastos newInstance(EntidadeSenador senador, String ano) {
        SenadorFragmentGastos fragment = new SenadorFragmentGastos();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, senador);
        args.putString(ARG_PARAM2, ano);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            senador = (EntidadeSenador) getArguments().getSerializable(ARG_PARAM1);
            ano = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_senador_gastos, container, false);
//        populate();
        return currentView;
    }

    public void populate() {
        expandableListView = (ExpandableListView) currentView.findViewById(R.id.expandableListView);
        expandableListView.setGroupIndicator(null);
        EntidadeSenadorBalancete balancete = null;
        //Descobrindo em qual ano está
        if (senador.getConteudoBalancete() != null) {
            for (EntidadeSenadorBalancete in : senador.getConteudoBalancete()) {
                if (in.ano.equals(this.ano)) {
                    balancete = in;
                    break;
                }
            }
        }

        final List<String> titulos = new ArrayList<String>();
        final HashMap<String, List<ExpandableItem>> vinculacao_titulos_linhas =
                new HashMap<String, List<ExpandableItem>>();

        if (balancete != null) {
            //Criando objeto para o adapter
            for (int i = 0; i < balancete.tabela.size(); i++) {
                titulos.add(balancete.tabela.get(i).titulo);
                List<ExpandableItem> linhas = new ArrayList<ExpandableItem>();

                for (int j = 0; j < balancete.tabela.get(i).linhas.size(); j++) {
                    //Crio a Matéria
                    linhas.add(new ExpandableItem(
                            balancete.tabela.get(i).titulo,
                            balancete.tabela.get(i).linhas.get(j).label,
                            balancete.tabela.get(i).linhas.get(j).conteudo,
                            balancete.tabela.get(i).linhas.get(j).link));
                }

                vinculacao_titulos_linhas.put(titulos.get(i), linhas);
            }
            try {
                expandableListView.removeAllViewsInLayout();
            } catch (Exception e) {
            }
            expandableListAdapter = new ExpandableListAdapter(getActivity(), titulos, vinculacao_titulos_linhas);
            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {

                    String key = titulos.get(groupPosition);
                    ExpandableItem item = vinculacao_titulos_linhas.get(key).get(childPosition);
                    if (item.link == null) {
                        Toast.makeText(SenadorFragmentGastos.this.getActivity(), "Sem link", Toast.LENGTH_SHORT).show();
                    } else if (item.pai != null
                            && (item.pai.toLowerCase().contains("benefício")
                            || item.pai.toLowerCase().contains("pessoal")
                            || item.pai.toLowerCase().contains("subsídios")
                            || item.pai.toLowerCase().contains("aposentadoria"))) {
                        //Se não for a respeito de dinheiro, abrir no navegador
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
                        startActivity(browserIntent);
                    } else {

                        SenadorResumoActivity.setSenador(senador);
                        SenadorResumoActivity.setLink(item.link);
                        SenadorResumoActivity.setTitulo(item.nome);
                        SenadorResumoActivity.setTituloBalancete(item.pai);
                        Intent intent = new Intent(getActivity(), SenadorResumoActivity.class);
                        startActivity(intent);
                    }
                    return false;
                }
            });
            expandableListView.setAdapter(expandableListAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        this.populate();
    }

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
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
