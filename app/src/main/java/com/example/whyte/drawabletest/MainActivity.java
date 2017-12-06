package com.example.whyte.drawabletest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
    }
}
