package com.tronipm.matt.fiscalize.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.activities.SenadorActivity;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.util.ArrayList;

/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class SenadorListCustomAdapter extends ArrayAdapter<EntidadeSenador> {

    private ArrayList<EntidadeSenador> dataSet;
    private Context mContext;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;

    public SenadorListCustomAdapter(TextDrawable.IBuilder mDrawableBuilder, ArrayList<EntidadeSenador> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mDrawableBuilder = mDrawableBuilder;
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public EntidadeSenador getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.row_senador_listactivity, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final EntidadeSenador item = getItem(position);

        holder.textView.setText(item.getNomeCivil());
        holder.textView2.setText(item.getPartido());
        TextDrawable drawable = mDrawableBuilder.build(String.valueOf(item.getNomeCivil().charAt(0)), mColorGenerator.getColor(item.getNomeCivil()));
        holder.imageView.setImageDrawable(drawable);
        holder.view.setBackgroundColor(Color.TRANSPARENT);

        if (item.getLinkFoto() != null) {
//            try {
//                Bitmap bitmap = Picasso.get().load(dataSet.get(position).getLinkFoto()).get();
//                if (dataSet.get(position).getLinkFoto().equals(dataSet.get(viewHolder.position).getLinkFoto())) {
//                    viewHolder.info.setImageBitmap(bitmap);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            Glide.with(mContext).load(dataSet.get(position).getLinkFoto()).into(holder.imageView);
//            new ThumbnailTask(pos, dataSet.get(pos).getLinkFoto(), viewHolder)
//                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
//            new ThumbnailTask(pos, dataModel.getLinkFoto(), viewHolder).execute();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SenadorActivity.class);
                intent.putExtra(SenadorActivity.PARAM1, item);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private class ViewHolder {
        private View view;
        private ImageView imageView;
        private TextView textView;
        private TextView textView2;

        private ViewHolder(View view) {
            this.view = view;
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
            this.textView = (TextView) view.findViewById(R.id.textView);
            this.textView2 = (TextView) view.findViewById(R.id.textView2);
        }
    }
}


