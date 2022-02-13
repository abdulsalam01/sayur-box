package com.sayur.tobos.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sayur.tobos.R;

public class Toolbars extends LinearLayout {
    private boolean tb_back = false;
    private String tb_title = "";
    private boolean tb_shadow = true;

    public Toolbars(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.toolbars, this);
    }

    public Toolbars(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Toolbars(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /*GET SET*/
    public boolean isTb_back() {
        return tb_back;
    }

    public void setTb_back(boolean tb_back) {
        this.tb_back = tb_back;
    }

    public String getTb_title() {
        return tb_title;
    }

    public void setTb_title(String tb_title) {
        this.tb_title = tb_title;
    }

    public boolean isTb_shadow() {
        return tb_shadow;
    }

    public void setTb_shadow(boolean tb_shadow) {
        this.tb_shadow = tb_shadow;
    }

    /*INIT*/
    public void init(Context context, @Nullable AttributeSet attrs){
        TypedArray styleAttr = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Toolbars,
                0,
                0
        );

        try {
            tb_back = styleAttr.getBoolean(R.styleable.Toolbars_tb_back, false);
            tb_title = styleAttr.getString(R.styleable.Toolbars_tb_title);
            tb_shadow = styleAttr.getBoolean(R.styleable.Toolbars_tb_shadow, true);
        }finally {
            styleAttr.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.toolbars, this);

        /*VIEW*/
        ImageView tb_back_comp = this.findViewById(R.id.tb_back);
        tb_back_comp.setVisibility(tb_back ? VISIBLE : GONE);

        TextView tb_title_comp = this.findViewById(R.id.tb_title);
        tb_title_comp.setText(tb_title);

        View tb_shadow_comp = this.findViewById(R.id.tb_shadow);
        tb_shadow_comp.setVisibility(tb_shadow ? VISIBLE : GONE);

        /*CLICK*/
        tb_back_comp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onToolbarsClick.onBackClick();
            }
        });
    }

    public interface OnToolbarsClick{
        void onBackClick();
    }

    OnToolbarsClick onToolbarsClick;
    public void setOnToolbarsClick(OnToolbarsClick onToolbarsClick){
        this.onToolbarsClick = onToolbarsClick;
    }
}
