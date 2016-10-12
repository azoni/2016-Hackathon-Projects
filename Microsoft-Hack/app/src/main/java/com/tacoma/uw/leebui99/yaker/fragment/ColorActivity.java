package com.tacoma.uw.leebui99.yaker.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tacoma.uw.leebui99.yaker.R;
import com.microsoft.projectoxford.speechrecognition.Contract;
import com.microsoft.projectoxford.speechrecognition.DataRecognitionClient;
import com.microsoft.projectoxford.speechrecognition.DataRecognitionClientWithIntent;
import com.microsoft.projectoxford.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.projectoxford.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.projectoxford.speechrecognition.MicrophoneRecognitionClientWithIntent;
import com.microsoft.projectoxford.speechrecognition.RecognitionResult;
import com.microsoft.projectoxford.speechrecognition.RecognitionStatus;
import com.microsoft.projectoxford.speechrecognition.SpeechRecognitionMode;
import com.microsoft.projectoxford.speechrecognition.SpeechRecognitionServiceFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ColorActivity extends AppCompatActivity
        implements
        ISpeechRecognitionServerEvents{

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    int m_waitSeconds = 0;
    DataRecognitionClient m_dataClient = null;
    MicrophoneRecognitionClient m_micClient = null;
    boolean m_isMicrophoneReco;
    SpeechRecognitionMode m_recoMode;
    boolean m_isIntent;
    FinalResponseStatus isReceivedResponse = FinalResponseStatus.NotReceived;

    public enum FinalResponseStatus {NotReceived, OK, Timeout}

    //MY VARIABLES
    String[] myResult = {""};
    int myCorrectCount = 0;
    int myErrorCount = 0;
    boolean gameOver = false;

    ////////////////////////////////////////////

    private TextView mSmallWord;
    private Button bButton;
    private static final String
            url = "http://cssgate.insttech.washington.edu/~leebui99/MyColors.php";
    private ProgressDialog mProgressDialog;

    //   private ArrayList<Generator.MyColor> mList;
    public Generator myGenerator;
    private String mColor;
    private Color color;
    private int mColorId = 0;
    private String TAG = "HELLO";
    private TextView timer;
    private Generator.MyColor c;
    private EditText myScore;

    private final long startTime = 60 * 1000;
    private final long interval = 1 * 1000;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.fragment_container, new MenuFragment()).commit();

        if (getString(R.string.subscription_key).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        }

        // Set the mode and microphone flag to your liking

        // m_recoMode can be SpeechRecognitionMode.ShortPhrase or SpeechRecognitionMode.LongDictation
        m_recoMode = SpeechRecognitionMode.ShortPhrase;
        m_isMicrophoneReco = true;
        m_isIntent = false;

        m_waitSeconds = m_recoMode == SpeechRecognitionMode.ShortPhrase ? 20 : 200;

        mSmallWord = (TextView) findViewById(R.id.smallWord);

        myScore = (EditText) findViewById(R.id.score);
        myScore.setText("0");

        timer = (TextView) findViewById(R.id.timer);

        initializeRecoClient();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        // start the program
        switchOn();
    }


    @Override
    public void onStart() {
        super.onStart();
        //       mList = new ArrayList<Color>();
        myGenerator = new Generator();
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[]{url});

        countDownTimer = new MyCountDownTimer(startTime, interval);
        timer.setText(timer.getText() + String.valueOf(startTime / 1000));
        countDownTimer.start();
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(getContext(), "HELLO", Toast.LENGTH_LONG).show();
            mProgressDialog = ProgressDialog.show(ColorActivity.this, "Wait", "Downloading...");

        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download the list of question, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            mProgressDialog.dismiss();
            //Success to retrieve data from the service
            mColor = result;
            //           result = ColorList.parseColorJSON(mColor, mList);
            //           color = mList.get(mColorId);
            //          myGenerator = new Generator();
            ColorList.parseColorJSON(mColor, myGenerator);

            Log.d(TAG, "is it working");
            Toast.makeText(ColorActivity.this, "HELLO", Toast.LENGTH_LONG).show();
            setNextColor();
        }

    }
    public void setNextColor(){
        c = myGenerator.generate();

        mSmallWord.setText(c.distraction);
        mSmallWord.setTextColor(c.color);
        mColorId++;
    }

    public void timerUp() {

    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            timerUp();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timer.setText("" + millisUntilFinished / 1000);
        }

    }


