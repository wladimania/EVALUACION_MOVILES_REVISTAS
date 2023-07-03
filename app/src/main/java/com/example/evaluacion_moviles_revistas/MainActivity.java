package com.example.evaluacion_moviles_revistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import com.example.evaluacion_moviles_revistas.utiles.Methods;
import com.example.evaluacion_moviles_revistas.utiles.MyCardAdapter;
import com.example.evaluacion_moviles_revistas.utiles.MyLogs;
import com.example.evaluacion_moviles_revistas.utiles.SuperItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private List<SuperItem> elements;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarVistas();
        MyLogs.setLabel("MyLogs");
        getDataVolley();
    }

    private void iniciarVistas(){
        //recyclerview del activity main
        recView = findViewById(R.id.idlistview);
    }

    private void setValues(List<SuperItem> data){
        //para indicar la posición del poblamiento de los datos {lista o celda}
        //para celdas usariamos gridLayoutManager
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        recView.setLayoutManager(manager);
        //HeaderDecoration headerDecoration = new HeaderDecoration(/* init */);
        //recView.addItemDecoration(headerDecoration);
        //asignar información
        elements = data;
        //agrega los elementos al adaptador
        adapter = new MyCardAdapter(MainActivity.this,elements);
        //ubicar adaptador
        recView.setAdapter(adapter);
        //recView.addItemDecoration(new StartOffsetItemDecoration(10));
    }

    private List<SuperItem> embebedData(){
        //generar datos, solo para pruebas
        List<SuperItem> data = new ArrayList<>();
        for (int i=0;i<15;i++){
            SuperItem item = new SuperItem();
            item.setTitle("Salidos "+i);
            item.setDate_published("23-04-2000 00:00:00");
            item.setCover("https://revistas.uteq.edu.ec/ws/journals.php");
            data.add(item);
        }
        return data;
    }
    private void getDataVolley(){
        //Obtención de datos del web service utilzando Volley
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://revistas.uteq.edu.ec/ws/issues.php?j_id=1",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyLogs.info("ws todo bien");
                        //Procesar las respuesta y armar un Array con estos
                        List<SuperItem> data = processResponse(response.toString());
                        //método para mostrar los datos en el activity
                        setValues(data);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyLogs.error("we error");
                        MessageToast("Error en Volley");
                    }
                }
        );
        queue.add(request);
    }
    private void MessageToast(String message){
        Toast toast= Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    private List<SuperItem> processResponse(String response){
        List<SuperItem> data = new ArrayList<>();
        Gson gson = new Gson();//convertidor de jsonObjecto a Object.class
        //Convertir el string de respuesta(con formato JsonArray) en un JsonArray
        JsonArray jarr = Methods.stringToJsonArray(response);
        //validar cantidad de elementos, para informar en caso de no encontrar alguno
        if(jarr.size() > 0){
            //recorrer los items
            for(int ind = 0; ind <jarr.size(); ind++) {
                //convertir el elemento del json en jsonObject(por defecto los items dentro de
                // JsonArray son de tipo JsonElement)
                JsonObject jso = Methods.JsonElementToJSO(jarr.get(ind));
                //Verificar cantidad de keys dentor del json (si hay 0 lo mas probable es que haya
                // ocurrido algún problema durante la conversión de JsonElement a JsonObject)
                if(jso.size() > 0) {
                    //Casteo de JsonObject s Java.class (en este caso SuperItem)
                    SuperItem item = gson.fromJson(jso.toString(), SuperItem.class);
                    //agrega el item a la lista
                    data.add(item);
                }
            }
        }else{
            //Toast indicando la ausencia de elementos
            MessageToast("No hay registros.");
        }
        //retorno de la lista con todos los elementos
        return data;
    }
    private void runAnimationAgain() {

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_right_to_left);

        recView.setLayoutAnimation(controller);
        adapter.notifyDataSetChanged();
        recView.scheduleLayoutAnimation();

    }
}