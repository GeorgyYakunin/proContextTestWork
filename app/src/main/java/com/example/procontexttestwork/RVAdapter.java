package com.example.procontexttestwork;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.mViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<? extends JsonInfoClass> array;
    private ArrayList<JsonInfoClass> arrayFiltered;
    private Class onClickActivity;
    private Class ArrayType;
    private static final String TAG = "RVAdapter";


    public RVAdapter(ArrayList<? extends JsonInfoClass> a, Context mContext){
        array = a;
        if(a.get(0) instanceof Photographer){
            onClickActivity = AlbumsInfoActivity.class;
            ArrayType = Photographer.class;
        }

        else{
            onClickActivity = GalleryActivity.class;
            ArrayType = Album.class;
        }

        arrayFiltered = (ArrayList<JsonInfoClass>) array;
        this.mContext = mContext;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    arrayFiltered = (ArrayList<JsonInfoClass>) array;
                } else {
                    ArrayList<JsonInfoClass> arrayFilteredList = new ArrayList<>();
                    for (JsonInfoClass row : array) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            arrayFilteredList.add(row);
                        }
                    }
                    arrayFiltered = arrayFilteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayFiltered = (ArrayList<JsonInfoClass>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class mViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView;
        public CardView cardView;
        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item, viewGroup, false);
        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final mViewHolder holder, final int i) {

        final JsonInfoClass item = array.get(i);
        final int ID = item.getId();
        holder.nameTextView.setText(item.getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, onClickActivity);
                intent.putExtra("id", ID);
                mContext.startActivity(intent);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View v) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View inflatedV = inflater.inflate(R.layout.dialog_rename_rv_item, null);
                final EditText etName = (EditText) inflatedV.findViewById(R.id.edit_name);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.rename_item)
                        .setView(inflatedV)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                DBHelper dbHelper = new DBHelper(mContext);
                                String newName = String.valueOf(etName.getText());
                                dbHelper.rename(newName, ArrayType, ID);
                                array.get(i).setName(newName);
                                notifyItemChanged(i);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayFiltered.size();
    }

}
