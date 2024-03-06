package br.otaviof.esp32manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import br.otaviof.esp32manager.fragment.ClockFragment;
import br.otaviof.esp32manager.fragment.ImageFragment;
import br.otaviof.esp32manager.fragment.TextFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "Main Activity";
    private final ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewHolder.fragmentContainer = findViewById(R.id.fragment_placeholder);

        mViewHolder.button = findViewById(R.id.button_done);
        mViewHolder.button.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.main_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mViewHolder.spinner = findViewById(R.id.spinner_option);
        mViewHolder.spinner.setAdapter(adapter);
        mViewHolder.spinner.setOnItemSelectedListener(this);
    }

    /***
     * When the button is clicked, performs different functions according to the selector
     * If it's not button_done that was clicked, the function does nothing
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.button_done) //check that the clicked thing is the done_button
            return; //do nothing if it's not

        ViewModelProvider vmp = new ViewModelProvider(this);
        switch (mViewHolder.spinner.getSelectedItemPosition()) {
            case 1: //digiclock
                sendClock();
                break;
            case 2: // free text
                sendText();
                break;
            case 3: // image
                sendImage();
                break;
            default:
                Log.w(MainActivity.TAG, "No data");
                break;
        }
    }

    /***
     * Changes the bottom fragment when the selection changes
     * @param parent The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int pos = mViewHolder.spinner.getSelectedItemPosition();
        Log.w(MainActivity.TAG, "Selected position: " + pos);
        mViewHolder.fragmentContainer.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        switch (pos) {
            case 1: //digiclock
                ft.add(R.id.fragment_placeholder, ClockFragment.class, null).commit();
                break;
            case 2: //free text
                ft.add(R.id.fragment_placeholder, TextFragment.class, null).commit();
                break;
            case 3: //image
                ft.add(R.id.fragment_placeholder, ImageFragment.class, null).commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /***
     * Sends a command for the ESP32 to display a digital clock
     */
    private void sendClock() {
        CheckBox date = mViewHolder.fragmentContainer.findViewById(R.id.checkbox_date);
        CheckBox isAMPM = mViewHolder.fragmentContainer.findViewById(R.id.checkbox_24hour);
        Log.i(MainActivity.TAG, "\"type\":\"clock\"" +
                ",\"date\":" + (date.isChecked() ? "true" : "false") +
                ",\"ampm\":" + (isAMPM.isChecked() ? "true" : "false")
        );
    }

    /***
     * Sends a command for the paired ESP32 to display text
     */
    private void sendText() {
        EditText text = mViewHolder.fragmentContainer.findViewById(R.id.edittext_content);
        Log.i(MainActivity.TAG, "\"type\":\"text\"" +
                ",\"content\":" + "\"" + text.getText().toString() + "\"");
    }

    /***
     * Sends a command for the paired ESP32 to display an bw image
     */
    private void sendImage() {
        //TODO: Fetch image
        //TODO: Resize and convert image to bw
        //TODO: Send image data as JSON
        Log.i(MainActivity.TAG, "Method to send image not implemented.");
    }

    private static class ViewHolder {
        public Spinner spinner;
        public FragmentContainerView fragmentContainer;
        public Button button;

    }
}