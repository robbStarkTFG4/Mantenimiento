package local_db_activity_fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import data_activity_fragments.FotoDialogFragment;
import data_activity_fragments.ImageViewFragment;
import local_Db.FotoDB;
import mantenimiento.mim.com.mantenimiento.DataActivity;
import mantenimiento.mim.com.mantenimiento.R;
import util.navigation.Modifier;
import util.navigation.Navigator;
import util.navigation.adapter.FotosAdapter;
import util.navigation.adapter.FotosLocalAdapter;
import util.navigation.custom.recycler.RecyclerViewEmpty;
import util.navigation.modelos.Foto;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraLocalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraLocalFragment extends Fragment implements FotosAdapter.PositionConsumer, FotosLocalAdapter.PositionConsumer {
    private boolean control = false;
    private final int SELECT_PHOTO = 290;

    @Override
    public void position(int position) {
        ImageViewFragment dialog = ImageViewFragment.newInstance(dataList.get(position).getArchivo(), null);
        dialog.show(getFragmentManager(), "imgDialog");
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public interface PhotosConsumer {
        public void setPhotosList(List<FotoDB> list);

        public List<FotoDB> getPhotosList();
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Navigator navigator;

    private RecyclerViewEmpty mRecyclerView;
    private RecyclerViewEmpty.Adapter mAdapter;
    private RecyclerViewEmpty.LayoutManager mLayoutManager;

    private List<FotoDB> dataList = new ArrayList<>();
    private FloatingActionButton floatButton;
    private String ruta;

    private PhotosConsumer consumer;

    public static final int REQUEST_IMAGE_CAPTURE = 4231;

    public CameraLocalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraLocalFragment newInstance(String param1, String param2, Navigator navigator) {
        CameraLocalFragment fragment = new CameraLocalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Navigator) {
            navigator = (Navigator) context;
            consumer = (PhotosConsumer) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera_local, container, false);

        widgetSetUp(view);
        dataSetUp();
        recyclerSetUp(view);

        InputMethodManager inputManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:

        inputManager.hideSoftInputFromWindow(container.getWindowToken(), 0);


        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (consumer != null) {
            consumer.setPhotosList(dataList);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.camera_local_menu, menu);
        Modifier.changeMenuItemColor(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera_foto_local:
                cameraIntent();
                break;
            case R.id.camera_attach_dos:
                pickPhoto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void pickPhoto() {
        control = false;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    private void dataSetUp() {
        if (consumer.getPhotosList() != null) {
            dataList = consumer.getPhotosList();
        }
    }

    private void widgetSetUp(View view) {
        floatButton = (FloatingActionButton) view.findViewById(R.id.fab_picture_local);
        floatButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });
    }

    private File createImageFile() throws IOException {

        Random r = new Random();

        int i1 = r.nextInt(200 - 70) + 17;
        int i2 = r.nextInt(67 - 12) - 6;
        int i3 = r.nextInt(89 - 27) - 6;
        int i4 = r.nextInt(200 - 70) + 17;
        int i5 = r.nextInt(89 - 27) - 6;
        String photoTitle = "JPEG_" + i1 + i2 + "uncompressed_" + i3 + i4 + "-" + i5;

        File image = new File(Environment.  //THIS WORKS
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                photoTitle + ".jpg");

        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void cameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                ruta = photoFile.getPath();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
                Toast.makeText(getContext(), "exception in file creation", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(getContext(), "no se creo el archivo temporal", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "package manager es nulo", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                imageResult(resultCode);
                break;
            case SELECT_PHOTO:
                if (resultCode == ((Activity) getContext()).RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = ((Activity) getContext()).getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        //selectedImage.setConfig(matrix);


                        final File imageFile = createImageFile();

                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {
                                try {
                                    OutputStream stream = new FileOutputStream(imageFile);
                                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    stream.flush();
                                    stream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                notifyChange();
                            }
                        }.execute();


                        FotoDialogFragment foto = FotoDialogFragment.newInstance("das", "das");
                        foto.show(getFragmentManager(), "dialog");
                        ruta = imageFile.getPath();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }

    private void imageResult(int resultCode) {
        if (resultCode == ((Activity) getContext()).RESULT_OK) {
            control = true;
            FotoDialogFragment foto = FotoDialogFragment.newInstance("das", "das");
            foto.show(getFragmentManager(), "dialog");

        } else if (resultCode == ((Activity) getContext()).RESULT_CANCELED) {
            // User cancelled the image capture
            if (ruta != null) {
                Toast.makeText(getContext(), "borra archivo", Toast.LENGTH_LONG).show();
            }
        } else {
            // Image capture failed, advise user
        }
    }

    private void notifyChange() {
        mAdapter.notifyDataSetChanged();
    }

    private void recyclerSetUp(View view) {
        mRecyclerView = (RecyclerViewEmpty) view.findViewById(R.id.fotos_reciclador_local);
        mRecyclerView.setEmptyView((TextView) view.findViewById(R.id.empty_view_local_fotos));
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FotosLocalAdapter(dataList, getActivity(), this);

        mRecyclerView.setAdapter(mAdapter);

    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public void setPhotoInfo(String title, String descripcion) {
        FotoDB current = new FotoDB();
        current.setArchivo(ruta);
        current.setTitulo(title);
        current.setDescripcion(descripcion);
        dataList.add(current);
        //current = new Foto();
        mAdapter.notifyDataSetChanged();
    }

}
