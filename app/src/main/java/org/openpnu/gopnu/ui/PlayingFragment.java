package org.openpnu.gopnu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.openpnu.gopnu.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlayingFragment extends Fragment implements View.OnClickListener {

  private TextView mTitleView;
  private Button mStepButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_playing, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mTitleView = view.findViewById(R.id.playing_title);
    mStepButton = view.findViewById(R.id.step_button);
    mStepButton.setOnClickListener(this);
  }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.step_button)
        {
            Intent intent=new Intent(getContext(), StepActivity.class);
            startActivity(intent);
        }
    }
}
