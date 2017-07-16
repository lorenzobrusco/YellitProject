package unical.master.computerscience.yellit.graphic.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import siclo.com.ezphotopicker.api.EZPhotoPick;
import siclo.com.ezphotopicker.api.EZPhotoPickStorage;
import siclo.com.ezphotopicker.api.models.EZPhotoPickConfig;
import siclo.com.ezphotopicker.api.models.PhotoSource;
import siclo.com.ezphotopicker.models.PhotoIntentException;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.connection.PostGestureService;
import unical.master.computerscience.yellit.connection.ServerResponse;
import unical.master.computerscience.yellit.graphic.Activities.LoginFragment;
import unical.master.computerscience.yellit.graphic.custom.SelectorImageView;
import unical.master.computerscience.yellit.logic.GoogleApiClient;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.utilities.BaseURL;


/**
 * Created by Salvatore on the 17/03/2017
 */
public class AddPostFragment extends Fragment implements OnChartValueSelectedListener {

    private static final String DEMO_PHOTO_PATH = "MyDemoPhotoDir";

    @Bind(R.id.pie_menu)
    protected PieChart mainMenu;
    @Bind(R.id.addpost_floating_button)
    protected FloatingActionButton sendPostFloatButton;
    @Bind(R.id.addpost_cam_button)
    protected AppCompatButton camButton;
    @Bind(R.id.addpost_gall_button)
    protected AppCompatButton galButton;
    @Bind(R.id.addpost_bottomsheet)
    protected View mBottomSheetAddPost;
    @Bind(R.id.comment_post_add)
    protected TextInputEditText mCommentText;
    @Bind(R.id.addpost_switch_button)
    protected SwitchCompat positionSwitchButton;
    @Bind(R.id.galleryGridView)
    protected GridView gallery;
    @Bind(R.id.addpost_transp_layer)
    protected ImageView transparentLayer;
    @Bind(R.id.tv_location_text)
    protected MaterialSpinner mLocationSpinner;
    private ProgressDialog progressDialog;

    private Typeface mTfRegular;
    private Typeface mTfLight;
    private String[] mainCategoryLabels;
    private String[] subCategoryLabels;
    private int[] mainCategoryColors;
    private int[] subCategoryColors;

    private String currentCategory;

    private Animation expandIn;
    private Animation expandOut;

    private boolean isSubMenu;
    private int lastSubMenu;

    private BottomSheetBehavior mBottomSheetBehavior;
    private EZPhotoPickStorage ezPhotoPickStorage;

    private ArrayList<String> mImages;
    private String currentPhotoPath = "";
    private boolean locked = false;
    private int currentPosition = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        ButterKnife.bind(this, view);
        GoogleApiClient.getInstance((AppCompatActivity) getActivity()).getPlaceDetection(getContext());
        this.progressDialog = new ProgressDialog(this.getContext());
        buildVariousStuff();
        buildMainMenu();
        ezPhotoPickStorage = new EZPhotoPickStorage(getActivity());
        buildButtonsCallback();
        setupImagePicker(null);
        changePriorityOfScroll();

