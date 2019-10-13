package com.example.procontexttestwork;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<? extends JsonInfoClass> array;
    private ArrayList<JsonInfoClass> arrayFiltered;
    private Class onClickActivity;
    private Class ArrayType;
    private static final String TAG = "RVAdapter";
    private static boolean haveData = true;
    private static final int VIEW_TYPE_CARD = 0;
    private static final int VIEW_TYPE_EMPTY = 1;


    public RVAdapter(ArrayList<? extends JsonInfoClass> a, Context mContext){
        array = a;
        if(array.isEmpty()){
            arrayFiltered =  new ArrayList<JsonInfoClass>();
            }else if (array.get(0) instanceof Photographer) {
            arrayFiltered = (ArrayList<JsonInfoClass>) array;
                onClickActivity = AlbumsInfoActivity.class;
                ArrayType = Photographer.class;
            } else if (array.get(0) instanceof Album){
                arrayFiltered = (ArrayList<JsonInfoClass>) array;
                onClickActivity = GalleryActivity.class;
                ArrayType = Album.class;
            }
            this.mContext = mContext;
        }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                int sequenseSize = charSequence.length();
                String searchString = charSequence.toString().toLowerCase();
                if (sequenseSize == 0) {
                    arrayFiltered = (ArrayList<JsonInfoClass>) array;
                } else {
                    ArrayList<JsonInfoClass> arrayFilteredList = new ArrayList<>();
                    for (JsonInfoClass row : array) {
                        String rowString = row.getName().substring(0,sequenseSize).toLowerCase();
                        Log.e(TAG, rowString + " " + rowString.equals(searchString));
                            if (rowString.equals(searchString)) {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    class cardViewHolder extends ViewHolder{
        public TextView nameTextView;
        public CardView cardView;
        public cardViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    class phViewHolder extends ViewHolder{
        public TextView phTextView;
        public phViewHolder(@NonNull View itemView) {
            super(itemView);
            phTextView = itemView.findViewById(R.id.placeholder_text_view);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;
        ViewHolder vh;
        if (viewType == VIEW_TYPE_CARD) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item, viewGroup, false);
            vh = new cardViewHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_placeholder_item, viewGroup, false);
            vh = new phViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {

        int viewType = getItemViewType(i);
        if (viewType == VIEW_TYPE_EMPTY) {
            phViewHolder placeHolder = (phViewHolder) holder;



        } else if (viewType == VIEW_TYPE_CARD) {
        cardViewHolder cardHolder = (cardViewHolder) holder;
        final JsonInfoClass item = arrayFiltered.get(i);
        final int ID = item.getId();
            cardHolder.nameTextView.setText(item.getName());
            cardHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, onClickActivity);
                intent.putExtra("id", ID);
                mContext.startActivity(intent);
            }
        });
            cardHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
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
    }

    @Override
    public int getItemCount() {
        if(arrayFiltered.size() == 0)
            return 1;
        else
            return arrayFiltered.size();
    }

    public int getItemViewType(int position) {
        if (arrayFiltered.size() == 0)
            return VIEW_TYPE_EMPTY;
        else
            return VIEW_TYPE_CARD;
        }
//

    public void deleteAllData (){
        arrayFiltered = new ArrayList<>();
        notifyDataSetChanged();
    }
    }



