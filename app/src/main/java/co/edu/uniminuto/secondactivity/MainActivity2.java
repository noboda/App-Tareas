package co.edu.uniminuto.secondactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    private ListView listViewTask;
    private Button btnBack;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnBack), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initObject();
        listData();
        deleteTask();
        updateResult();

        // Cerrar MainActiity2
        this.btnBack.setOnClickListener(view -> finish());
    }

    // Obtener lista de MainActivity
    private void listData(){
        Intent intent = getIntent();
        arrayList = intent.getStringArrayListExtra("taskList");
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        adapter.clear();
        adapter.addAll(arrayList);
        adapter.notifyDataSetChanged();
    }

    // Eliminar tarea
    private void deleteTask(){
        listViewTask.setOnItemClickListener((parent, view, position, id) -> {
            String removedTask = arrayList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Tarea Eliminada: " + removedTask, Toast.LENGTH_SHORT).show();
            updateResult();
        });
    }

    // Enviar lista actulizada a MainActivity
    private void updateResult() {
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("updatedList", arrayList);
        setResult(RESULT_OK, resultIntent);
    }

    // Inicializador
    private void initObject(){
        this.btnBack = findViewById(R.id.btnBack);
        this.listViewTask = findViewById(R.id.listViewTask);
        this.arrayList = new ArrayList<>();
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.arrayList);
        this.listViewTask.setAdapter(this.adapter);
    }
}