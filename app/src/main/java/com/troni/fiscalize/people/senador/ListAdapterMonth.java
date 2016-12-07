package com.troni.fiscalize.people.senador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.troni.fiscalize.R;

import java.util.List;

public class ListAdapterMonth extends ArrayAdapter<Nota> {

    private List<View.OnClickListener> onClickListners = null;

    public ListAdapterMonth(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapterMonth(Context context, int resource, List<Nota> items, List<View.OnClickListener> onClickListeners) {
        super(context, resource, items);

        this.onClickListners = onClickListeners;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.senador_month_detail_itemlistrow, null);
        }

        final Nota p = getItem(position);

        if (p != null) {
            final int pos = position;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "POSIÇÃO: " + pos + " |  COPY DATA CLIPBOAR (FAZER)", Toast.LENGTH_SHORT).show();
                }
            });

            TextView id = (TextView) v.findViewById(R.id.textView_desp_identificacao);
            TextView forn = (TextView) v.findViewById(R.id.textView_desp_fornecedor);
            TextView desc = (TextView) v.findViewById(R.id.textView_desp_descricao);
            TextView dat = (TextView) v.findViewById(R.id.textView_desp_data);
            TextView vr = (TextView) v.findViewById(R.id.textView_desp_valor);

            if (id != null) {
                id.setText(p.identificacao);
            }
            if (forn != null) {
                forn.setText(p.fornecedor);
            }
            if (desc != null) {
                desc.setText(p.descricao);
            }
            if (dat != null) {
                dat.setText(p.data);
            }
            if (vr != null) {
                vr.setText(p.valor);
            }
        }

        return v;
    }

}