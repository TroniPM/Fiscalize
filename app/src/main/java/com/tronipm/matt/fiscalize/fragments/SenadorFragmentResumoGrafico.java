package com.tronipm.matt.fiscalize.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesColumn;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAnchor;
import com.anychart.anychart.HoverMode;
import com.anychart.anychart.Position;
import com.anychart.anychart.TooltipPositionMode;
import com.anychart.anychart.ValueDataEntry;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorResumo;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class SenadorFragmentResumoGrafico extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EntidadeSenador senador;
    private EntidadeSenadorResumo resumo;

    public View currentView;
//    private OnFragmentInteractionListener mListener;

    public SenadorFragmentResumoGrafico() {
        // Required empty public constructor
    }

    public void setSenador(EntidadeSenador senador) {
        this.senador = senador;
    }

    public void setResumo(EntidadeSenadorResumo resumo) {
        this.resumo = resumo;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param senador Parameter 1.
     * @return A new instance of fragment SenadorFragmentPerfil.
     */
    public static SenadorFragmentResumoGrafico newInstance(EntidadeSenador senador, EntidadeSenadorResumo resumo) {
        SenadorFragmentResumoGrafico fragment = new SenadorFragmentResumoGrafico();
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
        currentView = inflater.inflate(R.layout.fragment_senador_resumo_grafico, container, false);
//        populate();
        return currentView;
    }


    private void criarGrafico() {
        if (resumo == null) {
            Toast.makeText(getActivity(), "Erro ao consultar informações", Toast.LENGTH_SHORT).show();
            return;
        }
        AnyChartView anyChartView = currentView.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(currentView.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < resumo.tabela.linhas.size(); i++) {
            double vr = Double.parseDouble(resumo.tabela.linhas.get(i).conteudo
                    .replaceAll("\\.", "")
                    .replaceAll(",", "."));
            data.add(new ValueDataEntry(resumo.tabela.linhas.get(i).label,
                    vr));
        }

        CartesianSeriesColumn column = cartesian.column(data);
        //formatação do DECIMAL com.anychart.anychart.Tooltip >> https://docs.anychart.com/Common_Settings/Text_Formatters
        String formatModel = "R$ {%Value}{groupsSeparator:., decimalsCount:2, decimalPoint:\\\\,}";//%.2f
        column.getTooltip()
                .setTitleFormat("{%X}")
                .setPosition(Position.CENTER_BOTTOM)
                .setAnchor(EnumsAnchor.CENTER_BOTTOM)
                .setOffsetX(0d)
                .setOffsetY(5d)
                .setFormat(formatModel);

        cartesian.setAnimation(true);
        cartesian.setTitle(resumo.titulo);

        cartesian.getYScale().setMinimum(0d);

        cartesian.getYAxis().getLabels().setFormat(formatModel);

        cartesian.getTooltip().setPositionMode(TooltipPositionMode.POINT);
        cartesian.getInteractivity().setHoverMode(HoverMode.BY_X);

        cartesian.getXAxis().setTitle("Mês");
        cartesian.getYAxis().setTitle("Valor");

        anyChartView.setChart(cartesian);
    }

    @Override
    public void onResume() {
        super.onResume();

        criarGrafico();
    }
}
