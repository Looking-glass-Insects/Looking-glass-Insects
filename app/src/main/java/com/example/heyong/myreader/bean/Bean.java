package com.example.heyong.myreader.bean;

import java.util.List;

/**
 * Created by Heyong on 2017/4/6.
 * @see com.example.heyong.myreader.net.HomeData
 */

public class Bean {


    /**
     * error : false
     * results : [{"_id":"58e5bd03421aa90d6bc75ac4","createdAt":"2017-04-06T11:58:59.427Z","desc":"折叠效果的 UICollectionView Swift Controller。","images":["http://img.gank.io/ba565163-f79d-43c4-94c4-4c2b1fdede98"],"publishedAt":"2017-04-06T12:06:10.17Z","source":"chrome","type":"iOS","url":"https://github.com/Ramotion/gliding-collection","used":true,"who":"带马甲"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * _id : 58e5bd03421aa90d6bc75ac4
         * createdAt : 2017-04-06T11:58:59.427Z
         * desc : 折叠效果的 UICollectionView Swift Controller。
         * images : ["http://img.gank.io/ba565163-f79d-43c4-94c4-4c2b1fdede98"]
         * publishedAt : 2017-04-06T12:06:10.17Z
         * source : chrome
         * type : iOS
         * url : https://github.com/Ramotion/gliding-collection
         * used : true
         * who : 带马甲
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
