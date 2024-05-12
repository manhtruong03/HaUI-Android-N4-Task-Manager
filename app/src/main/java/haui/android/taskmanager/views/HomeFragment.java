package haui.android.taskmanager.views;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.activity.EdgeToEdge;

import haui.android.taskmanager.R;

public class HomeFragment extends Fragment {

    ProgressBar progressBarRec, progressBarCircle, progressCircular1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Enabling Edge-to-Edge
        if (getActivity() != null) {
            EdgeToEdge.enable(getActivity());
        }

        // Initializing progress bars
        progressBarRec = view.findViewById(R.id.home_pressRectange);
        progressBarCircle = view.findViewById(R.id.home_progress_circular2);
        progressCircular1 = view.findViewById(R.id.home_progress_circular1);

        // Setting progress bar values
        progressBarRec.setMax(10);
        progressBarRec.setProgress(6);
        progressBarCircle.setProgress(7);
        progressBarCircle.setMax(10);
        progressCircular1.setProgress(75);
        progressCircular1.setMax(100);
    }
}
