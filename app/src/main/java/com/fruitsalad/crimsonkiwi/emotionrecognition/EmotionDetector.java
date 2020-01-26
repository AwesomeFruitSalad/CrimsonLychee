package com.fruitsalad.crimsonkiwi.emotionrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.affectiva.android.affdex.sdk.detector.PhotoDetector;
import com.fruitsalad.crimsonkiwi.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EmotionDetector extends AppCompatActivity implements Detector.ImageListener {

    public static final String LOG_TAG = "Affectiva";
    private String[] metricScoreTextViews;

    PhotoDetector detector;
    Bitmap bitmap = null;
    Frame.BitmapFrame frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_detector);

        Log.i(LOG_TAG, "onCreate()");
        metricScoreTextViews = new String[100];
        loadInitialImage();
    }

    private void loadInitialImage() {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.frawn);
        }
        setAndProcessBitmap(Frame.ROTATE.NO_ROTATION, false);
    }

    void startDetector() {
        if (!detector.isRunning()) {
            detector.start();
        }
    }

    void stopDetector() {
        if (detector.isRunning()) {
            detector.stop();
        }
    }

    public Bitmap getBitmapFromAsset(Context context, String filePath) throws IOException {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap;
        istr = assetManager.open(filePath);
        bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }

    public Bitmap getBitmapFromUri(Uri uri) throws FileNotFoundException {
        InputStream istr;
        Bitmap bitmap;
        istr = getContentResolver().openInputStream(uri);
        bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }

    void setAndProcessBitmap(Frame.ROTATE rotation, boolean isExpectingFaceDetection) {
        if (bitmap == null) {
            return;
        }

        switch (rotation) {
            case BY_90_CCW:
                bitmap = Frame.rotateImage(bitmap,-90);
                break;
            case BY_90_CW:
                bitmap = Frame.rotateImage(bitmap,90);
                break;
            case BY_180:
                bitmap = Frame.rotateImage(bitmap,180);
                break;
            default:
                //keep bitmap as it is
        }

        frame = new Frame.BitmapFrame(bitmap, Frame.COLOR_FORMAT.UNKNOWN_TYPE);

        detector = new PhotoDetector(this,1, Detector.FaceDetectorMode.LARGE_FACES );
        detector.setDetectAllEmotions(true);
        detector.setDetectAllExpressions(true);
        detector.setDetectAllAppearances(true);
        detector.setImageListener(this);

        startDetector();
        detector.process(frame);
        stopDetector();

    }

    @Override
    public void onImageResults(List<Face> faces, Frame image, float timestamp) {

        PointF[] points = null;

        if (faces != null && faces.size() > 0) {
            Face face = faces.get(0);
            setMetricTextViewText(face);
            points = face.getFacePoints();
        } else {
            for (int n = 0; n < MetricsManager.getTotalNumMetrics(); n++) {
                metricScoreTextViews[n] = "---";
            }
        }

        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.RED);
    }

    private void setMetricTextViewText(Face face) {
        // set the text for all the numeric metrics (scored or measured)
        for (int n = 0; n < MetricsManager.getTotalNumNumericMetrics(); n++) {
            metricScoreTextViews[n] = String.format("%.3f", getScore(n, face));
            Log.i("Metric value", metricScoreTextViews[n]);
        }

        // set the text for the appearance metrics
        String textValue="";
        switch (face.appearance.getGender()) {
            case UNKNOWN:
                textValue = "unknown";
                break;
            case FEMALE:
                textValue = "female";
                break;
            case MALE:
                textValue = "male";
                break;
        }
        metricScoreTextViews[MetricsManager.GENDER] = (textValue);

        switch (face.appearance.getAge()) {
            case AGE_UNKNOWN:
                textValue = "unknown";
                break;
            case AGE_UNDER_18:
                textValue = "under 18";
                break;
            case AGE_18_24:
                textValue = "18-24";
                break;
            case AGE_25_34:
                textValue = "25-34";
                break;
            case AGE_35_44:
                textValue = "35-44";
                break;
            case AGE_45_54:
                textValue = "45-54";
                break;
            case AGE_55_64:
                textValue = "55-64";
                break;
            case AGE_65_PLUS:
                textValue = "65+";
                break;
        }
        metricScoreTextViews[MetricsManager.AGE] = (textValue);

        switch (face.appearance.getEthnicity()) {
            case UNKNOWN:
                textValue = "unknown";
                break;
            case CAUCASIAN:
                textValue = "caucasian";
                break;
            case BLACK_AFRICAN:
                textValue = "black african";
                break;
            case EAST_ASIAN:
                textValue = "east asian";
                break;
            case SOUTH_ASIAN:
                textValue = "south asian";
                break;
            case HISPANIC:
                textValue = "hispanic";
                break;
        }
        metricScoreTextViews[MetricsManager.ETHNICITY] = (textValue);
    }

    float getScore(int metricCode, Face face) {

        float score;

        switch (metricCode) {
            case MetricsManager.ANGER:
                score = face.emotions.getAnger();
                break;
            case MetricsManager.CONTEMPT:
                score = face.emotions.getContempt();
                break;
            case MetricsManager.DISGUST:
                score = face.emotions.getDisgust();
                break;
            case MetricsManager.FEAR:
                score = face.emotions.getFear();
                break;
            case MetricsManager.JOY:
                score = face.emotions.getJoy();
                break;
            case MetricsManager.SADNESS:
                score = face.emotions.getSadness();
                break;
            case MetricsManager.SURPRISE:
                score = face.emotions.getSurprise();
                break;
            case MetricsManager.ATTENTION:
                score = face.expressions.getAttention();
                break;
            case MetricsManager.BROW_FURROW:
                score = face.expressions.getBrowFurrow();
                break;
            case MetricsManager.BROW_RAISE:
                score = face.expressions.getBrowRaise();
                break;
            case MetricsManager.CHEEK_RAISE:
                score = face.expressions.getCheekRaise();
                break;
            case MetricsManager.CHIN_RAISE:
                score = face.expressions.getChinRaise();
                break;
            case MetricsManager.DIMPLER:
                score = face.expressions.getDimpler();
                break;
            case MetricsManager.ENGAGEMENT:
                score = face.emotions.getEngagement();
                break;
            case MetricsManager.EYE_CLOSURE:
                score = face.expressions.getEyeClosure();
                break;
            case MetricsManager.EYE_WIDEN:
                score = face.expressions.getEyeWiden();
                break;
            case MetricsManager.INNER_BROW_RAISE:
                score = face.expressions.getInnerBrowRaise();
                break;
            case MetricsManager.JAW_DROP:
                score = face.expressions.getJawDrop();
                break;
            case MetricsManager.LID_TIGHTEN:
                score = face.expressions.getLidTighten();
                break;
            case MetricsManager.LIP_DEPRESSOR:
                score = face.expressions.getLipCornerDepressor();
                break;
            case MetricsManager.LIP_PRESS:
                score = face.expressions.getLipPress();
                break;
            case MetricsManager.LIP_PUCKER:
                score = face.expressions.getLipPucker();
                break;
            case MetricsManager.LIP_STRETCH:
                score = face.expressions.getLipStretch();
                break;
            case MetricsManager.LIP_SUCK:
                score = face.expressions.getLipSuck();
                break;
            case MetricsManager.MOUTH_OPEN:
                score = face.expressions.getMouthOpen();
                break;
            case MetricsManager.NOSE_WRINKLE:
                score = face.expressions.getNoseWrinkle();
                break;
            case MetricsManager.SMILE:
                score = face.expressions.getSmile();
                break;
            case MetricsManager.SMIRK:
                score = face.expressions.getSmirk();
                break;
            case MetricsManager.UPPER_LIP_RAISE:
                score = face.expressions.getUpperLipRaise();
                break;
            case MetricsManager.VALENCE:
                score = face.emotions.getValence();
                break;
            case MetricsManager.YAW:
                score = face.measurements.orientation.getYaw();
                break;
            case MetricsManager.ROLL:
                score = face.measurements.orientation.getRoll();
                break;
            case MetricsManager.PITCH:
                score = face.measurements.orientation.getPitch();
                break;
            case MetricsManager.INTER_OCULAR_DISTANCE:
                score = face.measurements.getInterocularDistance();
                break;
            case MetricsManager.BRIGHTNESS:
                score = face.qualities.getBrightness();
                break;
            default:
                score = Float.NaN;
                break;
        }
        Log.i("Score", Float.toString(score));
        return score;
    }
}
