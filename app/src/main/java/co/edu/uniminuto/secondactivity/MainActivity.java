package co.edu.uniminuto.secondactivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    // Parte Grafica
    private EditText etTask;
    private Button btnAdd;
    private ListView listTask;
    private EditText etSearch;

    // Propio de aqui
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnBack), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initObject();
        setEtSearch();
        secondActivity();
        setActivityResultLauncher();


        // Generar evento click -> Agregar tarea
        this.btnAdd.setOnClickListener(this::addTask);
    }

    // Agregar Tarea
    private void addTask(View view){
        // 1. Capturar el texto que introduce el usuario
        String task = this.etTask.getText().toString().trim();
        if (!task.isEmpty()){
            this.arrayList.add(task);
            this.adapter.notifyDataSetChanged();
            this.etTask.setText("");
        } else {
            Toast.makeText(this, "Coloque una tarea", Toast.LENGTH_SHORT).show();
        }
    }

    // Ir al 2do activity
    private void secondActivity(){
        this.listTask.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putStringArrayListExtra("taskList", new ArrayList<>(arrayList));
            activityResultLauncher.launch(intent);
        });
    }

    // Activity Result Launcher
    private void setActivityResultLauncher () {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        ArrayList<String> updatedList = result.getData().getStringArrayListExtra("updatedList");
                        etSearch.setText("");
                        if (updatedList != null) {
                            arrayList.clear();
                            arrayList.addAll(updatedList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    // Buscardor de tareas
    private void setEtSearch () {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Filtro de tareas
    private void taskFilter (String searchText) {
        List<String> listFiltered = arrayList.stream()
                .filter(listTask -> listTask
                .toLowerCase()
                .contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
        adapter.clear();
        adapter.addAll(listFiltered);
        adapter.notifyDataSetChanged();
    }

    // Inicializador
    private void initObject(){
        this.etTask = findViewById(R.id.etTask);
        this.etSearch = findViewById(R.id.etSearch);
        this.btnAdd = findViewById(R.id.btnAdd);
        this.listTask = findViewById(R.id.listTask);
        this.arrayList = new ArrayList<>();
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.arrayList);
        this.listTask.setAdapter(this.adapter);
    }
}