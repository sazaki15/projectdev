package android.security.checking;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ListView userListView = findViewById(R.id.userListView);
        Button backButton = findViewById(R.id.backButton);

        // Set a click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the previous activity
                finish(); // Ends the current activity and returns to the previous one
            }
        });
        List<String> userInfoList = new ArrayList<>();
        for (User user : UserManager.getUsers()) {
            userInfoList.add("Username: " + user.getUsername() + "\nEmail: " + user.getEmail());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userInfoList);
        userListView.setAdapter(adapter);
    }
}
