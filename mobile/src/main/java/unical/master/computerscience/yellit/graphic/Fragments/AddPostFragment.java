package unical.master.computerscience.yellit.graphic.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import siclo.com.ezphotopicker.api.EZPhotoPick;
import siclo.com.ezphotopicker.api.EZPhotoPickStorage;
import siclo.com.ezphotopicker.api.models.EZPhotoPickConfig;
import siclo.com.ezphotopicker.api.models.PhotoSource;
import siclo.com.ezphotopicker.models.PhotoIntentException;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.Dialog.CustomDialogBottomSheet;


/**
 * Created by Salvatore on the 17/03/2017
 */
public class AddPostFragment extends Fragment implements OnChartValueSelectedListener {

    private static final String DEMO_PHOTO_PATH = "MyDemoPhotoDir";
    private ArrayList<String> images;

    @Bind(R.id.pie_menu)
    protected PieChart mainMenu;
    @Bind(R.id.addpost_floating_button)
    protected FloatingActionButton floatingButton;
    @Bind(R.id.addpost_img)
    protected ImageView imageLoaded;
    @Bind(R.id.addpost_cam_button)
    protected Button camButton;
    @Bind(R.id.addpost_gall_button)
    protected Button galButton;

    private Typeface mTfRegular;
    private Typeface mTfLight;
    private String[] mainCategoryLabels;
    private String[] subCategoryLabels;
    private int[] mainCategoryColors;
    private int[] subCategoryColors;

    private Animation expandIn;
    private Animation expandOut;

    private boolean isSubMenu;
    private int lastSubMenu;

    @Bind(R.id.addpost_bottomsheet)
    View bottomSheet;

    @Bind(R.id.galleryGridView)
    protected GridView gallery;

    private BottomSheetBehavior mBottomSheetBehavior;
    private EZPhotoPickStorage ezPhotoPickStorage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        ButterKnife.bind(this, view);

        mainCategoryLabels = getResources().getStringArray(R.array.main_categories);
        mainCategoryColors = getResources().getIntArray(R.array.main_colors);

        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        isSubMenu = false;
        lastSubMenu = -1;

        buildAnimationStuff();

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setPeekHeight(1000);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                    floatingButton.setVisibility(View.VISIBLE);
                    //animate().scaleX(1).scaleY(1).setDuration(300).start();


                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {

                    floatingButton.setVisibility(View.INVISIBLE);
                    //.animate().scaleX(0).scaleY(0).setDuration(300).start();

                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        buildMainMenu();

        ezPhotoPickStorage = new EZPhotoPickStorage(getActivity());

        buildButtonsCallback();
        setupImagePicker();

        return view;
    }

    private void setupImagePicker() {
        gallery.setAdapter(new ImageAdapter(getActivity()));
        this.gridViewSetting(gallery);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != images && !images.isEmpty())

                    Glide.with(getContext()).load(images.get(position))
                            .centerCrop()
                            .into(imageLoaded);

            }
        });
    }

    private void buildButtonsCallback() {

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
    }

    private void buildAnimationStuff() {
        expandIn = AnimationUtils.loadAnimation(getContext(), R.anim.expand_in);
        expandOut = AnimationUtils.loadAnimation(getContext(), R.anim.expand_out);

        Animation.AnimationListener animation1Listener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (isSubMenu) {
                    mainMenu.setCenterText(mainCategoryLabels[lastSubMenu]);
                    setData(subCategoryLabels, subCategoryColors);
                } else {
                    mainMenu.setCenterText(generateCenterSpannableText());
                    setData(mainCategoryLabels, mainCategoryColors);
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

        setData(mainCategoryLabels, mainCategoryColors);

        mainMenu.getLegend().setEnabled(false);

        // entry label styling
        mainMenu.setEntryLabelColor(Color.WHITE);
        mainMenu.setEntryLabelTypeface(mTfRegular);
        mainMenu.setEntryLabelTextSize(11f);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("What are you doing?\npowered by Yellit");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 17, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 17, s.length() - 18, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 17, s.length() - 18, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 17, s.length() - 17, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 17, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 17, s.length(), 0);
        return s;
    }

    private void setData(String[] labels, int[] colorz) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < labels.length; i++) {

            entries.add(
                    new PieEntry(20,
                            labels[i]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawValues(false);

        dataSet.setSliceSpace(3f);
        //dataSet.setIconsOffset(new MPPointF(0, 40));
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

        // undo all highlights
        mainMenu.highlightValues(null);

        mainMenu.invalidate();
    }

    /*
        In base all'index del bottone premuto carichiamo uno specifico sub menù, secondo l'ordine:

        Category 1 = Index 0
        Category 2 = Index 1
        etc
     */
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        if (isSubMenu) {

            switch (lastSubMenu) {
                case 0:
                    handleSubMenu1(e, h);
                    break;
                case 1:
                    handleSubMenu2(e, h);
                    break;
                case 2:
                    handleSubMenu3(e, h);
                    break;
                case 3:
                    handleSubMenu4(e, h);
                    break;
                case 4:
                    handleSubMenu5(e, h);
                    break;
            }

        } else {

            // Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            // v.vibrate(80); // milliseconds

            int index = (int) h.getX();

            switch (index) {
                case 0:
                    subCategoryLabels = getResources().getStringArray(R.array.sub_categories_1);
                    subCategoryColors = getResources().getIntArray(R.array.sub_colors_1);
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
                    subCategoryLabels = getResources().getStringArray(R.array.sub_categories_5);
                    subCategoryColors = getResources().getIntArray(R.array.sub_colors_5);
                    break;
            }

            isSubMenu = true;
            lastSubMenu = index;
            mainMenu.startAnimation(expandIn);
        }
        /* Log.i("VAL SELECTED",
                "Value: " + e.getX() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex()); */
    }

    private void handleSubMenu1(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

            case 0:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case 1:
            case 2:
            case 3:

                CustomDialogBottomSheet c = CustomDialogBottomSheet.newInstance("");
                c.show(getFragmentManager().beginTransaction(), "");

                break;

            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    private void handleSubMenu2(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    private void handleSubMenu3(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    private void handleSubMenu4(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    private void handleSubMenu5(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

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
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == EZPhotoPick.PHOTO_PICK_GALERY_REQUEST_CODE) {

            try {
                ArrayList<String> pickedPhotoNames = data.getStringArrayListExtra(EZPhotoPick.PICKED_PHOTO_NAMES_KEY);

                for (String photoName : pickedPhotoNames) {
                    Bitmap pickedPhoto = ezPhotoPickStorage.loadStoredPhotoBitmap(DEMO_PHOTO_PATH, photoName, 300);

                    imageLoaded.setBackground(null);

                    imageLoaded.setImageBitmap(pickedPhoto);
                    imageLoaded.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    imageLoaded.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == EZPhotoPick.PHOTO_PICK_CAMERA_REQUEST_CODE) {

            try {

                Bitmap pickedPhoto = ezPhotoPickStorage.loadLatestStoredPhotoBitmap(300);

                imageLoaded.setImageBitmap(pickedPhoto);
                imageLoaded.setScaleType(ImageView.ScaleType.FIT_CENTER);

                imageLoaded.setVisibility(View.VISIBLE);
                setupImagePicker();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void gridViewSetting(GridView gridview) {

        int size = 50;
        // Calculated single Item Layout Width for each grid element ....
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

        /**
         * The context.
         */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext the local context
         */
        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = getAllShownImagesPath(context);
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(images.get(position))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(picturesView);

            return picturesView;
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
