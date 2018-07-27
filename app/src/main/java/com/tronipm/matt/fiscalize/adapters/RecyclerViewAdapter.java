package com.tronipm.matt.fiscalize.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tronipm.matt.fiscalize.R;
import com.tronipm.matt.fiscalize.crawlers.entities.EntidadeSenadorDetalhe;

public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private EntidadeSenadorDetalhe mDataset;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewCpf;
        public TextView textViewFornecedor;
        public TextView textViewDescricao;
        public TextView textViewData;
        public TextView textViewValor;
        public ImageButton btnExpand;


        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewFornecedor = itemView.findViewById(R.id.primary_text);
            this.textViewCpf = itemView.findViewById(R.id.sub_text);
            this.textViewDescricao = itemView.findViewById(R.id.supporting_text);
            this.btnExpand = itemView.findViewById(R.id.expand_button);
            btnExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (textViewDescricao.getVisibility() == View.GONE) {
                        textViewDescricao.setVisibility(View.VISIBLE);
                    } else {
                        textViewDescricao.setVisibility(View.GONE);
                    }
                }
            });
            this.textViewValor = itemView.findViewById(R.id.textViewValor);
            this.textViewData = itemView.findViewById(R.id.sub_textData);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerViewAdapter(EntidadeSenadorDetalhe myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_card_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String aux = mDataset.tabela.linhas.get(position).doc;
        holder.textViewCpf.setText(aux.contains("/") ? "CNPJ: " + aux : "CPF: " + aux);
        holder.textViewFornecedor.setText(mDataset.tabela.linhas.get(position).fornecedor);
        holder.textViewDescricao.setText(mDataset.tabela.linhas.get(position).descricao);
        holder.textViewData.setText("Data: " + mDataset.tabela.linhas.get(position).data);
        holder.textViewValor.setText(mDataset.tabela.linhas.get(position).valor);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.tabela.linhas.size();
    }
}