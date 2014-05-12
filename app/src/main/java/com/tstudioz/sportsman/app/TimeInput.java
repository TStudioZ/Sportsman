package com.tstudioz.sportsman.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Tomáš Zahálka on 12. 5. 2014.
 */
public class TimeInput extends LinearLayout {

    public int getMaxHours() {
        return maxHours;
    }

    public void setMaxHours(int maxHours) {
        this.maxHours = maxHours;
        invalidate();
        requestLayout();
    }

    public boolean isShowTwoDigits() {
        return showTwoDigits;
    }

    public void setShowTwoDigits(boolean showTwoDigits) {
        this.showTwoDigits = showTwoDigits;
        invalidate();
        requestLayout();
    }

    public void setHours(int hours) {
        editTextHours.setText(String.format("%02d", hours));
        invalidate();
        requestLayout();
    }

    public void setMinutes(int minutes) {
        editTextMinutes.setText(String.format("%02d", minutes));
        invalidate();
        requestLayout();
    }

    public void setSeconds(int seconds) {
        editTextSeconds.setText(String.format("%02d", seconds));
        invalidate();
        requestLayout();
    }

    public void setTime(long miliseconds) {
        long seconds = miliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;

        setHours((int) hours);
        setMinutes((int) minutes);
        setSeconds((int) seconds);
    }

    private int maxHours;
    private boolean showTwoDigits;

    private EditText editTextHours;
    private EditText editTextMinutes;
    private EditText editTextSeconds;

    public long getMiliseconds() {
        return getHours() * 3600000l + getMinutes() * 60000l + getSeconds() * 1000l;
    }

    public int getHours() {
        int hours = -1;
        try {
            hours = Integer.parseInt(editTextHours.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return hours;
    }

    public int getMinutes() {
        int hours = -1;
        try {
            hours = Integer.parseInt(editTextMinutes.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return hours;
    }

    public int getSeconds() {
        int hours = -1;
        try {
            hours = Integer.parseInt(editTextSeconds.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return hours;
    }

    public TimeInput(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.TimeInput, 0, 0);

        try {
            maxHours = typedArray.getInt(R.styleable.TimeInput_maxHours, 99);
            showTwoDigits = typedArray.getBoolean(R.styleable.TimeInput_showTwoDigits, true);
        } finally {
            typedArray.recycle();
        }

        InputFilter filterSixty = new RangeInputFilter(0, 59);
        InputFilter filterHours = new RangeInputFilter(0, maxHours);

        LayoutInflater.from(context).inflate(R.layout.time_input, this);
        editTextHours = (EditText) findViewById(R.id.hours);
        editTextHours.setFilters(new InputFilter[] {filterHours});

        editTextMinutes = (EditText) findViewById(R.id.minutes);
        editTextMinutes.setFilters(new InputFilter[] {filterSixty});

        editTextSeconds = (EditText) findViewById(R.id.seconds);
        editTextSeconds.setFilters(new InputFilter[] {filterSixty});
    }

    private class RangeInputFilter implements InputFilter {

        private final int min;
        private final int max;

        private RangeInputFilter(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence charSequence, int start, int end, Spanned spanned, int dstart, int dend) {
            try {
                int number = Integer.parseInt(spanned.toString() + charSequence.toString());
                if (number >= min && number <= max)
                    return null;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
