package hackernews.android.beans;

/**
 * Created by viki on 8/9/17.
 */

public class Analytics {


    public static class Param {
        public static final String PAGE = "page";
        public static final String WHAT = "what";
        public static final String TIME_SPENT = "time_spent";
        public static final String SESSION_NAME = "session_name";
        public static final String EVENT_COUNTER = "event_counter";
        public static final String UTC_TIME = "time";

        protected Param() {
        }
    }

    public static class Event {
        public static final String SCREEN_VIEW = "screen_view";
        public static final String SCREEN_EXIT = "screen_exit";
        public static final String CLICK = "click";
        public static final String API_LOAD_SUCCESS = "api_success";
        public static final String API_LOAD_FAIL = "api_fail";

        protected Event() {
        }
    }
}
