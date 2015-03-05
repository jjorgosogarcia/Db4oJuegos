package com.example.sadarik.dbo4juegos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Principal extends Activity {
    private ArrayList<Juego> juegos = new ArrayList<Juego>();
    private ListView lv;
    private AdaptadorArray ad;
    private String s = "";
    private TextView tvPre;
    private EditText etTitulo, etGenero, etPlataforma;
    private Juego game;
    private static final int CREAR=0;
    private static final int MODIFICAR=1;
    private ObjectContainer bdJuegos;


    /***************************************************************/
    /*                                                             */
    /*                         METODOS ON                          */
    /*                                                             */
    /***************************************************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            String titulo = data.getStringExtra(getString(R.string.titulo));
            String genero = data.getStringExtra(getString(R.string.genero));
            String plataforma = data.getStringExtra(getString(R.string.plataforma));
            int index = data.getIntExtra(getString(R.string.index),-1);
            Juego j = new Juego(titulo,genero,plataforma,"");
            switch (requestCode){
                case CREAR:
                    //Añado el juego ordenado siempre que no esté repetido en la lista
                    if(j.getTitulo().length()==0) {
                        tostada(getString(R.string.error));
                    }else
                    if(!juegos.contains(j)){
                        bdJuegos.store(j);
                        bdJuegos.commit();
                        juegos.clear(); //Comprobar esto
                        Collections.sort(juegos);
                        leerdatos();
                        ad.notifyDataSetChanged();
                    }else{
                        tostada(getString(R.string.Yatiene));
                    }
                    break;
                case MODIFICAR:
                    //Modifico el juego ordenado siempre que no esté repetido en la lista
                    if(j.getTitulo().length()==0) {
                        tostada(getString(R.string.error));
                    }else
                    if(!juegos.contains(j)){
                        game=juegos.get(index);
                        ObjectSet<Juego> videojuegos = bdJuegos.query(
                                new Predicate<Juego>() {
                                    @Override
                                    public boolean match(Juego j) {
                                        return j.getPlataforma().compareTo(game.getPlataforma())==0
                                                && j.getTitulo().compareTo(game.getTitulo())==0;
                                    }
                                });
                        Log.v("Longitud Array",videojuegos.size()+"");
                      if (videojuegos.hasNext()){
                          Juego jue = videojuegos.next();
                            jue.setTitulo(titulo);
                            jue.setGenero(genero);
                            jue.setPlataforma(plataforma);
                            bdJuegos.store(jue);
                            bdJuegos.commit();
                            Log.v("ENTROOO EN EDITARR",jue.toString());
                        }
                        tostada(getString(R.string.modify));
                        juegos.set(index, j);
                        Collections.sort(juegos);
                        ad.notifyDataSetChanged();
                    }else{
                        tostada(getString(R.string.Yatiene));
                    }
                    break;
            }
        }else {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bdJuegos.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        initComponents();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    /* Hacer long click sobre un item del ListView */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (id == R.id.action_eliminar) {
            delete(index);
        } else
        if (id == R.id.action_editar) {
             editar(index);

        } else
        if (id == R.id.action_prestado) {
            prestar(index);
        } else
        if (id == R.id.action_devolver) {
            devolver(index);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_anadir) {
            Intent i = new Intent(this,gestionJuego.class);
            startActivityForResult(i, CREAR);
        } else if (id == R.id.action_borrarTodo) {
                borrarTodo();
        }else if (id == R.id.action_ordenarPlataforma) {
         Collections.sort(juegos, new ordenarPlataforma());
            ad.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savingInstanceState) {
        super.onSaveInstanceState(savingInstanceState);
        savingInstanceState.putSerializable(getString(R.string.objeto), juegos);
    }

    /***************************************************************/
    /*                                                             */
    /*                       METODOS CLICK                         */
    /*                                                             */
    /***************************************************************/

    public void borrarTodo() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.borrarTodos);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        juegos.clear();
                        ad.notifyDataSetChanged();
                    }
                });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    public void devolver(final int index) {
        if (juegos.get(index).getPrestado() == "") {
            tostada(getString(R.string.error2));
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.juegoDevuelto);
            alert.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            juegos.get(index).setPrestado("");
                            ad.notifyDataSetChanged();
                        }
                    });
            alert.setNegativeButton(android.R.string.no, null);
            alert.show();
        }
    }

    public void editar(final int index){
        Intent i = new Intent(this,gestionJuego.class);
        Bundle b = new Bundle();
        b.putSerializable("juegos", juegos.get(index));
        b.putInt("index", index);
        i.putExtras(b);
        startActivityForResult(i, MODIFICAR);
    }

    public void prestar(final int index) {
        if (juegos.get(index).getPrestado() != "") {
            tostada(getString(R.string.yaPrestado));
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.prestar);
            LayoutInflater inflater = LayoutInflater.from(this);
            final View vista = inflater.inflate(R.layout.prestar, null);
            alert.setView(vista);
            final EditText etPres;
            etPres = (EditText) vista.findViewById(R.id.etPrestar);
            alert.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            tvPre = (TextView) findViewById(R.id.tvPrestado);
                            juegos.get(index).setPrestado(etPres.getText().toString());
                            juegos.get(index).setPrestado(getString(R.string.prestado) + " " + juegos.get(index).getPrestado());
                            ad.notifyDataSetChanged();
              }
                    });
            alert.setNegativeButton(android.R.string.no, null);
            alert.show();
        }
    }

    /***************************************************************/
    /*                                                             */
    /*                         AUXILIARES                          */
    /*                                                             */
    /***************************************************************/

    //Iniciamos las variables
    private void initComponents() {
        bdJuegos = Db4oEmbedded.openFile(
                Db4oEmbedded.newConfiguration(), getExternalFilesDir(null) +
                        "/bdJuegos.db4o");
        ad = new AdaptadorArray(this, R.layout.lista_datos, juegos);
        leerdatos();
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
        Collections.sort(juegos);
    }



    //Método que compara los juegos y los oredena por plataforma
    private class ordenarPlataforma implements Comparator<Juego> {
        public int compare(Juego game1, Juego game2) {
            int compara = game1.getPlataforma().compareTo(game2.getPlataforma());
            if(compara == 0){
                return game1.compareTo(game2);
            }
            return compara;
        }
    }

    //Sacamos mensajes de información
    private void tostada(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void leerdatos(){
        List<Juego> games= bdJuegos.query(Juego.class);
        for(Juego j: games){
            juegos.add(j);
        }
    }

    public void delete(final int index){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.eliminarUno);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        game=juegos.get(index);
                        ObjectSet<Juego> videojuegos = bdJuegos.query(
                                new Predicate<Juego>() {
                                    @Override
                                    public boolean match(Juego j) {
                                        return j.getPlataforma().compareTo(game.getPlataforma())==0
                                                && j.getTitulo().compareTo(game.getTitulo())==0;
                                    }
                                });
                        if (videojuegos.hasNext()){
                            Juego j = videojuegos.next();
                            bdJuegos.delete(j);
                            bdJuegos.commit();
                        }
                        juegos.remove(index);
                        ad.notifyDataSetChanged();
                    }
                });

        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
    }

}
