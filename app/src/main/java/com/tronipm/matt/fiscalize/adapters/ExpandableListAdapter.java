package com.tronipm.matt.fiscalize.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import com.tronipm.matt.fiscalize.R;


/**
 * Created by PMateus on 07/07/2018.
 * For project Fiscalize.
 * Contact: <paulomatew@gmail.com>
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    Context ctx;
    List<String> headerCollectionArr;
    HashMap<String, List<ExpandableItem>> holderHash;


    public ExpandableListAdapter(Context act,
                                 List<String> headerCollectionArr,
                                 HashMap<String, List<ExpandableItem>> holderHash) {
        this.ctx = act;
        this.headerCollectionArr = headerCollectionArr;
        this.holderHash = holderHash;
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        String st = headerCollectionArr.get(groupPosition);

        Object obj = null;
        if (holderHash.size() > 0) {
            obj = holderHash.get(st).get(childPosititon);
        }

        return obj;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ExpandableItem exp_obj = (ExpandableItem) getChild(groupPosition, childPosition);

        ChildViewHolder viewHolder = new ChildViewHolder();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listview_lv2, null);
            viewHolder.despesaTitulo = (TextView) convertView.findViewById(R.id.expListViewL2_despesa_nome);
            viewHolder.despesaValor = (TextView) convertView.findViewById(R.id.expListViewL2_despesa_valor);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        viewHolder.despesaTitulo.setText(exp_obj.nome);
        viewHolder.despesaValor.setText(exp_obj.valor);

        //Verificação para só aparecer icone de expandir se HOUVER link no ExpandableItem
        if (exp_obj.link == null || exp_obj.link.equals("")) {
            convertView.findViewById(R.id.expListViewL2_imageView).setVisibility(View.INVISIBLE);
        } else {
            convertView.findViewById(R.id.expListViewL2_imageView).setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String st = headerCollectionArr.get(groupPosition);
        int i = 0;
        if (holderHash.size() > 0) {
            List<ExpandableItem> ll = holderHash.get(st);
            if (ll != null) {
                i = ll.size();
            } else {

                i = 0;
            }

        }
        return i;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerCollectionArr.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return headerCollectionArr.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listview_lv1, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.expListViewL1_lb_periodo);
        ImageView image = (ImageView) convertView.findViewById(R.id.expListViewL1_imageView);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        int imageResourceId = 0;

        if (isExpanded) {
            imageResourceId = R.drawable.icon_baixo;
            image.setBackgroundResource(imageResourceId);
            image.setImageResource(imageResourceId);
        } else {
            imageResourceId = R.drawable.icon_direita;
            image.setBackgroundResource(imageResourceId);
            image.setImageResource(imageResourceId);
        }

        image.setVisibility(View.VISIBLE);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ChildViewHolder {
        TextView despesaTitulo;
        TextView despesaValor;

    }

}
