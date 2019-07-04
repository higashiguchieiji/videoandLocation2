package com.example.mediarecorder;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import android.widget.Button;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Environment;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.getExternalStoragePublicDirectory;


public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private Camera myCamera;
    private SurfaceView mySurfaceView;
    private boolean isRecording;
    private SurfaceHolder v_holder;
    private MediaRecorder mediaRecorder;
    private String filePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        SurfaceHolder holder = mySurfaceView.getHolder();
        holder.addCallback(mSurfaceListener);
        //holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        myCamera = getCameraInstance();//

    }

    private SurfaceHolder.Callback mSurfaceListener = new SurfaceHolder.Callback() {

        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub

            try {
                myCamera.setPreviewDisplay(holder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            //myCamera.release();
            //myCamera = null;

            myCamera.setPreviewCallback(null);
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;

        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub
            v_holder = holder; // SurfaceHolderを保存

            //myCamera.stopPreview();
            Camera.Parameters parameters = myCamera.getParameters();

            List<Size> asizeSupport = parameters.getSupportedPreviewSizes();
            //一番小さいプレビューサイズを利用
            Size size = asizeSupport.get(asizeSupport.size() - 1);
            parameters.setPreviewSize(1920,1080);
            Log.d("size1", "w=" + String.valueOf(width) + "h=" + String.valueOf(height));//
            Log.d("size2", "w=" + String.valueOf(size.width) + "h=" + String.valueOf(size.height));//

            LayoutParams paramLayout;
            paramLayout = mySurfaceView.getLayoutParams();
            paramLayout.width = 1920;
            paramLayout.height = 1080;
            mySurfaceView.setLayoutParams(paramLayout);
            myCamera.setDisplayOrientation(0);// カメラを回転

            //List<Camera.Size> size = parameters.getSupportedPreviewSizes();

            //Log.d("カメラのサイズ", "w=" + String.valueOf(size.get(0).width) + "h=" + String.valueOf(size.get(0).height));//
            //parameters.setPreviewSize(size.get(0).width, size.get(0).height);
            // myCamera.setParameters(parameters);
            myCamera.startPreview();//
        }
    };



   // final View.OnClickListener b7OnClickListener = new View.OnClickListener() {
     //   @Override



   public void click(View v) {
            // 録画中でなければ録画を開始
            if (!isRecording) {

                initializeVideoSettings(); // MediaRecorderの設定
              //  mediaRecorder.start(); // 録画開始
                isRecording = true; // 録画中のフラグを立てる

                // 録画中であれば録画を停止
            } else {
                mediaRecorder.stop(); // 録画停止
                mediaRecorder.reset(); // 無いとmediarecorder went away with unhandled
                // events　が発生
                mediaRecorder.release();//
                mediaRecorder = null;
                //myCamera.lock();
                //myCamera.release(); // release the camera for other applications
                //myCamera = null;
                isRecording = false; // 録画中のフラグを外す
            }
        }
   // };


    private void initializeVideoSettings() {
        // TODO 自動生成されたメソッド・スタブ
        try {

            //myCamera = getCameraInstance();
            mediaRecorder = new MediaRecorder();
            //mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);


            myCamera.unlock();

            mediaRecorder.setCamera( myCamera );
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 録画の入力ソースを指定
//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // ファイルフォーマットを指定
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP); // ビデオエンコーダを指定
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

            //CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            //mediaRecorder.setProfile(profile);

            mediaRecorder.setVideoFrameRate(24); // 動画のフレームレートを指定
            mediaRecorder.setVideoSize(1920, 1080); // 動画のサイズを指定

            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"Camera");
           // File folder = getCacheDir();
           // File file;
            File file = File.createTempFile("sample20110603", ".mp4", folder);//

            mediaRecorder.setOutputFile(file.getAbsolutePath());
            mediaRecorder.setPreviewDisplay(v_holder.getSurface());//


            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }
    //private static File getOutputMediaFile(){
    //    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
    //    if (! mediaStorageDir.exists()){
    //        if (! mediaStorageDir.mkdirs()){
    //            Log.d("MyCameraApp", "failed to create directory");
    //            return null;
    //        }
    //    }
    //    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    //    File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mpg");
    //    return mediaFile;
    // }

    private Camera getCameraInstance() {
        // TODO 自動生成されたメソッド・スタブ
        Camera c = null;
        try {
            myCamera = Camera.open( 0 ); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return myCamera; // returns null if camera is unavailable
    }

}
