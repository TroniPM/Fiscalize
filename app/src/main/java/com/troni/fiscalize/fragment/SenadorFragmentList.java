package com.troni.fiscalize.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.troni.fiscalize.activities.MainActivity;
import com.troni.fiscalize.R;
import com.troni.fiscalize.people.senador.Senador;
import com.troni.fiscalize.session.Session;

public class SenadorFragmentList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int mLastFirstVisibleItem = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SenadorFragmentList() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SenadorFragmentList newInstance(String param1, String param2) {
        SenadorFragmentList fragment = new SenadorFragmentList();
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
        View v = inflater.inflate(R.layout.fragment_senador_fragment_list, container, false);
        final ListView lv = (ListView) v.findViewById(R.id.lista_senadores);

        final MainActivity main = (MainActivity) getActivity2();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                main.db_sql.selectSenadoresGetNome());

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putInt(SenadorFragmentMain.ARG_PARAM1, position + 1);

                SenadorFragmentMain frag = SenadorFragmentMain.newInstance(position + 1);

                main.switchContent(frag);
            }
        });

        final FloatingActionButton fab1 = (FloatingActionButton) v.findViewById(R.id.btn_atualizar_lista_senadores);


        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > mLastFirstVisibleItem) {
                    fab1.hide();
                } else if (firstVisibleItem < mLastFirstVisibleItem) {
                    fab1.show();
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main, "Atualizando... (FAZER)", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Session.createLog(SenadorFragmentList.class.getName(), "onResume()", null);

        Session.currentSenador = new Senador();
    }
    public Activity getActivity2() {
        return Session.currentActivity;
    }
}
