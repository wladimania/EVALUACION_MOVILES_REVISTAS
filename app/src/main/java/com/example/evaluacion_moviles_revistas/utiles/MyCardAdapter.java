package com.example.evaluacion_moviles_revistas.utiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.example.evaluacion_moviles_revistas.R;

public class MyCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SuperItem> elements;
    private Context context;
    private int HeaderType = 1;

    public MyCardAdapter(Context myContext, List<SuperItem> elements) {
        this.context = myContext;
        this.elements = elements;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //se crea un view y se usa LayoutInflater enviandole como resource el view que creamos
        View view;
        if(viewType==HeaderType){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hmicard, parent, false);
            return new viewHeader(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.micard, parent, false);
            return new RecycleHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof RecycleHolder){
            //se crean los elementos {reta -1 porque el header tambien cuenta como un item,
            // y al querer renderizar el último elemento, este no existe}
            SuperItem card = elements.get(position - 1);
            RecycleHolder miholder = (RecycleHolder) holder;
            miholder.titlecard.setText(card.getTitle());
            miholder.numercard.setText("Num: "+card.getNumber()+ "\t");
            miholder.volumencard.setText("Vol. "+card.getVolume()+ "\t");
            miholder.yearcard.setText("año: " + card.getYear());
            miholder.doicard.setText(card.getDoi());
            miholder.datecard.setText(card.getDate_published());
            //renderiza img
            Glide.with(context).load(card.getCover()).into(miholder.imgCard);
            //añadir animación a la tarjeta
            miholder.card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.rigth_to_left));

        }else if(holder instanceof viewHeader){
            viewHeader miholder = (viewHeader) holder;
            miholder.articlesnumber.setText(String.valueOf(getItemCount()-1));
            miholder.card.setAnimation(AnimationUtils.loadAnimation(context, R.anim.small_to_big));
        }
    }

    public int getItemViewType(int position){
        //examinar solo el elemento 0
        if(position==0) {
            return HeaderType;
        }
        //retorna 0 cuando es un tiem normal.
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return elements.size() + 1;
    }

    /*clase vie holder requerida para enlazar la vista de los items*/
    public static class RecycleHolder extends RecyclerView.ViewHolder{
        private ImageView imgCard;
        private TextView titlecard, numercard, yearcard, volumencard, doicard, datecard;
        CardView card;

        public RecycleHolder(@NonNull View itemView) {
            super(itemView);
            //obtener los elementos del activity y ponerlo a disposición de la clase
            imgCard = itemView.findViewById(R.id.imgcard);
            titlecard = itemView.findViewById(R.id.titlecard);
            volumencard = itemView.findViewById(R.id.volumencard);
            numercard = itemView.findViewById(R.id.numercard);
            yearcard = itemView.findViewById(R.id.yearcard);
            doicard = itemView.findViewById(R.id.doicard);
            datecard = itemView.findViewById(R.id.datecard);
            //establecemos la referencia de la tarjeta para asignarle una animación
            card = itemView.findViewById(R.id.micard);
        }
    }

    public static class viewHeader extends RecyclerView.ViewHolder{
        TextView articlesnumber;
        CardView card;

        public viewHeader(View itemView) {
            super(itemView);
            //obtener los elementos del activity y ponerlo a disposición de la clase
            this.articlesnumber = (TextView) itemView.findViewById(R.id.articlesnumber);
            //establecemos la referencia de la tarjeta para asignarle una animación
            card = itemView.findViewById(R.id.mihead);
        }
    }
}
