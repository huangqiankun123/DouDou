package com.example.administrator.doudou.modules.settings;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.doudou.BaseActivity;
import com.example.administrator.doudou.R;
import com.example.administrator.doudou.utils.ViewUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ChoosePhotoActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_select_album)
    TextView tvSelectAlbum;
    @BindView(R.id.gv_pictures)
    GridView gvPictures;
    /**
     * String 代表文件名
     * ArrayList<String> 文件下中的图片
     */
    private Map<String, ArrayList<String>> picMap = new HashMap<>();
    private ArrayList<ImageFileBean> mImageBeen;

    private Disposable mDisposable;

    private CheckPicAdapter mPicAdapter;

    private BottomSheetDialog mBottomSheetDialog;

    private final int ACTION_TAKE_PHOTO = 20;
    private final int CROP_PHOTO = 10;
    private Intent picToView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePhotoActivity.this.finish();
            }
        });
        mImageBeen = new ArrayList<>();
        requestRunTimePermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionCall() {
            @Override
            public void requestSuccess() {
                initImages();
            }

            @Override
            public void refused() {
                Toast.makeText(ChoosePhotoActivity.this, "请授予读写和相机权限", Toast.LENGTH_LONG).show();
            }
        });
        gvPictures.setOnItemClickListener(this);

    }

    /**
     * 1.先要做一个SD卡是否挂载的判断
     * 2.获取到所有图片的uri
     * 3.获取ContentResolver 会提供数据（这里就是图片）
     * 4.使用contentResolver.query 获取到cursor
     * 5.使用cursor循环来图片 和图片所在的文件
     * 6.创建map集合 key 是文件名（图片父路径名） value 是文件名下的图片集合
     * 7.将所有的文件下的图片 都放到一个集合中
     */

    /**
     * 在日常开发中，我们经常会使用MediaStore来获取手机的音频、图片、视频等相关信息。下面3个是常见的内部类：

     MediaStore.Audio
     获取音频信息的类

     MediaStore.Images
     获取图片信息

     MediaStore.Video
     获取视频信息

     *2、MediaStore.Images

     在该类中有一个借口和两个class ，分别为：

     MediaStore.Images.ImageColumns
     可以查看图片的字段信息

     MediaStore.Images.Media
     可以获取图片的相关信息

     MediaStore.Images.Thumbnails
     可以获取图片的缩略图

     关于MediaStore.Images.ImageColumns 可以查看api ，下面通过MediaStore.Images.Media 查询手机上的图片。


     private Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

     */
    private void initImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showSnackBar(tvSelectAlbum, "未发现存储设备", Snackbar.LENGTH_SHORT);
        }
        Uri imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(imagesUri, null, MediaStore.Images.Media.MIME_TYPE + "= ? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?"
                , new String[]{"image/jpeg", "image/jpg"}, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor == null) {
            showSnackBar(tvSelectAlbum, "未发现存储设备", Snackbar.LENGTH_SHORT);
            return;
        }

        while (cursor.moveToNext()) {
            //图片的路径名 (全称)
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //图片父路径名
            String parentPah = new File(path).getParentFile().getName();

            if (!picMap.containsKey(parentPah)) {
                ArrayList<String> childList = new ArrayList<>(); //只作为value使用
                childList.add(path);
                picMap.put(parentPah, childList);
            } else {
                //就根据parentpath获取到这个value 并添加到这value中
                picMap.get(parentPah).add(path);
            }
        }
        cursor.close();

        //Map<String, ArrayList<String>> picMap = new HashMap<>(); 转化成 ArrayList<String>

        Observable.create(new ObservableOnSubscribe<Map<String, ArrayList<String>>>() {
            @Override
            public void subscribe(ObservableEmitter<Map<String, ArrayList<String>>> e) throws Exception {
                e.onNext(picMap);
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<Map<String, ArrayList<String>>, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> apply(Map<String, ArrayList<String>> stringArrayListMap) throws Exception {
                        ArrayList<String> allPath = new ArrayList<String>();
                        //对map集合进行遍历
                        for (Map.Entry<String, ArrayList<String>> entry : stringArrayListMap.entrySet()) {
                            ImageFileBean imageFileBean = new ImageFileBean();
                            imageFileBean.setFileName(entry.getKey());
                            imageFileBean.setTopImage(entry.getValue().get(0));
                            imageFileBean.setImages(entry.getValue());
                            mImageBeen.add(imageFileBean); // 一个对象的集合 ArrayList<ImageFileBean>
                            allPath.addAll(entry.getValue()); //全部都是图片
                        }
                        allPath.add(0, "ic_action_camera"); //做为GridView 的集合
                        ImageFileBean all = new ImageFileBean();
                        all.setFileName(getString(R.string.all_pictures));
                        all.setImages(allPath);
                        all.setTopImage(allPath.get(1));
                        //这个对象只做为dailog的第一个item
                        mImageBeen.add(0, all); //添加在到 对象集合中 供 dailogrecycleview使用

                        return allPath;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;

                    }

                    @Override
                    public void onNext(ArrayList<String> value) {
                        showPics(value);
                        initBottomDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showSnackBar(tvSelectAlbum, e.getMessage(), Snackbar.LENGTH_SHORT);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void initBottomDialog() {
        mBottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(ChoosePhotoActivity.this).inflate(R.layout.dialog_bottom_sheet, null, false);
        mBottomSheetDialog.setContentView(view);
        RecyclerView recyclerView = (RecyclerView) mBottomSheetDialog.findViewById(R.id.rv_album_list);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        AlbumAdapter adapter = new AlbumAdapter(mImageBeen, this);
        adapter.setItemClickListener(new AlbumAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ArrayList<String> images) {
                showPics(images);
                mBottomSheetDialog.dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
        setBehaviorCallback();

    }

    private void setBehaviorCallback() {
        View view = mBottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        assert view != null;
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetDialog.dismiss();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void showPics(ArrayList<String> value) {
        if (mPicAdapter == null) {
            mPicAdapter = new CheckPicAdapter(ChoosePhotoActivity.this, value);
            gvPictures.setAdapter(mPicAdapter);
        } else {
            mPicAdapter.setPicPaths(value);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String imagePath = mPicAdapter.getItem(position);
        if (imagePath.equals("ic_action_camera")) {
            //拍照
            takePhoto();
        } else {
            //裁剪
            cropPic(imagePath);
        }
    }

    private void takePhoto() {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            File file = new File(ViewUtils.getAppFile(this, "images"));//获取本应用在系统的存储目录
            File mPhotoFile = new File(ViewUtils.getAppFile(this, "images/user_take.jpg"));//具体的照片
            if (!file.exists()) {
                boolean result = file.mkdir();
                if (!mPhotoFile.exists()) {

                    boolean b = mPhotoFile.createNewFile();

                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(this, "com.pandaq.pandaeye.fileprovider", mPhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
            }
            startActivityForResult(intent, ACTION_TAKE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void cropPic(String imagePath) {
        File file = new File(imagePath);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "com.pandaq.pandaeye.fileprovider", file);
            intent.setDataAndType(contentUri, "image/*");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0.1);
        intent.putExtra("aspectY", 0.1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        startActivityForResult(intent, CROP_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File mPhotoFile = new File(ViewUtils.getAppFile(this, "images/user_take.jpg"));
        switch (requestCode) {
            case CROP_PHOTO: //裁剪照片后
                if (data != null) {
                    setPicToView(data);
                }
                //裁剪后删除拍照的照片
                if (mPhotoFile.exists()) {
                    mPhotoFile.delete();
                }
                break;
            case ACTION_TAKE_PHOTO:
                if (!mPhotoFile.exists()) {
                    return;
                }
                cropPic(ViewUtils.getAppFile(this, "images/user_take.jpg"));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }

    }

    public void setPicToView(Intent picToView) {
        setResult(RESULT_OK, picToView);
        this.finish();
    }

    @OnClick({R.id.tv_select_album})
    public void onViewClicked() {
        if (mBottomSheetDialog.isShowing()) {
            mBottomSheetDialog.dismiss();
        } else {
            mBottomSheetDialog.show();
        }
    }
}
