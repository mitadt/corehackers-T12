package com.example.chintan.smartwardrobe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraPreview extends AppCompatActivity
{
    private File newFile;
    protected static final int MEDIA_TYPE_IMAGE = 1;
    private static Uri locuri;
    long captureTime;
    public static String _path = "";
    private Camera mCamera;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        context = CameraPreview.this;
        startCameraActivity();
    }

    protected void startCameraActivity() {
        Log.i("MakeMachine", "startCameraActivity()");

        newFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

        locuri = Uri.fromFile(newFile);
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        intent.putExtra(MediaStore.EXTRA_OUTPUT,FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".com.example.chintan.smartwardrobe.provider", newFile));

        captureTime = System.currentTimeMillis();
        startActivityForResult(intent, 0);
    }

    private static File getOutputMediaFile(int type)
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        String path = mediaStorageDir.getAbsolutePath();

        try {
            Toast tst;
            if (!mediaStorageDir.exists()) {
                Log.d("MyCameraApp", "Directory does not exist");
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                    return null;
                }
            } else {
                Log.d("MyCameraApp", "Directory already exists");
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
//				if(data==null)
//				{
                if (locuri == null) {
                    Toast tsTest = Toast.makeText(this, "Data is null", Toast.LENGTH_LONG);
                    tsTest.show();
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 6;
                    Bitmap bmpimage = BitmapFactory.decodeStream(getContentResolver().openInputStream(locuri), null, options);
                    //Bitmap bmpimage=BitmapFactory.decodeStream(getContentResolver().openInputStream(locuri));
                    ImageView imgBmp = (ImageView) findViewById(R.id.imgTemp);
                    imgBmp.setImageBitmap(bmpimage);
                    setImageButton(bmpimage);
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setImageButton(Bitmap bmpimage)
    {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            if (bmpimage.compress(Bitmap.CompressFormat.JPEG, 30, bytes)) {

                //Save image to new File
                try {
                    newFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    fos.write(bytes.toByteArray());
                    fos.flush();
                    fos.close();
                    _path = newFile.getAbsolutePath();

                    long fileSize = newFile.length();

                    //set image as button image
                    ImageButton imButon = new ImageButton(this);
                    Uri imgUri = Uri.parse(_path);
                    //imButon.setImageURI(imgUri);

                    AddClothesActivity.imgBtn.setImageURI(imgUri);
                    AddClothesActivity.imgBtn.invalidate();
                    finish();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
        }
    }

    protected void onPhotoTaken()
    {
        Log.i("MakeMachine", "onPhotoTaken");

        //_taken = true;

        Toast ts1 = Toast.makeText(this, "Photo Taken ", Toast.LENGTH_SHORT);
        ts1.show();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(_path, options);
    }

    public void startCamera() {
        try {
            //if camera is available get instance of the same
            Camera mycam = getCameraInstance();
            //Create a preview class
            Camerapreview mPreview = new Camerapreview(CameraPreview.this, mycam);
        }
        catch (Exception ex)
        {
            String msg = ex.getLocalizedMessage();
            System.out.println(msg);
        }
    }

    public static Camera getCameraInstance()
    {
        Camera c = null;
        try {
            // attempt to get a Camera instance
            c = Camera.open();
        }
        catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        // returns null if camera is unavailable
        return c;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // release the camera immediately on pause event
        releaseCamera();
    }

    private void releaseCamera()
    {
        try {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
        catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            System.out.println(msg);
        }
    }

    /**
     * A basic Camera preview class
     */
    public class Camerapreview extends SurfaceView implements SurfaceHolder.Callback
    {
        private SurfaceHolder mHolder;

        public Camerapreview(Context context, Camera camera)
        {
            super(context);
            mCamera = camera;

            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder)
        {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (Exception e) {
                //Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder)
        {
            try {
                // empty. Take care of releasing the Camera preview in your activity.
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();

                System.out.println(e.getLocalizedMessage());

            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
        {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.
            if (mHolder.getSurface() == null) {
                // preview surface does not exist
                return;
            }
            // stop preview before making changes
            try {
                mCamera.stopPreview();
                // make any resize, rotate or reformatting changes here
                // start preview with new settings
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (Exception e) {
                // ignore: tried to stop a non-existent preview
            }
        }
    }

    //Callback handler for camera.takePicture
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                // TODO Auto-generated method stub
                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                if (pictureFile == null) {

                    Log.d("TAG", "Error creating media file, check storage permissions: ");
                    return;
                }
                //Write data to file
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();


                //RElease camera
                releaseCamera();


                //test for OCR
                ocr_test(pictureFile.getAbsolutePath());
            }
            catch (Exception ex) {
                String msg = ex.getLocalizedMessage();
                System.out.println(msg);
            }
        }

    };

    private void ocr_test(String filepath) {
    }
}
