package com.example.laptoppricepredictor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.SmartReplySuggestion;
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult;
import com.google.mlkit.nl.smartreply.TextMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayPage extends AppCompatActivity {

    private GridView gridView, staticGridView;
    private List<TextMessage> conversation;
    private TextView dispPP;
    private AppCompatButton openGoogle, receiveLaptop;
    private String pp;
    private Intent intent;
    private SmartReplyGenerator smartReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        smartReply = SmartReply.getClient();
        gridView = findViewById(R.id.gridView);
        receiveLaptop = findViewById(R.id.receiveLaptop);
        openGoogle = findViewById(R.id.openGoogleButton);
        staticGridView = findViewById(R.id.staticGridView);
        dispPP = findViewById(R.id.dispPP);
        intent = getIntent();
        conversation = new ArrayList<>();

        List<String> staticItems = Arrays.asList("Specifications", "Your Input");

        List<String> specsName = Arrays.asList(
                "Brand", "Type", "Ram", "Weight", "TouchScreen", "IPS",
                "ScreenSize", "ScreenResolution", "CPU", "SSD",
                "HDD", "GPU", "OS"
        );

        List<String> detailsList = Arrays.asList(
                intent.getStringExtra("company"),
                intent.getStringExtra("typename"),
                intent.getStringExtra("ram") + "GB",
                intent.getStringExtra("weight") + "kgs",
                intent.getStringExtra("touchscreen"),
                intent.getStringExtra("ips"),
                intent.getStringExtra("screen_size"),
                intent.getStringExtra("resolution"),
                intent.getStringExtra("cpubrand"),
                intent.getStringExtra("ssd") + "GB",
                intent.getStringExtra("hdd") + "GB",
                intent.getStringExtra("gpubrand"),
                intent.getStringExtra("os")
        );

        pp = intent.getStringExtra("pp");
        dispPP.setText("Predicted Price : " + pp);

        List<String> items = new ArrayList<>();
        for (int i = 0; i < specsName.size(); i++) {
            items.add(specsName.get(i));
            items.add(detailsList.get(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.display_list_item, items);
        gridView.setNumColumns(2);
        gridView.setAdapter(adapter);

        ArrayAdapter<String> staticAdapter = new ArrayAdapter<>(this, R.layout.display_title, staticItems);
        staticGridView.setAdapter(staticAdapter);

        openGoogle.setOnClickListener(v -> showLaptop());
        receiveLaptop.setOnClickListener(v -> showLaptop1());
    }

    private void showLaptop1() {
        try {
            String newText = "";  // Replace this with the actual input if needed.
            conversation.add(TextMessage.createForLocalUser(newText.trim(), System.currentTimeMillis()));
        } catch (Exception e) {
            Log.e("ConversationError", "Error adding text message: " + e.getMessage());
        }

        smartReply.suggestReplies(conversation).addOnSuccessListener(smartReplySuggestionResult -> {
            for (SmartReplySuggestion suggestion : smartReplySuggestionResult.getSuggestions()) {
                Toast.makeText(DisplayPage.this, suggestion.getText(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(DisplayPage.this, "Error while receiving message", Toast.LENGTH_SHORT).show()
        );
    }

    private void showLaptop() {
        String company = intent.getStringExtra("company");
        String type = intent.getStringExtra("typename");

        int ppValue = Integer.parseInt(pp);
        String result;

        if (ppValue >= 30000 && ppValue <= 120000) {
            result = "under " + ((ppValue / 5000 + 1) * 5000);
        } else {
            result = "out of range";
        }

        Uri uri = Uri.parse("https://www.flipkart.com/search?q=" + Uri.encode(company) + " " + Uri.encode(type) + " Laptops " + result);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
