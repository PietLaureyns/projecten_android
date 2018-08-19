package com.example.gilles.g_hw_sl_pv_9200.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.gilles.g_hw_sl_pv_9200.Backend.DataInterface;
import com.example.gilles.g_hw_sl_pv_9200.Backend.ServiceGenerator;
import com.example.gilles.g_hw_sl_pv_9200.R;
import com.example.gilles.g_hw_sl_pv_9200.Utils;
import com.example.gilles.g_hw_sl_pv_9200.adapter.KostenAdapter;
import com.example.gilles.g_hw_sl_pv_9200.model.Kost;
import com.example.gilles.g_hw_sl_pv_9200.model.KostenOverzicht;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KostOverzichtScherm extends AppCompatActivity {

    private TextView titel, switchTextView;
    private RecyclerView recyclerView;
    private Switch switch1;
    private KostenAdapter adapter;
    private DataInterface dataInterface;
    private int jaar,maand;
    private KostenOverzicht kostenOverzicht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kost_overzicht_scherm);

        titel = findViewById(R.id.textViewKostenHeader);
        switchTextView = findViewById(R.id.switchTextView);
        recyclerView = findViewById(R.id.kostenRecyclerView);
        switch1 = findViewById(R.id.switch1);

        dataInterface = ServiceGenerator.createService(DataInterface.class);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        selectKostenOverzicht(cal.get(Calendar.YEAR), new Date().getMonth());

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(adapter != null){
                    double totalkost = 0;
                    List<Kost> kosten = new ArrayList<Kost>();
                    kosten.addAll(Arrays.asList(kostenOverzicht.kosten));
                    for(Kost kost: kosten)
                        totalkost += kost.bedrag;
                   if(isChecked){
                       for(int i = kosten.size()-1; i>=0;i--){
                           boolean goedgekeurd = true;
                           for(String bekeuring: kosten.get(i).bekeuringen){
                               String[] b = bekeuring.split(",");
                               if(!b[0].equals("true")){
                                   goedgekeurd = false;
                               }
                           }
                           if(!goedgekeurd){
                               totalkost -= kosten.get(i).bedrag;
                               kosten.remove(i);
                           }
                       }
                       adapter.filterList(kosten);
                   }else{
                       adapter.filterList(kosten);
                   }
                    switchTextView.setText("Totaal bedrag per ouder = € "+(totalkost/2));
               }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        dataInterface = ServiceGenerator.createService(DataInterface.class);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        selectKostenOverzicht(cal.get(Calendar.YEAR), new Date().getMonth());
    }

    private void selectKostenOverzicht(int jaar, int maand){
        this.jaar = jaar;
        this.maand = maand;
        Call<KostenOverzicht> cal1 = dataInterface.getKostenOverzichtVanMaand(Utils.GEBRUIKER.getId(),jaar, maand);
        cal1.enqueue(new Callback<KostenOverzicht>() {
            @Override
            public void onResponse(Call<KostenOverzicht> call, Response<KostenOverzicht> response) {
                kostenOverzicht = response.body();
                SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/YYYY");
                titel.setText("Kostenoverzicht - " + sdf.format( kostenOverzicht.getOverzichtDatum()));
                List<Kost> list = new ArrayList<>();
                double totalkost = 0;
                for(Kost k : kostenOverzicht.kosten){
                    totalkost += k.bedrag;
                    list.add(k);
                }
                switchTextView.setText("Totaal bedrag per ouder = € "+(totalkost/2));

                adapter = new KostenAdapter(list ,getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
            @Override
            public void onFailure(Call<KostenOverzicht> call, Throwable t) {
                Log.d("Errormessage", t.getMessage());
            }
        });
    }

    public void voegKostToe(View view){
           Intent intent = new Intent(KostOverzichtScherm.this, KostToevoegen.class);
           startActivity(intent);
    }

    public void onLeftClicked(View view){
        if(maand == 0){
            selectKostenOverzicht(jaar-1, 11);
        }else{
            selectKostenOverzicht(jaar, maand-1);
        }
    }

    public void onRightClicked(View view){
        if(maand == 11){
            selectKostenOverzicht(jaar+1, 0);
        }else{
            selectKostenOverzicht(jaar, maand+1);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(KostOverzichtScherm.this, MainActivity.class);
        startActivity(intent);
    }
}