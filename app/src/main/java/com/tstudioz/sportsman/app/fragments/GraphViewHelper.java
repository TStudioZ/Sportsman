package com.tstudioz.sportsman.app.fragments;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;

/**
 * Created by Tomáš Zahálka on 14. 5. 2014.
 */
public class GraphViewHelper {

    public static class GraphViewFormatter implements CustomLabelFormatter {
        @Override
        public String formatLabel(double value, boolean isValueX) {
            if (isValueX) {

            }
            return null;
        }
    }

    public static class GraphViewDataWithTimestamp extends GraphView.GraphViewData {
        private String timestamp;
        public GraphViewDataWithTimestamp(double valueX, double valueY, String timestamp) {
            super(valueX, valueY);
            this.timestamp = timestamp;
        }
    }

}
