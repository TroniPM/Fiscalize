package com.tronipm.matt.fiscalize.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.activities.SenadorActivity;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;

public class SenadorListCustomAdapter extends ArrayAdapter<EntidadeSenador> /*implements View.OnClickListener*/ {

    private ArrayList<EntidadeSenador> dataSet;
    private Context mContext;

    // View lookup cache
    private class ViewHolder {
        TextView txtNome;
        TextView txtPartido;
        ImageView info;
    }

    public SenadorListCustomAdapter(ArrayList<EntidadeSenador> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        EntidadeSenador dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final int pos = position;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtNome = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtPartido = (TextView) convertView.findViewById(R.id.type);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
//        lastPosition = position;

        viewHolder.txtNome.setText(dataSet.get(position).getNomeCivil());
        viewHolder.txtPartido.setText(dataSet.get(position).getPartido());
//        viewHolder.txtVersion.setText(dataSet.get(position).getId());
        if (dataSet.get(position).getLinkFoto() != null && !dataSet.get(position).getLinkFoto().isEmpty()) {
            //https://github.com/bumptech/glide
            Glide.with(mContext).load(dataSet.get(position).getLinkFoto()).into(viewHolder.info);
            System.out.println("!!!!SIM >> " + dataSet.get(position).getNomeCivil());
        } else {
            System.out.println("NAO >> " + dataSet.get(position).getNomeCivil());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object object = getItem(pos);
                EntidadeSenador dataModel = (EntidadeSenador) object;

                Intent intent = new Intent(mContext, SenadorActivity.class);
                intent.putExtra(SenadorActivity.PARAM1, dataModel);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
