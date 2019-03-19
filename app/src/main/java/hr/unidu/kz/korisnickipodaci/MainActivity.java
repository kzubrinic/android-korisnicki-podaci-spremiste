package hr.unidu.kz.korisnickipodaci;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPref, sp2;
    private Gson gson;
    private EditText ime, starost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ime = findViewById(R.id.ime);
        starost = findViewById(R.id.starost);
        //Pristupi inicijalnom spremištu tekuće aktivnosti
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        gson = new Gson();
    }
    public void spremi(View v){
        if (((starost.getText().length()==0) || (starost.getText() == null)) ||
                ((ime.getText().length()==0) || (ime.getText() == null))){
            Toast.makeText(MainActivity.this, "Ime i starost se moraju unijeti!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Stvori objekt tipa Editor koji omogućuje mijenjanje inicijalnog spremišta
        SharedPreferences.Editor editor = sharedPref.edit();
        // Serijaliziraj objekt tipa Korisnik
        Korisnik kor = new Korisnik();
        kor.setIme(ime.getText().toString());
        kor.setStarost(Integer.parseInt(starost.getText().toString()));
        String json = gson.toJson(kor);
        // Zapiši u inicijalno spremište objekt tipa Korisnik s ključem "korisnik"
        // u JSON formatu (niz znakova)
        editor.putString("korisnik", json);
        // Zapiši podatak u spremište
        editor.commit();
        Toast.makeText(MainActivity.this, "Podatak "+json + " zapisan!", Toast.LENGTH_SHORT).show();
    }
    public void citaj(View v){
        Korisnik kor = gson.fromJson(sharedPref.getString("korisnik",""), Korisnik.class);
        if (kor == null){
            Toast.makeText(MainActivity.this, "Korisnik ne postoji u spremištu!", Toast.LENGTH_SHORT).show();
            return;
        }
        ime.setText(kor.getIme());
        starost.setText(String.valueOf(kor.getStarost()));
    }
    public void brisi(View v){
        // Stvori objekt tipa Editor koji omogućuje mijenjanje spremišta
        SharedPreferences.Editor editor = sharedPref.edit();
        if (!sharedPref.contains("korisnik")){
            Toast.makeText(MainActivity.this, "Podatak s ključem \"korisnik\" ne postoji!", Toast.LENGTH_SHORT).show();
            return;
        }
        editor.remove("korisnik");
        // Potvrdi brisanje
        editor.commit();
        ime.setText("");
        starost.setText("");
        Toast.makeText(MainActivity.this, "Podatak s ključem \"korisnik\"obrisan!", Toast.LENGTH_SHORT).show();
    }
}
