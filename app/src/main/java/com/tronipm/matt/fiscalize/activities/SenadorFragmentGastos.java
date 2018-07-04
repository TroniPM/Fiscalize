package com.tronipm.matt.fiscalize.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.adapters.expandablelistview.ExpandableCollection;
import com.tronipm.matt.fiscalize.adapters.expandablelistview.ExpandableListAdapter;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;
import java.util.HashMap;

public class SenadorFragmentGastos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    // TODO: Rename and change types of parameters
    private EntidadeSenador senador;

//    private OnFragmentInteractionListener mListener;

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
    // TODO: Rename and change types and number of parameters
    public static SenadorFragmentGastos newInstance(EntidadeSenador senador) {
        SenadorFragmentGastos fragment = new SenadorFragmentGastos();
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
        View view = inflater.inflate(R.layout.fragment_senador_fragment_gastos, container, false);
        populate(view);
        return view;
    }

    private void populate(View view) {
        ExpandableCollection.key_value = new ArrayList<String>();
        ExpandableCollection.expandable_main_arr = new ArrayList<ExpandableCollection>();
        ExpandableCollection.expandable_hashmap = new HashMap<String, ArrayList<ExpandableCollection>>();
        if (senador.getTabelas() != null && senador.getTabelas().get(0).linhas != null)
            for (int i = 0; i < senador.getTabelas().size(); i++) {
                //Crio o período
                ExpandableCollection.key_value.add(senador.getTabelas().get(i).titulo);
                ArrayList<ExpandableCollection> arr_obj = new ArrayList<ExpandableCollection>();

                for (int j = 0; j < senador.getTabelas().get(i).linhas.size(); j++) {
                //Crio a Matéria
                arr_obj.add(new ExpandableCollection(
                        senador.getTabelas().get(i).linhas.get(j).label,
                        senador.getTabelas().get(i).linhas.get(j).conteudo,
                        senador.getTabelas().get(i).linhas.get(j).link));
                }
                ExpandableCollection.expandable_hashmap.put(ExpandableCollection.key_value.get(i), arr_obj);
            }

        expandableListAdapter = new ExpandableListAdapter(getActivity(),
                ExpandableCollection.key_value,
                ExpandableCollection.expandable_hashmap);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                if (ExpandableCollection.key_value != null) {
                    if (ExpandableCollection.key_value.size() > groupPosition) {
                        String key = ExpandableCollection.key_value.get(groupPosition);

                        if (ExpandableCollection.expandable_hashmap.size() > 0) {
                            ExpandableCollection obj_exp = ExpandableCollection.expandable_hashmap
                                    .get(key).get(childPosition);

                            if (obj_exp.tipo == 1) {
                                //Abrir link em browser...
//                                abrirLinkInBrowser(obj_exp.link);
                            } else {
                                if (obj_exp.link == null || obj_exp.link.isEmpty()) {
                                    //Se não tiver link, não faço action.
                                } else {
//                                    SenadorFragmentMainYearDetail fragment = SenadorFragmentMainYearDetail.newInstance(obj_exp.link);
//                                    ((MainActivity) getActivity2()).switchContent(fragment);
                                }
                            }
                        }
                    }
                }
                return false;
            }
        });

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
