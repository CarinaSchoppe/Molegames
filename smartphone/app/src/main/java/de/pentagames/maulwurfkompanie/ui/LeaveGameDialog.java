package de.pentagames.maulwurfkompanie.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import de.pentagames.maulwurfkompanie.R;

/**
 * This class creates a dialog to warn the user about the consequences of him leaving the game.
 */
public class LeaveGameDialog extends AppCompatDialogFragment {

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
    var builder = (AlertDialog.Builder) new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
    var infl = (LayoutInflater) getActivity().getLayoutInflater();
    var view = infl.inflate(R.layout.fragment_leave_game_dialog, null);
    builder.setView(view)
      .setTitle("Warnung")
      .setNegativeButton("verlassen", (dialogInterface, i) -> GameActivity.instance.leaveGame())
      .setPositiveButton("bleiben", (dialogInterface, i) -> {
      });
    return builder.create();
  }
}
