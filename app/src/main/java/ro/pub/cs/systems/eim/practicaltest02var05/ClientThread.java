package ro.pub.cs.systems.eim.practicaltest02var05;

/**
 * Created by daniel on 5/19/17.
 */

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientThread extends Thread {

    private String address;
    private int port;
    private String key;
    private String value;
    private String workType;
    private TextView sendWorkButton;

    private Socket socket;

    public ClientThread(String address, int port, String workType, String key, String value, TextView sendWorkButton) {
        this.address = address;
        this.port = port;
        this.workType = workType;
        this.key = key;
        this.value = value;
        this.sendWorkButton = sendWorkButton;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }


            printWriter.println(workType);
            printWriter.flush();
            printWriter.println(key);
            printWriter.flush();
            printWriter.println(value);
            printWriter.flush();

            String workResult;
            while ((workResult = bufferedReader.readLine()) != null) {
                final String finalizedWorkResult = workResult;
                sendWorkButton.post(new Runnable() {
                    @Override
                    public void run() {
                        sendWorkButton.setText(finalizedWorkResult);
                    }
                });
            }


        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}