//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "MainActivities Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.tacoma.uw.leebui99.yaker.fragment/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "MainActivities Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.tacoma.uw.leebui99.yaker.fragment/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }


    public void onPartialResponseReceived(final String response) {
        EditText myEditText = (EditText) findViewById(R.id.editText1);
//        myEditText.append("********* Partial Result *********\n");
//        myEditText.append(response + "\n");
        //Our stuff
        myResult = response.split(" ");
        if(!gameOver) {

            if (myErrorCount > 5) {
                myEditText.append("You said:" + myResult[myCorrectCount] + "\n");


            } else if (myResult.length > myCorrectCount && c.check(myResult[myCorrectCount])) {
                myEditText.append("TRUE \n");
                myCorrectCount++;
                myScore.setText(String.valueOf(myCorrectCount));
                myErrorCount = 0;
                setNextColor();

            } else {
                myEditText.append("PENDING \n");
                myErrorCount++;
            }
        }
    }


    //OUR METHOD

    public void onFinalResponseReceived(final RecognitionResult response) {
        boolean isFinalDicationMessage = m_recoMode == SpeechRecognitionMode.LongDictation &&
                (response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);
        if (m_isMicrophoneReco && ((m_recoMode == SpeechRecognitionMode.ShortPhrase) || isFinalDicationMessage)) {
            // we got the final result, so it we can end the mic reco.  No need to do this
            // for dataReco, since we already called endAudio() on it as soon as we were done
            // sending all the data.
            m_micClient.endMicAndRecognition();
        }

        if ((m_recoMode == SpeechRecognitionMode.ShortPhrase) || isFinalDicationMessage) {
//            Button startButton = (Button) findViewById(R.id.button1);
//            startButton.setEnabled(true);
            this.isReceivedResponse = FinalResponseStatus.OK;
        }

        if (!isFinalDicationMessage) {
            EditText myEditText = (EditText) findViewById(R.id.editText1);
            myEditText.append("***** Final NBEST Results *****\n");
//            for (int i = 0; i < response.Results.length; i++) {
//                myEditText.append(i + " Confidence=" + response.Results[i].Confidence +
//                        " Text=\"" + response.Results[i].DisplayText + "\"\n");
//            }
            myEditText.append("GAME OVER!");
        }
        countDownTimer.cancel();
        myCorrectCount = 0;
        myErrorCount = 0;
        //endGame();
    }

    public void onIntentReceived(final String payload) {
//        EditText myEditText = (EditText) findViewById(R.id.editText1);
//        myEditText.append("********* Final Intent *********\n");
//        myEditText.append(payload + "\n");
    }

    public void onError(final int errorCode, final String response) {
//        Button startButton = (Button) findViewById(R.id.button1);
//        startButton.setEnabled(true);

        EditText myEditText = (EditText) findViewById(R.id.editText1);
        myEditText.append("********* Error Detected *********\n");
        myEditText.append(errorCode + " " + response + "\n");
    }

    /**
     * Invoked when the audio recording state has changed.
     *
     * @param recording The current recording state
     */
    public void onAudioEvent(boolean recording) {
        if (!recording) {
            m_micClient.endMicAndRecognition();
//            Button startButton = (Button) findViewById(R.id.button1);
//            startButton.setEnabled(true);
        }

//        EditText myEditText = (EditText) findViewById(R.id.editText1);
//        myEditText.append("********* Microphone status: " + recording + " *********\n");
    }




    void switchOn() {

        EditText myEditText = (EditText) findViewById(R.id.editText1);
        myEditText.setText("");


        if (m_isMicrophoneReco) {
            // Speech recognition from the microphone.  The microphone is turned on and data from the microphone
            // is sent to the Speech Recognition Service.  A built in Silence Detector
            // is applied to the microphone data before it is sent to the recognition service.
            m_micClient.startMicAndRecognition();
        } else {
            String filename = m_recoMode == SpeechRecognitionMode.ShortPhrase ? "whatstheweatherlike.wav" : "batman.wav";
            RecognitionTask doDataReco = new RecognitionTask(m_dataClient, m_recoMode, filename);
            try {
                doDataReco.execute().get(m_waitSeconds, TimeUnit.SECONDS);
            } catch (Exception e) {
                doDataReco.cancel(true);
                isReceivedResponse = FinalResponseStatus.Timeout;
            }
        }


    }

    void endGame(){

        final Context appContext = this;

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(appContext).create();
        alertDialog.setTitle("Final Response");
        EditText myEditText = (EditText) findViewById(R.id.editText1);

        if (m_micClient != null) {
            while (isReceivedResponse == FinalResponseStatus.NotReceived) {
            }
            m_micClient.endMicAndRecognition();
            String msg = isReceivedResponse == FinalResponseStatus.OK ? "See TextBox below for response.  App Done" : "Timed out.  App Done";
            alertDialog.setMessage(msg);
            try {
                m_micClient.finalize();
            } catch (Throwable e) {
                myEditText.append(e + "\n");
            }
        } else if (m_dataClient != null) {
            String msg = isReceivedResponse == FinalResponseStatus.OK ? "See TextBox below for response.  App Done" : "Timed out.  App Done";
            alertDialog.setMessage(msg);
            try {
                m_dataClient.finalize();
            } catch (Throwable e) {
                myEditText.append(e + "\n");
            }
        } else {
            alertDialog.setMessage("Press Start first please!");
        }
        alertDialog.show();

        //Jump back
        //TODO
    }


    //--------------------------------------------------------------------------------------------//


    private class RecognitionTask extends AsyncTask<Void, Void, Void> {
        DataRecognitionClient dataClient;
        SpeechRecognitionMode recoMode;
        String filename;

        RecognitionTask(DataRecognitionClient dataClient, SpeechRecognitionMode recoMode, String filename) {
            this.dataClient = dataClient;
            this.recoMode = recoMode;
            this.filename = filename;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Note for wave files, we can just send data from the file right to the server.
                // In the case you are not an audio file in wave format, and instead you have just
                // raw data (for example audio coming over bluetooth), then before sending up any
                // audio data, you must first send up an SpeechAudioFormat descriptor to describe
                // the layout and format of your raw audio data via DataRecognitionClient's sendAudioFormat() method.
                // String filename = recoMode == SpeechRecognitionMode.ShortPhrase ? "whatstheweatherlike.wav" : "batman.wav";
                InputStream fileStream = getAssets().open(filename);
                int bytesRead = 0;
                byte[] buffer = new byte[1024];

                do {
                    // Get  Audio data to send into byte buffer.
                    bytesRead = fileStream.read(buffer);

                    if (bytesRead > -1) {
                        // Send of audio data to service.
                        dataClient.sendAudio(buffer, bytesRead);
                    }
                } while (bytesRead > 0);
            } catch (IOException ex) {
                Contract.fail();
            } finally {
                dataClient.endAudio();
            }

            return null;
        }
    }

    void initializeRecoClient() {
        String language = "en-us";

        String subscriptionKey = this.getString(R.string.subscription_key);
        String luisAppID = this.getString(R.string.luisAppID);
        String luisSubscriptionID = this.getString(R.string.luisSubscriptionID);

        if (m_isMicrophoneReco && null == m_micClient) {
            if (!m_isIntent) {
                m_micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(this,
                        m_recoMode,
                        language,
                        this,
                        subscriptionKey);
            } else {
                MicrophoneRecognitionClientWithIntent intentMicClient;
                intentMicClient = SpeechRecognitionServiceFactory.createMicrophoneClientWithIntent(this,
                        language,
                        this,
                        subscriptionKey,
                        luisAppID,
                        luisSubscriptionID);
                m_micClient = intentMicClient;

            }
        } else if (!m_isMicrophoneReco && null == m_dataClient) {
            if (!m_isIntent) {
                m_dataClient = SpeechRecognitionServiceFactory.createDataClient(this,
                        m_recoMode,
                        language,
                        this,
                        subscriptionKey);
            } else {
                DataRecognitionClientWithIntent intentDataClient;
                intentDataClient = SpeechRecognitionServiceFactory.createDataClientWithIntent(this,
                        language,
                        this,
                        subscriptionKey,
                        luisAppID,
                        luisSubscriptionID);
                m_dataClient = intentDataClient;
            }
        }
    }


}
