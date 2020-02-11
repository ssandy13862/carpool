package com.example.dec23_carpool.util;

import java.util.List;

public class PlacesAutoCompleteAddress {

    private List<Predictions> predictions;

    public List<Predictions> getPredictions() {
        return predictions;
    }

    public class Predictions {

        private String description;
        private StructuredFormatting structured_formatting;

        public String getDescription() {
            return description;
        }

        public StructuredFormatting getStructured_formatting() {
            return structured_formatting;
        }

        public class StructuredFormatting {
            private String main_text;

            public String getMain_text() {
                return main_text;
            }
        }
    }
}
