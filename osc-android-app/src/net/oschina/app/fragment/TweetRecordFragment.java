package net.oschina.app.fragment;

import java.io.File;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.bean.Tweet;
import net.oschina.app.service.ServerTaskUtils;
import net.oschina.app.util.DensityUtils;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.TDevice;
import net.oschina.app.util.UIHelper;
import net.oschina.app.widget.RecordButton;
import net.oschina.app.widget.RecordButton.OnFinishedRecordListener;
import net.oschina.app.widget.RecordButtonUtil.OnPlayListener;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 语音动弹发布界面
 * 
 * @author kymjs(kymjs123@gmail.com)
 * 
 */
public class TweetRecordFragment extends BaseFragment {

    @InjectView(R.id.tweet_layout_record)
    RelativeLayout mLayout;
    @InjectView(R.id.tweet_img_record)
    ImageView mImgPlay;
    @InjectView(R.id.tweet_btn_record)
    RecordButton mBtnRecort;
    @InjectView(R.id.speech)
    Button mBtnSpeech;
    @InjectView(R.id.cancle)
    Button mBtnCancle;

    private AnimationDrawable background;
    private final String strSpeech = "#语音动弹#";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.speech:
            break;
        case R.id.tweet_img_record:
            mBtnRecort.setAudioPath(mBtnRecort.getCurrentAudioPath());
            mBtnRecort.startPlay();
            break;
        case R.id.cancle:
            mLayout.setVisibility(View.GONE);
            mBtnRecort.delete();
            break;
        }
    }

    @Override
    public void initView(View view) {
        RelativeLayout.LayoutParams params = (LayoutParams) mBtnRecort
                .getLayoutParams();
        params.width = DensityUtils.getScreenW(getActivity());
        params.height = (int) (DensityUtils.getScreenH(getActivity()) * 0.4);
        mBtnRecort.setLayoutParams(params);

        mBtnCancle.setOnClickListener(this);
        mBtnSpeech.setOnClickListener(this);
        mImgPlay.setOnClickListener(this);
        mBtnRecort.setOnPlayListener(new OnPlayListener() {
            @Override
            public void starPlay() {
                mImgPlay.setBackgroundDrawable(background);
                background.start();
            }

            @Override
            public void stopPlay() {
                background.stop();
                mImgPlay.setBackgroundDrawable(background.getFrame(0));
            }
        });
        mBtnRecort.setOnFinishedRecordListener(new OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath, int recordTime) {
                mLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancleRecord() {
                AppContext.showToastShort(R.string.cancle_record);
            }
        });
    }

    @Override
    public void initData() {
        background = (AnimationDrawable) mImgPlay.getBackground();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.item_tweet_pub_record,
                container, false);
        ButterKnife.inject(this, rootView);
        initView(rootView);
        initData();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pub_tweet_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.public_menu_send:
            handleSubmit(mBtnRecort.getCurrentAudioPath());
            break;
        }
        return true;
    }

    /**
     * 发布动弹
     */
    private void handleSubmit(String audioPath) {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_network_error);
            return;
        }
        if (!AppContext.getInstance().isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        if (StringUtils.isEmpty(audioPath)) {
            AppContext.showToastShort(R.string.record_sound_notfound);
            return;
        }
        File file = new File(audioPath);
        if (!file.exists()) {
            AppContext.showToastShort(R.string.record_sound_notfound);
            return;
        }
        Tweet tweet = new Tweet();
        tweet.setAuthorid(AppContext.getInstance().getLoginUid());
        tweet.setAudioPath(audioPath);
        tweet.setBody(strSpeech);
        ServerTaskUtils.pubTweet(getActivity(), tweet);
        getActivity().finish();
    }
}