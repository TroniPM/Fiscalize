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

public class ListAdapterYear extends ArrayAdapter<Dados> {

    private List<View.OnClickListener> onClickListners = null;

    public ListAdapterYear(Context context, int resource, List<Dados> items, List<View.OnClickListener> onClickListeners) {
        super(context, resource, items);

        items.remove(0);
        onClickListeners.remove(0);
        this.onClickListners = onClickListeners;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.senador_year_detail_itemlistrow, null);
        }

        final Dados p = getItem(position);

        if (p != null) {
            //final int pos = position;
            v.setOnClickListener(onClickListners.get(position));

            TextView tt2 = (TextView) v.findViewById(R.id.categoryId);
            TextView tt3 = (TextView) v.findViewById(R.id.description);

            if (tt2 != null) {
                tt2.setText(p.label);
            }

            if (tt3 != null) {
                tt3.setText(p.valor);
            }
        }

        return v;
    }
}