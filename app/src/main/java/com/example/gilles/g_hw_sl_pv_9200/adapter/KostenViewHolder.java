package com.example.gilles.g_hw_sl_pv_9200.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gilles.g_hw_sl_pv_9200.R;
import com.example.gilles.g_hw_sl_pv_9200.model.Kost;

import java.text.SimpleDateFormat;

public class KostenViewHolder extends RecyclerView.ViewHolder{

    CardView cv;
    TextView kostnaam;
    TextView datum;
    TextView bedrag;
    ImageView bekeuring;
    View v;

    public KostenViewHolder(View itemView) {
        super(itemView);
        this.v = itemView;
        this.cv = v.findViewById(R.id.cardView);
    }

    public void setData(Kost kost){
        kostnaam = v.findViewById(R.id.kostNaamTextView);
        if(kost.getNaam().length() > 15)
            kostnaam.setText(kost.getNaam().substring(0,15));
        else
            kostnaam.setText(kost.getNaam());

        datum = v.findViewById(R.id.datumTextView);
        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/YYYY");
        datum.setText(sdf.format(kost.getDatum()));

        bedrag = v.findViewById(R.id.bedragTextView);
        bedrag.setText("€ "+kost.getBedrag());

        bekeuring = v.findViewById(R.id.bekeuringImageView);
        Boolean goedgekeurd = true;
        for(String bekeuring: kost.bekeuringen){
            String[] b = bekeuring.split(",");
            if(b[0].equals("false"))
                goedgekeurd = false;
            if(b[0].equals("null"))
                goedgekeurd = null;
        }
        if(goedgekeurd == null){
            bekeuring.setImageResource(0);
        }else if(!goedgekeurd){
            bekeuring.setImageResource(R.drawable.crossmark);
        }
    }
}
