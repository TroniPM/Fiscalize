package com.tronipm.matt.fiscalize.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;
import com.tronipm.matt.fiscalize.utils.MLRoundedImageView;

import java.util.Locale;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class SenadorFragmentPerfil extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EntidadeSenador senador;
    private String ano;

    public View currentView;
//    private OnFragmentInteractionListener mListener;

    public SenadorFragmentPerfil() {
        // Required empty public constructor
    }

    public void setSenador(EntidadeSenador senador) {
        this.senador = senador;
    }

    public void setAno(String ano) {
        if (ano == null) {
            ano = senador.getAnosDisponiveis().get(senador.getAnosDisponiveis().size() - 1);
        }
        this.ano = ano;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param senador Parameter 1.
     * @return A new instance of fragment SenadorFragmentPerfil.
     */
    public static SenadorFragmentPerfil newInstance(EntidadeSenador senador, String ano) {
        SenadorFragmentPerfil fragment = new SenadorFragmentPerfil();
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
        currentView = inflater.inflate(R.layout.fragment_senador_fragment_perfil, container, false);
//        populate();
        return currentView;
    }

    public void populate() {

        try {
            Picasso.get().load(senador.getLinkFoto()).into((MLRoundedImageView) currentView.findViewById(R.id.imageView));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ImageView profilePic = (ImageView)view.findViewById(R.id.imageView);
        Locale locale = new Locale("pt", "br");
        TextView nome = (TextView) currentView.findViewById(R.id.textView_nome);
        if (senador.getNomeCivil() != null) {
            nome.setText(senador.getNomeCivil());
        }
        TextView partido = (TextView) currentView.findViewById(R.id.textView_partido);
        if (senador.getPartido() != null) {
            partido.setText(senador.getPartido());
        }
        TextView dtNascimento = (TextView) currentView.findViewById(R.id.textView7);
        if (senador.getDataNascimento() != null) {
            dtNascimento.setText(senador.getDataNascimento());
        }
        TextView naturalidade = (TextView) currentView.findViewById(R.id.textView9);
        if (senador.getNaturalidade() != null) {
            naturalidade.setText(senador.getNaturalidade().toUpperCase(locale));
        }
        TextView gabinete = (TextView) currentView.findViewById(R.id.textView11);
        if (senador.getGabinete() != null) {
            gabinete.setText(senador.getGabinete().toUpperCase(locale));
        }
        TextView telefones = (TextView) currentView.findViewById(R.id.textView13);
        if (senador.getTelefones() != null) {
            telefones.setText(senador.getTelefones());
            telefones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + senador.getTelefones()));
                    startActivity(intent);
                }
            });
        }
        TextView fax = (TextView) currentView.findViewById(R.id.textView15);
        if (senador.getFax() != null) {
            fax.setText(senador.getFax());
            fax.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + senador.getFax()));
                    startActivity(intent);
                }
            });
        }
        TextView email = (TextView) currentView.findViewById(R.id.textView17);
        if (senador.getEmail() != null) {
            email.setText(senador.getEmail().toUpperCase(locale));
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", senador.getEmail(), null));
                    startActivity(Intent.createChooser(intent, "Enviar e-mail"));
                }
            });
        }
        TextView site = (TextView) currentView.findViewById(R.id.textView19);
        if (senador.getSitePessoal() != null) {
            site.setText(senador.getSitePessoal());
            site.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String link = senador.getSitePessoal();
                    if (link != null && !link.isEmpty()) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        startActivity(browserIntent);
                    }
                }
            });
        }
        TextView end = (TextView) currentView.findViewById(R.id.textView21);
        if (senador.getEscritorioApoio() != null) {
            end.setText(senador.getEscritorioApoio().toUpperCase(locale));
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
//        void onFragmentInteraction(Uri uri);
//    }
}
