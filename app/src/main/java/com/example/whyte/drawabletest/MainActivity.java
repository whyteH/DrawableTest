package com.example.whyte.drawabletest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.whyte.drawabletest.lrc.LrcView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        GradientDrawable gradientDrawable1 = new GradientDrawable();// 形状-圆角矩形
//        gradientDrawable1.setColor(Color.TRANSPARENT);
//        gradientDrawable1.setStroke(1, Color.RED);
//
//
////        GradientDrawable gradientDrawable2 = new GradientDrawable();// 形状-圆角矩形
////        gradientDrawable2.setShape(GradientDrawable.RECTANGLE);// 圆角
////        gradientDrawable2.setSize(ViewGroup.LayoutParams.MATCH_PARENT, 1);
////        gradientDrawable2.setColor(Color.parseColor("#EAEAEA"));
////        gradientDrawable2.setBounds(-2, -2, -2, 0);
//        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{gradientDrawable1});
//        layerDrawable.setLayerInset(0, -2, -2, -2, 0);
//        XQuickClearAndPasswordLayout linearLayout = findViewById(R.id.ll_container);
//        linearLayout.setBackgroundDrawable(layerDrawable);

        PrefixedEditText prefixedEditText = (PrefixedEditText) findViewById(R.id.et_test);
        prefixedEditText.setPrefix("+186");
        prefixedEditText.setPrefixTextColor(Color.parseColor("#FF00a8ff"));
        prefixedEditText.setPrefixClickListener(new PrefixedEditText.OnPrefixClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        LrcView lrcView = (LrcView) findViewById(R.id.lrc_view);

        lrcView.setLrc("\n" +
                "[00:26.37]漫天的话语纷乱落在耳际\n" +
                "\n" +
                "[00:32.10]你我沉默不回应\n" +
                "\n" +
                "[00:38.59]牵你的手 你却哭红了眼睛\n" +
                "\n" +
                "[00:44.19]路途漫长无止尽\n" +
                "\n" +
                "[00:49.77]多想提起勇气 好好地呵护你\n" +
                "\n" +
                "[00:55.59]不让你受委屈 苦也愿意\n" +
                "\n" +
                "[01:01.87]那些痛的记忆 落在春的泥土里\n" +
                "\n" +
                "[01:07.93]滋养了大地 开出下一个花季\n" +
                "\n" +
                "[01:14.10]风中你的泪滴 滴滴落在回忆里\n" +
                "\n" +
                "[01:20.21]让我们取名叫做珍惜\n" +
                "\n" +
                "[01:36.49]迷雾散尽 一切终于变清晰\n" +
                "\n" +
                "[01:42.31]爱与痛都成回忆\n" +
                "\n" +
                "[01:48.54]遗忘过去 繁花灿烂在天际\n" +
                "\n" +
                "[01:54.57]等待已有了结局\n" +
                "\n" +
                "[01:59.93]我会提起勇气 好好地呵护你\n" +
                "\n" +
                "[02:06.06]不让你受委屈 苦也愿意\n" +
                "\n" +
                "[02:12.14]那些痛的记忆 落在春的泥土里\n" +
                "\n" +
                "[02:18.23]滋养了大地 开出下一个花季\n" +
                "\n" +
                "[02:24.38]风中你的泪滴 滴滴落在回忆里\n" +
                "\n" +
                "[02:30.56]让我们取名叫做珍惜\n" +
                "\n" +
                "[02:34.67]\n" +
                "\n" +
                "[02:58.22]漫天纷飞的话语 落在春的泥土里\n" +
                "\n" +
                "[03:04.11]滋养了大地 开出下一个花季\n" +
                "\n" +
                "[03:10.21]风中你的泪滴\n" +
                "\n" +
                "[03:13.28]滴滴落在回忆里\n" +
                "\n" +
                "[03:16.27]让我们取名叫做珍惜\n" +
                "\n" +
                "[03:22.77]那些痛的记忆 落在春的泥土里\n" +
                "\n" +
                "[03:28.48]滋养了大地 开出下一个花季\n" +
                "\n" +
                "[03:34.72]风中你的泪滴 滴滴落在回忆里\n" +
                "\n" +
                "[03:42.06]让我们取名叫做珍惜\n" +
                "\n" +
                "[03:48.66]让我们懂得学会珍惜\n");

        lrcView.update(98274);

    }
}
