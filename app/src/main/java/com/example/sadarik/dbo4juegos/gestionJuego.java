package com.example.sadarik.dbo4juegos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class gestionJuego extends Activity {

    EditText etTitulo, etGenero,etPlataforma;
    Button btAceptar,btCancelar;
    int index;
    String s="";


    /***************************************************************/
    /*                                                             */
    /*                         METODOS ON                          */
    /*                                                             */
    /***************************************************************/

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_gestion_juego);
            initComponents();
            Bundle b = getIntent().getExtras();
            index=0;
            if(b !=null ){
                index = b.getInt(getString(R.string.index));
                Juego j = (Juego)b.getSerializable(getString(R.string.juegos));
                etTitulo.setText(j.getTitulo());
                etGenero.setText(j.getGenero());
                etPlataforma.setText(j.getPlataforma());
            }
            if(savedInstanceState != null){
            }
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gestion_juego, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        // Comprobar si el boton esta seleccionado
        boolean checked = ((RadioButton) view).isChecked();
        // Mirarmos que boton esta seleccionado
        switch (view.getId()) {
            case R.id.rbPc:
                if (checked)
                    s = getString(R.string.rbPc);
                break;
            case R.id.rbPs3:
                if (checked)
                    s = getString(R.string.rbPs3);
                break;
            case R.id.rbXbox:
                if (checked)
                    s = getString(R.string.rbXbox);
                break;
        }
    }

    /***************************************************************/
    /*                                                             */
    /*                       METODOS CLICK                         */
    /*                                                             */
    /***************************************************************/

    public void aceptar(View v){
        String a,b;
        a = etTitulo.getText().toString();
        b = etGenero.getText().toString();
        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.titulo),a);
        bundle.putString(getString(R.string.genero),b);
        bundle.putString(getString(R.string.plataforma),s);
        bundle.putInt(getString(R.string.index),index);
        i.putExtras(bundle);
        //s=""; para que si se nos olvida poner una plataforma no nos coja la anterior
        s = "";
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void cancelar(View v){
            Intent i = new Intent();
            setResult(Activity.RESULT_CANCELED, i);
            finish();
        }

    private void initComponents(){
        etTitulo = (EditText)findViewById(R.id.etTitulo);
        etGenero = (EditText)findViewById(R.id.etGenero);
        etPlataforma =(EditText)findViewById(R.id.etPlataforma);
        btAceptar = (Button)findViewById(R.id.btAceptar);
        btCancelar = (Button)findViewById(R.id.btCancelar);
    }
}
