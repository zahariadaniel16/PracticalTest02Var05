package ro.pub.cs.systems.eim.practicaltest02var05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02Var05MainActivity extends AppCompatActivity {

    private Button connectButton, putButton, getButton;
    private EditText port, putEditText, getEditText;
    private EditText getWork, putWork;
    private ServerThread serverThread;
    private ClientThread clientThread;

    private TextView workResult;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();

    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = port.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private PutButtonClickListener putButtonClickListener = new PutButtonClickListener();

    private class PutButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            String clientPort = port.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            String getEditText = getWork.getText().toString();
            String putEditText = putWork.getText().toString();

            if ((getEditText == null || getEditText.isEmpty())
                    && (putEditText == null || putEditText.isEmpty())) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client should be filled", Toast.LENGTH_SHORT).show();
                return;
            }


            String key = null;
            String value = null;
            if (getEditText != null && !getEditText.isEmpty()) {
                key = getEditText;
                value = "";
            } else {


                String values[] = putEditText.split(",");
                Log.e(Constants.TAG, values.toString());
                key = values[0];
                value = values[1];
            }

            workResult.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), key, value, workResult
            );
            clientThread.start();


        }

    }

    private GetButtonClickListener getButtonClickListener = new GetButtonClickListener();

    private class GetButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            String clientPort = port.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }



        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_var05_main);

        connectButton = (Button) findViewById(R.id.connect_button);
        getButton = (Button) findViewById(R.id.get_button);
        putButton = (Button) findViewById(R.id.put_button);

        port = (EditText) findViewById(R.id.server_port_edit_text);

        connectButton.setOnClickListener(connectButtonClickListener);
        putButton.setOnClickListener(putButtonClickListener);
        getButton.setOnClickListener(getButtonClickListener);

        putEditText = (EditText) findViewById(R.id.put_edit_text);
        getEditText = (EditText) findViewById(R.id.get_edit_text);

        
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();

    }
}