        return view;
    }

    private void buildVariousStuff() {

        mainCategoryLabels = getResources().getStringArray(R.array.main_categories);
        mainCategoryColors = getResources().getIntArray(R.array.main_colors);

        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        isSubMenu = false;
        lastSubMenu = -1;

        buildAnimationCallback();

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetAddPost);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setPeekHeight(450);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    sendPostFloatButton.setVisibility(View.VISIBLE);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    sendPostFloatButton.setVisibility(View.INVISIBLE);
                    transparentLayer.setVisibility(View.GONE);
                    resetBottomSheet(true);

                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });


        transparentLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void resetBottomSheet(boolean all) {
        mainMenu.highlightValues(null);
        mainMenu.invalidate();
        if (all)
            mCommentText.setText("");
        currentPhotoPath = null;
        this.setupImagePicker(null);
    }

    private void changePriorityOfScroll() {

        mBottomSheetAddPost.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View p_v, MotionEvent p_event) {
                mCommentText.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        mCommentText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View p_v, MotionEvent p_event) {
                p_v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void setupImagePicker(final String path) {
        if (path == null) {
            gallery.setAdapter(new ImageAdapter(getActivity()));
        } else {
            mImages.add(0, path);
            gallery.setAdapter(new ImageAdapter(getActivity(), mImages));
        }

        this.gridViewSetting(gallery);
    }

    private void buildButtonsCallback() {

        sendPostFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment = mCommentText.getText().toString();
                String place = "";

                boolean positionEnabled = positionSwitchButton.isChecked();
                if (positionEnabled)
                    place = mLocationSpinner.getItems().get(mLocationSpinner.getSelectedIndex()).toString();

                if (!positionEnabled && comment.equals("") && currentPhotoPath.equals(""))
                    Toast.makeText(getContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
                else {
                    createPostForUpload(comment, place);
                }

            }
        });

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    EZPhotoPickConfig config = new EZPhotoPickConfig();
                    config.photoSource = PhotoSource.CAMERA;
                    config.storageDir = DEMO_PHOTO_PATH;
                    config.needToAddToGallery = true;
                    config.exportingSize = 1000;
                    EZPhotoPick.startPhotoPickActivity(AddPostFragment.this, config);

                } catch (PhotoIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        galButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    EZPhotoPickConfig config = new EZPhotoPickConfig();
                    config.photoSource = PhotoSource.GALERY;
                    config.needToExportThumbnail = true;
                    config.isAllowMultipleSelect = true;
                    config.storageDir = DEMO_PHOTO_PATH;
                    config.exportingThumbSize = 200;
                    config.exportingSize = 1000;
                    EZPhotoPick.startPhotoPickActivity(AddPostFragment.this, config);

                } catch (PhotoIntentException e) {
                    e.printStackTrace();
                }
            }
        });

        positionSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mLocationSpinner.setEnabled(isChecked);
            }
        });
    }

    /**
     * Gestire casi di variabili NULLE ma quando comunque il post può essere inviato
     */
    private void createPostForUpload(String comment, String place) {
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Publication...");
        progressDialog.show();
        int pos = positionLocation(place) == -1 ? -1 : positionLocation(place);

        File file = new File(currentPhotoPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), pos == -1 ? "0.00" : InfoManager.getInstance().getmPlaceData().latLongs.get(pos).latitude + "");
        RequestBody longi = RequestBody.create(MediaType.parse("text/plain"), pos == -1 ? "0.00" : InfoManager.getInstance().getmPlaceData().latLongs.get(pos).longitude + "");

        RequestBody newComment = RequestBody.create(MediaType.parse("text/plain"), comment);
        RequestBody newPlace = RequestBody.create(MediaType.parse("text/plain"), place);
        RequestBody newCategory = RequestBody.create(MediaType.parse("text/plain"), currentCategory);
        //TODO remove comment at the end
        RequestBody newUserMail = RequestBody.create(MediaType.parse("text/plain"), InfoManager.getInstance().getmUser().getEmail());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostGestureService getResponse = retrofit.create(PostGestureService.class);

        Call<ServerResponse> call = getResponse.uploadPost(fileToUpload, filename, newUserMail, newComment, newPlace, newCategory, lat, longi, "adding");
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                progressDialog.dismiss();
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {

                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getContext(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();

                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                        isSubMenu = false;
                        lastSubMenu = -1;
                        mainMenu.startAnimation(expandIn);

                    } else {
                        Toast.makeText(getContext(), serverResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Server Response NULL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Fallimento " + call.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private int positionLocation(String place) {
        int i = 0;
        if (place.contains("places") && place.contains("detected"))
            return -1;
        for (String s : InfoManager.getInstance().getmPlaceData().place) {
            if (s.equals(place))
                return i;
            i++;
        }
        return -1;
    }

    private void buildAnimationCallback() {
        expandIn = AnimationUtils.loadAnimation(getContext(), R.anim.expand_in);
        expandOut = AnimationUtils.loadAnimation(getContext(), R.anim.expand_out);

        Animation.AnimationListener animation1Listener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (isSubMenu) {

                    currentCategory = mainCategoryLabels[lastSubMenu];

                    mainMenu.setCenterText(currentCategory);
                    setPieMenuData(subCategoryLabels, subCategoryColors);

                } else {

                    currentCategory = "";

                    mainMenu.setCenterText(generateCenterSpannableText());
                    setPieMenuData(mainCategoryLabels, mainCategoryColors);

                }

                mainMenu.startAnimation(expandOut);
            }
        };

        expandIn.setAnimationListener(animation1Listener);
    }

    public void buildMainMenu() {

        mainMenu.setBackgroundColor(Color.TRANSPARENT);

        mainMenu.setUsePercentValues(true);
        mainMenu.getDescription().setEnabled(false);
        mainMenu.setExtraOffsets(5, 10, 5, 5);

        mainMenu.setDragDecelerationFrictionCoef(0.9f);

        mainMenu.setCenterTextTypeface(mTfLight);
        mainMenu.setCenterText(generateCenterSpannableText());

        mainMenu.setDrawHoleEnabled(true);
        mainMenu.setHoleColor(Color.WHITE);

        mainMenu.setTransparentCircleColor(Color.WHITE);
        mainMenu.setTransparentCircleAlpha(110);

        mainMenu.setHoleRadius(42f);
        mainMenu.setTransparentCircleRadius(45f);

        mainMenu.setDrawCenterText(true);

        mainMenu.setRotationAngle(0);
        // enable rotation of the chart by touch
        mainMenu.setRotationEnabled(true);
        mainMenu.setHighlightPerTapEnabled(true);

        // add a selection listener
        mainMenu.setOnChartValueSelectedListener(this);

        setPieMenuData(mainCategoryLabels, mainCategoryColors);

        mainMenu.getLegend().setEnabled(false);

        /** entry label styling */
        mainMenu.setEntryLabelColor(Color.WHITE);
        mainMenu.setEntryLabelTypeface(mTfRegular);
        mainMenu.setEntryLabelTextSize(11f);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Yellit!\nWhat's up today?");

        s.setSpan(new RelativeSizeSpan(2.2f), 0, 7, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, 7, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 7, 0);

        s.setSpan(new RelativeSizeSpan(1.1f), 8, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 8, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 8, s.length(), 0);
        return s;
    }

    private void setPieMenuData(String[] labels, int[] colorz) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < labels.length; i++) {
            entries.add(new PieEntry(20, labels[i]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(11f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : colorz)
            colors.add(c);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(40f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        mainMenu.setData(data);

        /** undo all highlights */
        mainMenu.highlightValues(null);

        mainMenu.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        if (isSubMenu) {

            switch (lastSubMenu) {
                case 1:
                    handleSubMenuCategory2(e, h);
                    break;
                case 2:
                    handleSubMenuCategory3(e, h);
                    break;
                case 3:
                    handleSubMenuCategory4(e, h);
                    break;
            }

        } else {

            int index = (int) h.getX();

            switch (index) {
                case 0:
                    openBottomSheet();
                    break;
                case 1:
                    subCategoryLabels = getResources().getStringArray(R.array.sub_categories_2);
                    subCategoryColors = getResources().getIntArray(R.array.sub_colors_2);
                    break;
                case 2:
                    subCategoryLabels = getResources().getStringArray(R.array.sub_categories_3);
                    subCategoryColors = getResources().getIntArray(R.array.sub_colors_3);
                    break;
                case 3:
                    subCategoryLabels = getResources().getStringArray(R.array.sub_categories_4);
                    subCategoryColors = getResources().getIntArray(R.array.sub_colors_4);
                    break;
                case 4:
                    openBottomSheet();
                    break;
            }

            lastSubMenu = index;
            currentCategory = mainCategoryLabels[lastSubMenu];

            if (index != 0 && index != 4) {
                isSubMenu = true;
                mainMenu.startAnimation(expandIn);
            }
        }
    }

    private void handleSubMenu(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

            case 0:
                openBottomSheet();
            case 1:
            case 2:
            case 3:

                break;

            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    private void openBottomSheet() {

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        transparentLayer.setVisibility(View.VISIBLE);
        mainMenu.highlightValues(null);
        List<String> places = InfoManager.getInstance().getmPlaceData().place;
        if (places.size() == 0) {
            places.add("No places detected");
        }
        mLocationSpinner.setItems(places);
        mLocationSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                Snackbar.make(view, "Clicked " + item + " at " + position, Snackbar.LENGTH_LONG).show();
                Snackbar.make(view, InfoManager.getInstance().getmPlaceData().latLongs.get(position).latitude + " lat", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void handleSubMenuCategory2(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

            case 0:
            case 1:
            case 2:
            case 4:
                openBottomSheet();
                break;
            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    private void handleSubMenuCategory3(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {
            case 0:
            case 1:
            case 2:
                openBottomSheet();
                break;
            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    private void handleSubMenuCategory4(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {
            case 0:
            case 1:
            case 2:
            case 3:
                openBottomSheet();
                break;
            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == EZPhotoPick.PHOTO_PICK_GALERY_REQUEST_CODE) {
            ArrayList<String> pickedPhotoNames = data.getStringArrayListExtra(EZPhotoPick.PICKED_PHOTO_NAMES_KEY);
            setupImagePicker(ezPhotoPickStorage.getAbsolutePathOfStoredPhoto(DEMO_PHOTO_PATH, pickedPhotoNames.get(0)));
        } else if (requestCode == EZPhotoPick.PHOTO_PICK_CAMERA_REQUEST_CODE) {
                ArrayList<String> pickedPhotoNames = data.getStringArrayListExtra(EZPhotoPick.PICKED_PHOTO_NAMES_KEY);
                setupImagePicker(ezPhotoPickStorage.getAbsolutePathOfStoredPhoto(DEMO_PHOTO_PATH, pickedPhotoNames.get(0)));
                resetBottomSheet(false);

        }
    }

    private void gridViewSetting(GridView gridview) {

        int size = 50;
        /** Calculated single Item Layout Width for each grid element ....*/
        int width = 90;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int totalWidth = (int) (width * size * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridview.setLayoutParams(params);
        gridview.setStretchMode(GridView.STRETCH_SPACING);
        gridview.setNumColumns(size);
    }

    /**
     * The Class ImageAdapter.
     */
    private class ImageAdapter extends BaseAdapter {

        private Activity context;

        public ImageAdapter(Activity localContext) {
            context = localContext;
            mImages = getAllShownImagesPath(context);
        }

        public ImageAdapter(Activity localContext, ArrayList<String> images) {
            context = localContext;
            mImages = images;
        }

        public int getCount() {
            return mImages.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, final View convertView,
                            final ViewGroup parent) {

            final RelativeLayout layout = new RelativeLayout(context);
            final ImageView selectorImage = new ImageView(context);
            selectorImage.setBackground(getResources().getDrawable(R.drawable.unselected_image));
            final SelectorImageView picturesView = new SelectorImageView(context);
            selectorImage.setScaleX(0.6f);
            selectorImage.setScaleY(0.6f);
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            layout.setLayoutParams(new GridView.LayoutParams(270, 270));
            picturesView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            Glide.with(context).load(mImages.get(position))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(picturesView);
            layout.addView(picturesView);
            layout.addView(selectorImage);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == currentPosition && locked) {
                        picturesView.setmSelected();

                        if (!picturesView.ismSelected()) {
                            selectorImage.setBackground(getResources().getDrawable(R.drawable.unselected_image));
                            picturesView.setColorFilter(Color.argb(0, 0, 0, 0));
                            currentPhotoPath = "";
                            locked = false;
                        }

                    } else if (!locked) {
                        picturesView.setmSelected();

                        if (picturesView.ismSelected()) {
                            picturesView.setColorFilter(Color.argb(80, 0, 0, 0));
                            selectorImage.setBackground(getResources().getDrawable(R.drawable.selected_image));
                            currentPhotoPath = mImages.get(position);
                            locked = true;
                            currentPosition = position;
                        }

                    } else {

                        Toast.makeText(getContext(), "Non puoi selezionare altre immagini", Toast.LENGTH_LONG).show();
                    }
                }
            });
            return layout;
        }

        /**
         * Getting All Images Path.
         *
         * @param activity the activity
         * @return ArrayList with images Path
         */
        private ArrayList<String> getAllShownImagesPath(Activity activity) {
            Uri uri;
            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            final Cursor cursor = getContext().getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                            null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            final int maxDim = 50;
            int index = 0;
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                listOfAllImages.add(absolutePathOfImage);
                index++;
                if (index == maxDim)
                    break;
            }
            return listOfAllImages;
        }
    }


}
