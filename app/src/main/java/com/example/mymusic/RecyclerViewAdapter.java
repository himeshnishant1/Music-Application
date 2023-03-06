package com.example.mymusic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    ArrayList<MusicModel> model;

    public RecyclerViewAdapter(Context context, ArrayList<MusicModel> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        String name = model.get(position).getName();
        holder.tvName.setText(name.substring(0, name.lastIndexOf('.')));
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, PlayActivity.class)
                        .putExtra("Songs", model)
                        .putExtra("SongName", name)
                        .putExtra("pos", holder.getAdapterPosition())
                );
            }
        });
        //holder.tvPath.setText(model.get(position).getPath());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPath;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvName.setSelected(true);
            //tvPath = itemView.findViewById(R.id.tvPath);
        }
    }
}
