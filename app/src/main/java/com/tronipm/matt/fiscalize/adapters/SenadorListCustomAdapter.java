package com.tronipm.matt.fiscalize.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.activities.SenadorActivity;
import com.tronipm.matt.fiscalize.entities.EntidadeSenador;

import java.io.IOException;
import java.util.ArrayList;

public class SenadorListCustomAdapter extends ArrayAdapter<EntidadeSenador> /*implements View.OnClickListener*/ {

    private ArrayList<EntidadeSenador> dataSet;
    private Context mContext;

    // View lookup cache
    private class ViewHolder {
        int position = -1;
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
            viewHolder.info.setTag("" + position);

            viewHolder.position = pos;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtNome.setText(dataModel.getNomeCivil());
        viewHolder.txtPartido.setText(dataModel.getPartido());
        viewHolder.position = pos;

        if (dataModel.getLinkFoto() != null) {
//            try {
//                Bitmap bitmap = Picasso.get().load(dataSet.get(position).getLinkFoto()).get();
//                if (dataSet.get(position).getLinkFoto().equals(dataSet.get(viewHolder.position).getLinkFoto())) {
//                    viewHolder.info.setImageBitmap(bitmap);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            //Glide.with(mContext).load(dataSet.get(position).getLinkFoto()).into(viewHolder.info);
//            new ThumbnailTask(pos, dataSet.get(pos).getLinkFoto(), viewHolder)
//                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
//            new ThumbnailTask(pos, dataModel.getLinkFoto(), viewHolder).execute();
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

    class ThumbnailTask extends AsyncTask<Void, Void, Bitmap> {
        private final int mPosition;
        private final String mLink;
        private final ViewHolder mHolder;

        public ThumbnailTask(int position, String link, ViewHolder holder) {
            mPosition = position;
            mHolder = holder;
            mLink = link;
        }

        @Override
        protected Bitmap doInBackground(Void... arg0) {
            Bitmap bitmap = null;
            try {
                bitmap = Picasso.get().load(mLink).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                if (dataSet.get(mPosition).getLinkFoto().equals(mLink)
                        && mHolder.position == mPosition
                        && mHolder.info.getTag().equals("" + mPosition)) {

                    mHolder.info.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
