package com.example.listeserie.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.listeserie.R;
import com.example.listeserie.bdd.Produit;
import com.example.listeserie.tools.DatabaseLinker;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class view_produit extends AppCompatActivity {
    private TableLayout containerProduits;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_produit);
        drawerLayout = findViewById(R.id.drawerLayout);
        Button buttonAjoutProduits = findViewById(R.id.button_ajout_produit);
        buttonAjoutProduits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProduits = new Intent(view_produit.this, View_ajout_produit.class);
                startActivity(intentProduits);
            }
        });
        createProduit();
    }
    public void createProduit() {
        containerProduits.removeAllViews();
        DatabaseLinker linker = new DatabaseLinker(this);
        try {
            Dao<Produit, Integer> daoProduits = linker.getDao(Produit.class);
            List<Produit> produitList = daoProduits.queryForAll();
            for (Produit produit : produitList) {
                TableRow row = new TableRow(this);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setWeightSum(8);

                TableRow.LayoutParams param = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        4f
                );

                TextView labelNom = new TextView(this);
                labelNom.setLayoutParams(param);
                labelNom.setText(produit.getLibelleProduit());
                row.addView(labelNom);

                TextView labelPrenom = new TextView(this);
                labelPrenom.setLayoutParams(param);
                labelPrenom.setText(String.format("%d€", (int) produit.getPrixProduit()));
                row.addView(labelPrenom);

                TableRow.LayoutParams paramButton = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f
                );
                ImageButton deleteClient = new ImageButton(this);
                deleteClient.setLayoutParams(paramButton);
                deleteClient.setImageResource(com.android.car.ui.R.drawable.car_ui_icon_delete);
                deleteClient.setBackground(null);
                row.addView(deleteClient);
                deleteClient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            daoProduits.delete(produit);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        ((ViewGroup) row.getParent()).removeView(row);
                    }
                });
                ImageButton modifClient = new ImageButton(this);
                modifClient.setLayoutParams(paramButton);
                modifClient.setImageResource(com.android.car.ui.R.drawable.car_ui_icon_edit);
                modifClient.setBackground(null);
                row.addView(modifClient);
                modifClient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent monIntent = new Intent(view_produit.this, View_ajout_produit.class);
                        monIntent.putExtra("idProduit", produit.getIdProduit());
                        startActivity(monIntent);
                    }
                });
                containerProduits.addView(row);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}