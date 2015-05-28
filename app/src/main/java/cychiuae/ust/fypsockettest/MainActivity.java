package cychiuae.ust.fypsockettest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView serverTxt, portTxt;
    private Button connectBtn, clearBtn;

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverTxt = (TextView) findViewById(R.id.serverTxt);

        portTxt = (TextView) findViewById(R.id.portTxt);

        connectBtn = (Button) findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(this);

        clearBtn = (Button) findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(this);

        client = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.connectBtn) {
            final String serverAddress = serverTxt.getText().toString(),
                    portText = portTxt.getText().toString();

            if (serverAddress.isEmpty() || portText.isEmpty()) {
                Toast.makeText(this, "Please fill in info", Toast.LENGTH_SHORT).show();
            } else {
                new Thread() {
                    public void run() {
                        new Connect().execute(serverAddress, portText);
                    }
                }.start();
            }

        } else if (v.getId() == R.id.clearBtn) {
            serverTxt.setText("");
            portTxt.setText("");
        }
    }

    class Connect extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            client = new Client(params[0], Integer.parseInt(params[1]));
            client.connect();
            Log.d("client", "connecting");
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.d("client", "processing");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("client", "fin");
            if (client.isConnect()) {
                Toast.makeText(MainActivity.this, "Connect", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


