package com.example.gilles.g_hw_sl_pv_9200.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gilles.g_hw_sl_pv_9200.Backend.DataInterface;
import com.example.gilles.g_hw_sl_pv_9200.Backend.ServiceGenerator;
import com.example.gilles.g_hw_sl_pv_9200.R;
import com.example.gilles.g_hw_sl_pv_9200.Utils;
import com.example.gilles.g_hw_sl_pv_9200.model.Gebruiker;
import com.example.gilles.g_hw_sl_pv_9200.model.Gezin2;
import com.example.gilles.g_hw_sl_pv_9200.model.Kost;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KostToevoegen extends AppCompatActivity {
    @BindView(R.id.bedragEditText)
    EditText bedrag;
    @BindView(R.id.naamEditText)
    EditText naam;
    @BindView(R.id.omschrijvingEditText)
    EditText omschrijving;
    @BindView(R.id.btnKostToevoegen)
    Button btnKostToevoegen;
    @BindView(R.id.categorieSpinner)
    Spinner categorie;
    @BindView(R.id.datumEditText)
    TextView datumTextView;

    Calendar currentDate;
    int day, month, year;
    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kost_toevoegen);
        ButterKnife.bind(this);

        currentDate = Calendar.getInstance();
        selectedDate = currentDate.getTime();
        day = currentDate.get(Calendar.DAY_OF_MONTH);
        month = currentDate.get(Calendar.MONTH);
        year = currentDate.get(Calendar.YEAR);
        datumTextView.setText(day+"/"+(month+1)+"/"+year);
        datumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                DatePickerDialog datePickerDialog = new DatePickerDialog(KostToevoegen.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                        Date date2 = new Date();
                        date2.setYear(year-1900);
                        date2.setMonth(monthOfYear);
                        date2.setDate(dayOfMonth);
                        selectedDate = date2;
                        datumTextView.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    }
                },year, month, day);
                datePickerDialog.show();
            }
        });

        Context context = this;
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Gewoon");
        spinnerArray.add("Buitengewoon");
        spinnerArray.add("Medisch");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorie.setAdapter(adapter);

        btnKostToevoegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bedrag.getText().toString().equals("") || bedrag.getText().toString().length() == 0
                        || naam.getText().toString().equals("") || naam.getText().toString().length() == 0
                        ) {
                    new CustomToast().Show_Toast(context, getWindow().getDecorView().getRootView(),"Het bedrag en de naam moeten ingevuld zijn.");
                } else {
                    DataInterface dataInterface = ServiceGenerator.createService(DataInterface.class);
                    Call<Gezin2> call = dataInterface.getGezinsledenOfHuidigGezin(Utils.GEBRUIKER.getId());
                    call.enqueue(new Callback<Gezin2>() {
                        @Override
                        public void onResponse(Call<Gezin2> call, Response<Gezin2> response) {
                            List<String> bekeuringen = new ArrayList<>();
                            for(Gebruiker gezinslid : response.body().gezinsLeden){
                                if((gezinslid.getVoorNaam()+gezinslid.getNaam()).equals(Utils.GEBRUIKER.getVoorNaam()+Utils.GEBRUIKER.getNaam())){
                                    bekeuringen.add("true,"+gezinslid.getVoorNaam() +" "+gezinslid.getNaam());
                                }else{
                                    bekeuringen.add("null,"+gezinslid.getVoorNaam() +" "+gezinslid.getNaam());
                                }
                            }
                            int mbedrag = (int) Double.parseDouble((bedrag.getText().toString()));
                            Call<Kost> call2 = dataInterface.voegKostToe(Utils.GEBRUIKER.getId(), new Kost(naam.getText().toString(), Utils.GEBRUIKER.getId()
                                    , Utils.GEBRUIKER.getVoorNaam() + " " + Utils.GEBRUIKER.getNaam(), selectedDate, mbedrag, omschrijving.getText().toString(),
                                    categorie.getSelectedItem().toString(), bekeuringen));
                            call2.enqueue(new Callback<Kost>() {
                                @Override
                                public void onResponse(Call<Kost> call, Response<Kost> response) {
                                    Intent intent = new Intent(KostToevoegen.this, KostOverzichtScherm.class);
                                    startActivity(intent);
                                    Toast.makeText(context, "Kost succesvol toegevoegd.", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onFailure(Call<Kost> call, Throwable t) {
                                    Toast.makeText(context, "Fout bij het toevoegen van de kost.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onFailure(Call<Gezin2> call, Throwable t) {
                            Toast.makeText(context, "Fout bij het toevoegen van de kost.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(KostToevoegen.this, KostOverzichtScherm.class);
        startActivity(intent);
    }
}
