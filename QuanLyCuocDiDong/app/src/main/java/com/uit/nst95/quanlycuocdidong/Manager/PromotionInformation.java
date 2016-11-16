/**
 *
 */
package com.uit.nst95.quanlycuocdidong.Manager;


import java.util.Arrays;

/**
 * @author Truong Ngoc Son
 */

public class PromotionInformation {


    private String seconds;

    private String hits;

    private String more;

    private String found;

    private String from;

    private String to;


    private Results results;

    /**
     *
     */
    public PromotionInformation() {
        // TODO Auto-generated constructor stub
    }

    public PromotionInformation(String seconds, String hits, String more, String found, String from, String to,
                                Results results) {
        this.seconds = seconds;
        this.hits = hits;
        this.more = more;
        this.found = found;
        this.from = from;
        this.to = to;
        this.results = results;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getFound() {
        return found;
    }

    public void setFound(String found) {
        this.found = found;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "PromotionInformation [seconds=" + seconds + ", hits=" + hits + ", more=" + more + ", found=" + found
                + ", from=" + from + ", to=" + to + ", results=" + results + "]";
    }

    /**
     * Inner class
     */
    public static class Results {

        private Result[] results;

        public Result[] getResults() {
            return results;
        }

        public void setResults(Result[] results) {
            this.results = results;
        }

        @Override
        public String toString() {
            return "Results [results=" + Arrays.toString(results) + "]";
        }

    }

    /**
     * List  of results inside results tag
     */
    public static class Result {


        private String providerName;


        private String time;


        private String percentage;

        private String id;

        public Result() {
            // TODO Auto-generated constructor stub
        }

        public Result(String providerName, String time, String percent, String id) {
            this.providerName = providerName;
            this.time = time;
            this.percentage = percent;
            this.id = id;
        }

        public String getProviderName() {
            return providerName;
        }

        public void setProviderName(String providerName) {
            this.providerName = providerName;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percent) {
            this.percentage = percent;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Result [providerName=" + providerName + ", time=" + time + ", percent=" + percentage + ", id=" + id
                    + "]";
        }

    }

}
