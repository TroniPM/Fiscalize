package com.tronipm.matt.fiscalize.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.activities.SenadorBalanceteActivity;
import com.tronipm.matt.fiscalize.activities.SenadorDetalheActivity;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorResumo;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorTabelaResumoLinha;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class SenadorResumoListAdapter extends ArrayAdapter<EntidadeSenadorTabelaResumoLinha> {

    private ArrayList<EntidadeSenadorTabelaResumoLinha> dataSet;
    private EntidadeSenador senador;
    private Context mContext;

    public SenadorResumoListAdapter(Context context, EntidadeSenador senador, ArrayList<EntidadeSenadorTabelaResumoLinha> data) {
        super(context, R.layout.listview_lv2, data);
        this.dataSet = data;
        this.mContext = context;
        this.senador = senador;

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public EntidadeSenadorTabelaResumoLinha getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final EntidadeSenadorTabelaResumoLinha item = getItem(position);

        final ViewHolder holder;
        if (convertView == null) {
//            convertView = View.inflate(mContext, R.layout.listview_lv2, null);

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_lv2, parent, false);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        System.out.println(item);
        holder.textView.setText(item.label);
        holder.textView2.setText(item.conteudo);
//        holder.view.setBackgroundColor(Color.TRANSPARENT);

        //Verificação para só aparecer icone de expandir se HOUVER link no ExpandableItem
        if (item.link == null || item.link.equals("")) {
            holder.imageView.setVisibility(View.INVISIBLE);
        } else {
            holder.imageView.setVisibility(View.VISIBLE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SenadorDetalheActivity.setSenador(senador);
                    SenadorDetalheActivity.setLink(item.link);
                    SenadorDetalheActivity.setTitulo(item.label);

                    Intent intent = new Intent(mContext, SenadorDetalheActivity.class);
                    mContext.startActivity(intent);
                }
            });

        }

        return convertView;
    }

    private class ViewHolder {
//        private View view;
        private TextView textView;
        private TextView textView2;
        private ImageView imageView;

        private ViewHolder(View view) {
//            this.view = view;
            this.textView = (TextView) view.findViewById(R.id.expListViewL2_despesa_nome);
            this.textView2 = (TextView) view.findViewById(R.id.expListViewL2_despesa_valor);
            this.imageView = (ImageView) view.findViewById(R.id.expListViewL2_imageView);
        }
    }
}


