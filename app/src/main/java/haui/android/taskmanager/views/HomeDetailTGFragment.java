package haui.android.taskmanager.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import haui.android.taskmanager.R;
import haui.android.taskmanager.models.HomeListTag;
public class HomeDetailTGFragment extends Fragment {

    private RecyclerView rvcHomeListTag;
    HomeListTag homeListTag;

    HomeDetailTGAdapter homeDetailTGAdapter;
    TextView home_text_ten_nhom_cv;

    public HomeDetailTGFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home_detail_t_g, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvcHomeListTag = view.findViewById(R.id.rcv_hometaglist);
        home_text_ten_nhom_cv = view.findViewById(R.id.home_ten_nhom_cv_hometaglist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvcHomeListTag.setLayoutManager(linearLayoutManager);

        Bundle bundleReceive = getArguments();
        if(bundleReceive != null){
            homeListTag = (HomeListTag) bundleReceive.get("object_hometaglist");
            if(homeListTag != null){
                home_text_ten_nhom_cv.setText(homeListTag.getTaskDetailList().get(0).getTag().getTagName());
                homeDetailTGAdapter = new HomeDetailTGAdapter(homeListTag.getTaskDetailList());
                rvcHomeListTag.setAdapter(homeDetailTGAdapter);
            }
        }
    }
}