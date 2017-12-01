package smokesignals.utils.query.encodedparams;

public enum Modifier {

        // All Parameters
        MISSING(":missing"),

        //String Parameters
        EXACT(":exact"),
        CONTAINS(":contains"),

        //Token Parameters
        TEXT(":text"),
        ABOVE(":above"),
        BELOW(":below"),
        IN(":in"),
        NOT_IN(":not-in"),
        NOT(":not");

        private final String mEncodedValue;

        Modifier(String encodedValue) {
            mEncodedValue = encodedValue;
        }

        public String getEncodedValue() {
            return mEncodedValue;
        }

}
