package de.pentagames.maulwurfkompanie;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import de.pentagames.maulwurfkompanie.client.Client;
import de.pentagames.maulwurfkompanie.ui.LoginActivity;
import upb.maulwurfcompany.library.Message;
import upb.maulwurfcompany.library.data.Player;

public class Utils {

  /**
   * Return the current timestamp
   */
  public static long getTime() {
    return Calendar.getInstance().getTimeInMillis();
  }

  /**
   * Get the current context
   */
  public static Context getContext() {
    return LoginActivity.getInstance();
  }

  /**
   * This method displays a {@link Toast}.
   *
   * @param text      The text you want to show up.
   * @param important Should the {@link Toast} be shown if the visualisation time is < 100?
   */
  public static void makeToast(@NonNull final String text, final boolean important) {
    var vTime = 2000;
    if (Client.messageHandler != null && Client.messageHandler.game != null)
      vTime = Client.messageHandler.game.visualisationTime;
    if (!important && vTime < 100) return;
    if (LoginActivity.getInstance() != null) {
      int finalVTime = vTime;
      LoginActivity.getInstance().runOnUiThread(() -> Toast.makeText(LoginActivity.getInstance(), text, important ? Toast.LENGTH_SHORT : finalVTime).show());
    }
  }

  /**
   * This method creates a copy of the array with the new element added.
   *
   * @param array  The array to edit.
   * @param object The object to add.
   * @param <T>    The class of your arrays objects.
   * @return The new array with added object.
   */
  public static <T> T[] arrayPush(@NonNull final T[] array, @NonNull final T object) {
    var list = new ArrayList<>(Arrays.asList(array));
    list.add(object);
    return list.toArray(Arrays.copyOf(array, array.length + 1));
  }
  // TODO test

  /**
   * This method creates a copy of the array with the first occurrence of the element removed.
   *
   * @param array  The array to edit.
   * @param object The object to remove.
   * @param <T>    The class of your arrays objects.
   * @return The new array without the first occurrence of the element
   */
  public static <T> T[] arrayRemove(@NonNull final T[] array, @NonNull final T object) {
    var list = new ArrayList<>(Arrays.asList(array));
    list.remove(object);
    return list.toArray(Arrays.copyOf(array, array.length - 1));
  }

  /**
   * This method creates a copy of the array with the first occurrence of the element removed.
   *
   * @param players The array to edit.
   * @param player  The object to remove.
   * @return The new array without the first occurrence of the element.
   */
  public static Player[] arrayRemovePlayer(@NonNull final Player[] players, @NonNull final Player player) {
    var list = new ArrayList<>(Arrays.asList(players));
    for (var p : players) if (p.clientID == player.clientID) list.remove(p);
    return list.toArray(Arrays.copyOf(players, players.length - 1));
  }

  /**
   * This method determines whether the array contains a given element.
   *
   * @param array  The array to analyze.
   * @param object The object to search for.
   * @return true if object is found.
   */
  public static boolean arrayContains(@NonNull final Object[] array, @NonNull final Object object) {
    for (var o : array)
      if (o == object)
        return true;
    return false;
  }

  /**
   * This method converts an int array to an Integer array
   *
   * @param array the int array to convert.
   * @return The array of {@link Integer} objects.
   */
  public static Integer[] intToIntegerArray(int[] array) {
    Integer[] array2 = new Integer[array.length];
    for (int i = 0; i < array.length; i++) {
      array2[i] = array[i];
    }
    return array2;
  }

  /**
   * This method converts an Integer array to an int array.
   *
   * @param array The {@link Integer} array to be converted.
   * @return The converted int array.
   */
  public static int[] integerToIntArray(Integer[] array) {
    int[] array2 = new int[array.length];
    for (int i = 0; i < array.length; i++) {
      array2[i] = array[i];
    }
    return array2;
  }

  /**
   * This method determines whether a player mentioned in a {@link Message} is the user.
   *
   * @param pPlayer The Player mentioned.
   * @return true if the user is the player referred to in the {@link Message}.
   */
  public static boolean isThatUs(Player pPlayer) {
    return pPlayer.clientID == Client.clientId;
  }
}
