package com.example.rallyup.progressBar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.rallyup.R;

/**
 * This class if for a dialog that allows the user to manage and edit their milestones
 */
public class ManageMilestoneDialog extends DialogFragment {
    // Simple dialog that will have 4 buttons,
    // each will take the user to another dialog/activity, whichever may be
    // that will allow them to edit their milestones
    // We will only have a close button at the bottom

    /**
     * Initializes the dialog view along with all of the necessary buttons and their listeners
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the dialog view on creation
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_managemilestones, container, false);
        // Sets the background to transparent so our layout background can be applicable and
        // not have awkward white square corners poking out of the rounded corners
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RadioButton manageMilestoneOne = view.findViewById(R.id.ManageMilestonesRadioButtonOne);
        RadioButton manageMilestoneTwo = view.findViewById(R.id.ManageMilestonesRadioButtonTwo);
        RadioButton manageMilestoneThree = view.findViewById(R.id.ManageMilestonesRadioButtonThree);
        RadioButton manageMilestoneFour = view.findViewById(R.id.ManageMilestonesRadioButtonFour);
        Button closeButton = view.findViewById(R.id.ManageMilestonesCloseButton);

        manageMilestoneOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Something something happening here for your new activity
            }
        });

        manageMilestoneTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Something something happening here for your new activity
            }
        });

        manageMilestoneThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Something something happening here for your new activity
            }
        });

        manageMilestoneFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Something something happening here for your new activity
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
