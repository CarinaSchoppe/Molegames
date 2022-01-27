package de.pentagames.maulwurfkompanie.client;

import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import de.pentagames.maulwurfkompanie.ui.LoginActivity;
import upb.maulwurfcompany.library.Message;

public class ServerHandler implements Runnable {
  public final String ip;
  public final int port;
  private final MessageHandler messageHandler;
  private Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;

  public ServerHandler(@NotNull final String ip, final int port, @NotNull final MessageHandler messageHandler) {
    this.ip = ip;
    this.port = port;
    this.messageHandler = messageHandler;
  }

  @Override
  public void run() {
    try {
      this.socket = new Socket();
      socket.connect(new InetSocketAddress(ip, port), 2000);
      // TODO no exception if wrong port
      System.out.println("Connected to server " + ip + " on port " + port);
      this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (SocketTimeoutException e) {
      LoginActivity.getInstance().runOnUiThread(() -> Toast.makeText(LoginActivity.getInstance(), "Server nicht erreichbar!", Toast.LENGTH_LONG).show());
    } catch (IOException e) {
      e.printStackTrace();
      if (this.socket != null) close();
      LoginActivity.getInstance().runOnUiThread(() -> Toast.makeText(LoginActivity.getInstance(), "Error while connecting to server!", Toast.LENGTH_LONG).show());
      return;
    }
    while (socket.isConnected()) {
      try {
        var line = bufferedReader.readLine();
        try {
          messageHandler.offerMessage(Message.fromJson(line));
        } catch (Exception e) {
          System.out.println("error during message decoding");
          e.printStackTrace();
        }
      } catch (IOException e) {
        e.printStackTrace();
        close();
        break;
      }
    }
  }

  public void sendMessage(@NotNull final Message message) {
    new Thread(() -> {
      try {
        System.out.println("send " + Message.getMessageType(message.getClass()));
        this.bufferedWriter.write(message.toJson());
        this.bufferedWriter.newLine();
        this.bufferedWriter.flush();
      } catch (IOException e) {
        close();
        e.printStackTrace();
      }
    }).start();
  }

  public void sendMessages(@NotNull final Message... messages) {
    new Thread(() -> {
      try {
        for(Message message : messages) {
          System.out.println("send " + Message.getMessageType(message.getClass()));
          this.bufferedWriter.write(message.toJson());
          this.bufferedWriter.newLine();
          this.bufferedWriter.flush();
        }
      } catch (IOException e) {
        close();
        e.printStackTrace();
      }
    }).start();
  }

  public void close() {
    try {
      System.out.println("connection closed");
      if (socket != null) socket.close();
      if (bufferedWriter != null) bufferedWriter.close();
      if (bufferedReader != null) bufferedReader.close();
      if (messageHandler != null) messageHandler.close();
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  public boolean isClosed() {
    if (socket != null) return socket.isClosed();
    return true;
  }
}
