package edu.uw.facebook.readyseteat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.clarifai.api.exception.ClarifaiException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

public class ImageRecognitionActivity extends AppCompatActivity {

    private static final String TAG = ImageRecognitionActivity.class.getSimpleName();
    private static final String CLIENT_ID = "u6glCrkLLNr2KYtlRcBYtRsYrgh_N2bN2VM4ZxBU";
    private static final String CLIENT_SECRET = "BglrMukQBcM7S4zbIq25uJBMZ42bYHoNd3vpIU8e";

    /**Represents bad foods.*/
    public final static int RED_LIGHT = 0;
    /**Represents ok foods.*/
    public final static int YEllOW_LIGHT = 1;
    /**Represents good foods.*/
    public final static int GREEN_LIGHT = 2;
    /**Array of keywords that good foods usually are associated with.*/
    public final static String[] GREEN = {"salad", "green"};
    /**Array of keywords that bad foods usually are associated with.*/
    public final static String[]  RED= {"Beef", "red", "tomato", "food"};
    /**Array of keywords that ok foods usually are associated with.*/
    public final static String[] YELLOW = {"rice", "white"};

    private static final int CODE_PICK = 1;
    private Button selectButton;
    private ImageView imageView;
    private TextView textView;

    private final ClarifaiClient client = new ClarifaiClient(CLIENT_ID, CLIENT_SECRET);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_recognition);

        imageView = (ImageView) findViewById(R.id.image_view);
        textView = (TextView) findViewById(R.id.text_view);
        selectButton = (Button) findViewById(R.id.select_button);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send an intent to launch the media picker.
                final Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CODE_PICK);


            }
        });
    }

    /**Detects the healthyness of the food.
     * @param theTags is the array of tags that we compare to our keywords.
     * @return 0 if a tag from theTags exist in our RED array. 1 if a tag
     * from theTags exist in our YELLOW array. 2 if a tag from theTags exist in our GREEN array. -1 otherwise.*/
    public static final int detect(final String[] theTags) {
        int light = -1;
        final int size = theTags.length;
        String tag;

        for (int i = 0; i < size && light == -1; i++) {
            tag = theTags[i];
            if(isInArray(RED, tag))
                light = RED_LIGHT;
            else if(isInArray(YELLOW, tag))
                light = YEllOW_LIGHT;
            else if(isInArray(GREEN, tag))
                light = GREEN_LIGHT;
        }
        return light;
    }

    /**Searches for string in the array.
     * @param theKeyWords the array of keywords that of a category of foods.
     * @param theTag is the tag that is searched from the array of keywords.
     * @return true if found tag in the keywords array. Otherwise false.*/
    private static final boolean isInArray(final String [] theKeyWords, final String theTag) {
        boolean found = false;
        final int size = theKeyWords.length;
        for(int i = 0; i < size && !found; i++) {
            found = theTag.equalsIgnoreCase(theKeyWords[i]);
        }
        return found;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == CODE_PICK && resultCode == RESULT_OK) {
            // The user picked an image. Send it to Clarifai for recognition.
            Log.d(TAG, "User picked image: " + intent.getData());
            Bitmap bitmap = loadBitmapFromUri(intent.getData());
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                textView.setText("Recognizing...");
                selectButton.setEnabled(false);

                // Run recognition on a background thread since it makes a network call.
                new AsyncTask<Bitmap, Void, RecognitionResult>() {
                    @Override protected RecognitionResult doInBackground(Bitmap... bitmaps) {
                        return recognizeBitmap(bitmaps[0]);
                    }
                    @Override protected void onPostExecute(RecognitionResult result) {
                        updateUIForResult(result);
                    }
                }.execute(bitmap);
            } else {
                textView.setText("Unable to load selected image.");
            }
        }
    }


    /** Loads a Bitmap from a content URI returned by the media picker. */
    private Bitmap loadBitmapFromUri(Uri uri) {
        try {
            // The image may be large. Load an image that is sized for display. This follows best
            // practices from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, opts);
            int sampleSize = 1;
            while (opts.outWidth / (2 * sampleSize) >= imageView.getWidth() &&
                    opts.outHeight / (2 * sampleSize) >= imageView.getHeight()) {
                sampleSize *= 2;
            }

            opts = new BitmapFactory.Options();
            opts.inSampleSize = sampleSize;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, opts);
        } catch (IOException e) {
            Log.e(TAG, "Error loading image: " + uri, e);
        }
        return null;
    }

    /** Sends the given bitmap to Clarifai for recognition and returns the result. */
    private RecognitionResult recognizeBitmap(Bitmap bitmap) {
        try {
            // Scale down the image. This step is optional. However, sending large images over the
            // network is slow and  does not significantly improve recognition performance.
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 320,
                    320 * bitmap.getHeight() / bitmap.getWidth(), true);

            // Compress the image as a JPEG.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.JPEG, 90, out);
            byte[] jpeg = out.toByteArray();

            // Send the JPEG to Clarifai and return the result.
            return client.recognize(new RecognitionRequest(jpeg)).get(0);
        } catch (ClarifaiException e) {
            Log.e(TAG, "Clarifai error", e);
            return null;
        }
    }

    /** Updates the UI by displaying tags for the given result. */
    private void updateUIForResult(RecognitionResult result) {
        if (result != null) {
            if (result.getStatusCode() == RecognitionResult.StatusCode.OK) {
                // Display the list of tags in the UI.
                StringBuilder b = new StringBuilder();
                final Collection<Tag> theResultTags = result.getTags();
                String theTagsArray[] = new String[theResultTags.size()];
                int i = 0;
                for (Tag tag : theResultTags) {
                    b.append(b.length() > 0 ? ", " : "").append(tag.getName());
                    theTagsArray[i] = tag.getName();
                    i++;
                }
                textView.setText("Tags:\n" + b); //output tag
                int light = detect(theTagsArray);
                if(light == 0) {

                    Intent intent = new Intent(ImageRecognitionActivity.this, RedActivity.class);
                    startActivity(intent);
                }
                //yellow
                else if(light == 1) {
                    Intent intent = new Intent(ImageRecognitionActivity.this, YellowActivity.class);
                    startActivity(intent);

                }
                //green
                else if(light == 2) {
                    Intent intent = new Intent(ImageRecognitionActivity.this, GreenActivity.class);
                    startActivity(intent);
                }


            } else {
                Log.e(TAG, "Clarifai: " + result.getStatusMessage());
                textView.setText("Sorry, there was an error recognizing your image.");
            }
        } else {
            textView.setText("Sorry, there was an error recognizing your image.");
        }
        selectButton.setEnabled(true);
    }

}

