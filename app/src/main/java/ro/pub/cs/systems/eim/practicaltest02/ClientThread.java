package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private final String address;
    private final int port;
    private Socket socket;
    private final String key;
    private final TextView textView;

    public ClientThread(String address, int port, String key,TextView textView) {
        this.address = address;
        this.port = port;
        this.key = key;
        this.textView = textView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);

            BufferedReader bufferedReader = Utils.getReader(socket);
            PrintWriter printWriter = Utils.getWriter(socket);

            String request = key;


            printWriter.println(request);
            printWriter.flush();

            String response;
            while ((response = bufferedReader.readLine()) != null) {
                String finalInformation = response;
                textView.post(() -> textView.setText(finalInformation));
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    ioException.printStackTrace();
                }
            }
        }
    }
}