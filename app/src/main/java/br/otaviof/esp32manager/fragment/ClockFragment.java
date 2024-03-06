package br.otaviof.esp32manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import br.otaviof.esp32manager.R;

public class ClockFragment extends Fragment {
    private final ViewModel mViewModel = new ViewModel();
    public static ClockFragment newInstance() {
        return new ClockFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.isAMPM = view.findViewById(R.id.checkbox_24hour);
        mViewModel.showDate = view.findViewById(R.id.checkbox_date);
    }

    /***
     * Creates a JSON version of the data
     * @return The data contained on this as a JSON
     */
    @NonNull
    @Override
    public String toString() {
        return "{\"type\":\"clock\"" +
                ",\"ampm\":" +
                (mViewModel.isAMPM.isChecked() ? "true" : "false") +
                ",\"date\":" +
                (mViewModel.showDate.isChecked() ? "true" : "false");
    }

    private static class ViewModel {
        public CheckBox isAMPM;
        public CheckBox showDate;
    }
}