package com.example.gilles.g_hw_sl_pv_9200.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gilles.g_hw_sl_pv_9200.Backend.DataInterface;
import com.example.gilles.g_hw_sl_pv_9200.Backend.ServiceGenerator;
import com.example.gilles.g_hw_sl_pv_9200.R;
import com.example.gilles.g_hw_sl_pv_9200.Utils;
import com.example.gilles.g_hw_sl_pv_9200.model.Kost;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class KostActivity extends AppCompatActivity {

    @BindView(R.id.kostNaamTextView)
    TextView kostNaam;
    @BindView(R.id.aanmakerTextView)
    TextView aanmaker;
    @BindView(R.id.datumTextView)
    TextView datum;
    @BindView(R.id.bedragTextView)
    TextView bedrag;
    @BindView(R.id.categorieTextView)
    TextView categorie;
    @BindView(R.id.omschrijvingTextView)
    TextView omschrijving;
    @BindView(R.id.goedkeurButton)
    Button goedkeurButton;
    @BindView(R.id.afkeurButton)
    Button afkeurButton;

    ImageView[] images;

    Context context = this;
    private Kost kost;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kost);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            kost = (Kost) getIntent().getSerializableExtra("kost");

            SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/YYYY");
            kostNaam.setText("Kost - " + kost.getNaam());
            aanmaker.setText(kost.getAanmaker());
            datum.setText(sdf.format(kost.getDatum()));
            bedrag.setText("€ " + kost.getBedrag());
            categorie.setText(kost.getCategorie());
            omschrijving.setText(kost.getOmschrijving());

            afkeurButton.getBackground().setColorFilter(0xFFFA8072, PorterDuff.Mode.MULTIPLY);
            goedkeurButton.getBackground().setColorFilter(0xFF7CFC00, PorterDuff.Mode.MULTIPLY);

            createBekeuringen();
        }
    }


    public void goedkeurenClick(View view){
        for(String bekeuring : kost.bekeuringen){
            String[] b = bekeuring.split(",");
            String fullname = Utils.GEBRUIKER.getVoorNaam()+" "+Utils.GEBRUIKER.getNaam();
            if(b[1].equals(fullname)){
                index = kost.bekeuringen.indexOf(bekeuring);
                kost.bekeuringen.set(kost.bekeuringen.indexOf(bekeuring), "true,"+fullname);
            }
        }
        updateKost();
    }

    public void afkeurenClick(View view){
        for(String bekeuring : kost.bekeuringen){
            String[] b = bekeuring.split(",");
            String fullname = Utils.GEBRUIKER.getVoorNaam()+" "+Utils.GEBRUIKER.getNaam();
            if(b[1].equals(fullname)){
                index = kost.bekeuringen.indexOf(bekeuring);
                kost.bekeuringen.set(kost.bekeuringen.indexOf(bekeuring), "false,"+fullname);
            }
        }
        updateKost();
    }

    public void updateKost(){
        DataInterface dataInterface = ServiceGenerator.createService(DataInterface.class);
        Call<Kost> call = dataInterface.updateKost(Utils.GEBRUIKER.getId(), kost.getId(), kost);
        call.enqueue(new Callback<Kost>() {
            @Override
            public void onResponse(Call<Kost> call, Response<Kost> response) {
                kost = response.body();
                if(kost.bekeuringen.get(index).split(",")[0].equals("true")){
                    images[index].setImageResource(R.drawable.checkmark);
                }else{
                    images[index].setImageResource(R.drawable.crossmark);
                }
            }
            @Override
            public void onFailure(Call<Kost> call, Throwable t) {
                Toast.makeText(context, "Fout bij het bekeuren van de kost.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createBekeuringen(){
        TableLayout layout = findViewById(R.id.bekeuringenTableLayout);
        images = new ImageView[kost.bekeuringen.size()];
        for(int i = 0;i < kost.bekeuringen.size();i++){
            TableRow row = new TableRow(this);
            ImageView bekeuringImage = new ImageView(this);
            TextView bekeuringNaam = new TextView(this);
            String[] b = kost.bekeuringen.get(i).split(",");
            switch(b[0]){
                case "false": bekeuringImage.setImageResource(R.drawable.crossmark);break;
                case "true": bekeuringImage.setImageResource(R.drawable.checkmark);break;
                case "null": bekeuringImage.setImageResource(0);break;
            }
            images[i] = bekeuringImage;
            bekeuringNaam.setText(b[1]);
            bekeuringNaam.setTextSize(20);
            bekeuringNaam.setHeight(125);
            bekeuringNaam.setGravity(Gravity.CENTER_VERTICAL);
            bekeuringNaam.setPadding(35,0,0,0);
            row.addView(bekeuringImage);
            row.addView(bekeuringNaam);
            layout.addView(row);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, KostOverzichtScherm.class);
        intent.putExtra("kost", kost);
        context.startActivity(intent);
    }
}
