package com.example.laptoppricepredictor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btnPredict;
    TextView showPrice;
    String predictedPrice;
    private EditText inputWeight;
    private Spinner spinnerCompany, spinnerTypeName, spinnerRam, spinnerTouchScreen, spinnerIps, spinnerScreenSize, spinnerScreenResolution, spinnerCpu, spinnerSSD, spinnerHDD, spinnerGpu, spinnerOs;

    private static final String URL = "https://laptop-price-predictor-app.onrender.com/predict";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUI();
        setupSpinners();
        btnPredict.setOnClickListener(v -> {
            checkValidation();
        });

    }

    private void checkValidation() {
        if (spinnerCompany.getSelectedItem().equals("Select Brand")) {
            Toast.makeText(MainActivity.this, "Please enter Brand/Company!!!", Toast.LENGTH_SHORT).show();
        } else if (spinnerTypeName.getSelectedItem().equals("Select Type")) {
            Toast.makeText(MainActivity.this, "Please enter Type!!!", Toast.LENGTH_SHORT).show();
        } else if (spinnerRam.getSelectedItem().equals("Select Ram")) {
            Toast.makeText(MainActivity.this, "Please select RAM!!!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(inputWeight.getText().toString().trim()) ||
                !inputWeight.getText().toString().trim().matches("^\\d*(\\.\\d{1})?$") ||
                Double.parseDouble(inputWeight.getText().toString().trim()) <= 1 ||
                Double.parseDouble(inputWeight.getText().toString().trim()) >= 4) {
            inputWeight.setError("Enter Valid Weight!!!");
            inputWeight.requestFocus();
        } else if (spinnerTouchScreen.getSelectedItem().equals("TouchScreen")) {
            Toast.makeText(MainActivity.this, "Please specify if TouchScreen is available!!!", Toast.LENGTH_SHORT).show();
        } else if (spinnerIps.getSelectedItem().equals("IPS")) {
            Toast.makeText(MainActivity.this, "Please specify if IPS is available!!!", Toast.LENGTH_SHORT).show();
        } else if (spinnerScreenSize.getSelectedItem().equals("ScreenSize")) {
            Toast.makeText(MainActivity.this, "Please select Screen Size!!!", Toast.LENGTH_SHORT).show();
        } else if (spinnerScreenResolution.getSelectedItem().equals("ScreenResolution")) {
            Toast.makeText(MainActivity.this, "Please select Screen Resolution!!!", Toast.LENGTH_SHORT).show();
        } else if (spinnerCpu.getSelectedItem().equals("Select CPU")) {
            Toast.makeText(MainActivity.this, "Please select CPU!!!", Toast.LENGTH_SHORT).show();
        } else if (spinnerSSD.getSelectedItem().equals("Select SSD")) {
            Toast.makeText(MainActivity.this, "Please select SSD capacity!!!", Toast.LENGTH_SHORT).show();
        } else if (spinnerHDD.getSelectedItem().equals("Select HDD")) {
            Toast.makeText(MainActivity.this, "Please select HDD capacity!!!", Toast.LENGTH_SHORT).show();
        } else if (spinnerGpu.getSelectedItem().equals("Select GPU")) {
            Toast.makeText(MainActivity.this, "Please select GPU!!!", Toast.LENGTH_SHORT).show();
        } else if (spinnerOs.getSelectedItem().equals("Select OS")) {
            Toast.makeText(MainActivity.this, "Please select OS!!!", Toast.LENGTH_SHORT).show();
        } else {
            predict();
        }
    }

    private void goToNextPage() {
        Intent intent = new Intent(MainActivity.this,DisplayPage.class);
        intent.putExtra("company",spinnerCompany.getSelectedItem().toString());
        intent.putExtra("company", spinnerCompany.getSelectedItem().toString());
        intent.putExtra("typename", spinnerTypeName.getSelectedItem().toString());
        intent.putExtra("ram", spinnerRam.getSelectedItem().toString().trim());
        intent.putExtra("weight", inputWeight.getText().toString().trim());
        intent.putExtra("touchscreen", spinnerTouchScreen.getSelectedItem().toString().trim());
        intent.putExtra("ips", spinnerIps.getSelectedItem().toString().trim());
        intent.putExtra("screen_size", spinnerScreenSize.getSelectedItem().toString().trim());
        intent.putExtra("resolution", spinnerScreenResolution.getSelectedItem().toString().trim());
        intent.putExtra("cpubrand", spinnerCpu.getSelectedItem().toString().trim());
        intent.putExtra("ssd", spinnerSSD.getSelectedItem().toString().trim());
        intent.putExtra("hdd", spinnerHDD.getSelectedItem().toString().trim());
        intent.putExtra("gpubrand", spinnerGpu.getSelectedItem().toString().trim());
        intent.putExtra("os", spinnerOs.getSelectedItem().toString().trim());
        intent.putExtra("pp",predictedPrice);
        startActivity(intent);
    }

    private void initUI() {
        showPrice = findViewById(R.id.showPrice);
        btnPredict = findViewById(R.id.btnPredict);
        inputWeight = findViewById(R.id.inputWeight);
        spinnerCompany = findViewById(R.id.spinnerCompany);
        spinnerTypeName = findViewById(R.id.spinnerTypeName);
        spinnerRam = findViewById(R.id.spinnerRam);
        spinnerTouchScreen = findViewById(R.id.spinnerTouchScreen);
        spinnerIps = findViewById(R.id.spinnerIps);
        spinnerScreenSize = findViewById(R.id.spinnerScreenSize);
        spinnerScreenResolution = findViewById(R.id.spinnerScreenResolution);
        spinnerCpu = findViewById(R.id.spinnerCpu);
        spinnerSSD = findViewById(R.id.spinnerSSD);
        spinnerHDD = findViewById(R.id.spinnerHDD);
        spinnerGpu = findViewById(R.id.spinnerGpu);
        spinnerOs = findViewById(R.id.spinnerOs);
    }

    private void setupSpinners() {
        List<String> companies = Arrays.asList("Select Brand","Dell", "Lenovo", "HP", "Asus", "Acer", "MSI", "Toshiba", "Apple", "Samsung", "Razer", "Mediacom", "Microsoft", "Xiaomi", "Vero", "Chuwi", "Google", "Fujitsu", "LG", "Huawei");
        List<String> types = Arrays.asList("Select Type","Notebook", "Gaming", "Ultrabook", "2 in 1 Convertible", "Workstation", "Netbook");
        List<String> ramOptions = Arrays.asList("Select Ram","2", "4", "6", "8", "12", "16", "24","32");
        List<String> touchScreenOptions = Arrays.asList("TouchScreen","No", "Yes");
        List<String> ipsOptions = Arrays.asList("IPS", "No", "Yes");
        List<String> screenSizeOptions = Arrays.asList("ScreenSize","10.0", "11.0", "12.0", "13.0", "14.0", "15.0", "16.0", "17.0", "18.0");
        List<String> screenResolutionOptions = Arrays.asList("ScreenResolution","1920x1080", "1366x768", "1600x900", "3840x2160", "3200x1800", "2880x1800", "2560x1600", "2560x1440", "2304x1440");
        List<String> cpuOptions = Arrays.asList("Select CPU","Intel Core i3", "Intel Core i5", "Intel Core i7", "AMD Processor", "Other Intel Processor");
        List<String> ssdOptions = Arrays.asList("Select SSD","0", "8", "128", "256", "512", "1024", "2048");
        List<String> hddOptions = Arrays.asList("Select HDD","0", "128", "256", "512", "1024", "2048");
        List<String> gpuOptions = Arrays.asList("Select GPU","Intel", "AMD", "Nvidia");
        List<String> osOptions = Arrays.asList("Select OS","Windows", "Others/No OS/Linux", "Mac");

        setSpinnerAdapter(spinnerCompany, companies);
        setSpinnerAdapter(spinnerTypeName, types);
        setSpinnerAdapter(spinnerRam, ramOptions);
        setSpinnerAdapter(spinnerTouchScreen, touchScreenOptions);
        setSpinnerAdapter(spinnerIps, ipsOptions);
        setSpinnerAdapter(spinnerScreenSize, screenSizeOptions);
        setSpinnerAdapter(spinnerScreenResolution, screenResolutionOptions);
        setSpinnerAdapter(spinnerCpu, cpuOptions);
        setSpinnerAdapter(spinnerSSD, ssdOptions);
        setSpinnerAdapter(spinnerHDD, hddOptions);
        setSpinnerAdapter(spinnerGpu, gpuOptions);
        setSpinnerAdapter(spinnerOs, osOptions);
    }

    private void setSpinnerAdapter(Spinner spinner, List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.my_list_item, data);
        adapter.setDropDownViewResource(R.layout.my_list_item);
        spinner.setAdapter(adapter);
    }

    private void predict() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        predictedPrice = jsonObject.getString("Predicted Price");
                        goToNextPage();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("company", spinnerCompany.getSelectedItem().toString());
                params.put("typename", spinnerTypeName.getSelectedItem().toString());
                params.put("ram", spinnerRam.getSelectedItem().toString().trim());
                params.put("weight", inputWeight.getText().toString().trim());
                params.put("touchscreen", spinnerTouchScreen.getSelectedItem().toString().trim());
                params.put("ips", spinnerIps.getSelectedItem().toString().trim());
                params.put("screen_size", spinnerScreenSize.getSelectedItem().toString().trim());
                params.put("resolution", spinnerScreenResolution.getSelectedItem().toString().trim());
                params.put("cpubrand", spinnerCpu.getSelectedItem().toString().trim());
                params.put("ssd", spinnerSSD.getSelectedItem().toString().trim());
                params.put("hdd", spinnerHDD.getSelectedItem().toString().trim());
                params.put("gpubrand", spinnerGpu.getSelectedItem().toString().trim());
                params.put("os", spinnerOs.getSelectedItem().toString().trim());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(stringRequest);
    }
}
