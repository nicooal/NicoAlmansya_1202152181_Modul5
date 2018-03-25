package com.example.nico.nicoalmansya_1202152181_modul5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;

public class ToDoList extends AppCompatActivity {

    private Database dtBase;
    private RecyclerView rcView;
    private Adapter adapter;
    private ArrayList<AddData> data_list;
    //berisi variabel yang digunakan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        setTitle("To Do List App");

        rcView = findViewById(R.id.rec_view);
        //inisialisasi variabel yang digunakan
        data_list = new ArrayList<>();
        dtBase = new Database(this); //membuat database baru
        dtBase.readdata(data_list); //mengizinkan read data

        //menginisialisasi shared preference
        SharedPreferences sharedP = this.getApplicationContext().getSharedPreferences("Preferences", 0);
        int color = sharedP.getInt("Colourground", R.color.white);

        //membuat adapter baru
        adapter = new Adapter(this, data_list, color);
        //mengatur perubahan ukuran
        rcView.setHasFixedSize(true);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        //inisiasi adapter untuk recycler view
        rcView.setAdapter(adapter);

        //menjalankan method hapus data pada list to do
        hapusgeser();
    }

    public void hapusgeser(){
        //membuat method untuk menghapus item pada to do list
        ItemTouchHelper.SimpleCallback touchcall = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                AddData current = adapter.getData(position);
                if (direction == ItemTouchHelper.RIGHT | direction == ItemTouchHelper.LEFT) {
                    //remove item yang dipilih dengan mengenali todonya sebagai primary key
                    if (dtBase.removedata(current.getTodo())) {
                        //menghapus data
                        adapter.deleteData(position);
                        //membuat snack bar dan pemberitahuan bahwa item sudah terhapus dengan durasi 2 sekon
                        Snackbar.make(findViewById(R.id.coordinator), "List berhasil dihapus", 2000).show();
                    }
                }
            }
        };
        ItemTouchHelper touchhelp = new ItemTouchHelper(touchcall);
        touchhelp.attachToRecyclerView(rcView);
    }

    //ketika menu pada activity di buat
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //method yang dijalankan ketika item di pilih
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //mendapatkan id dari item yang
        int id = item.getItemId();
        //apabila item yang dipilih adalah setting
        if (id==R.id.action_settings){
            //membuat intent ke menu Pengaturan
            Intent intent = new Intent(ToDoList.this, Settings.class);
            //memulai intent
            startActivity(intent);
            //menutup aktivitas setelah intent dijalankan
            finish();
        }
        return true;
    }

    //method yang akan dijalankan ketika tombol add di klik
    public void addlist(View view) {
        //intent ke class add to do
        Intent intent = new Intent(ToDoList.this, AddToDo.class);
        //memulai intent
        startActivity(intent);
    }
}
