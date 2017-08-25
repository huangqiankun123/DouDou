package com.example.administrator.doudou.modules.zhihu.home.mvp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PandaQ on 2016/9/13.
 * email : 767807368@qq.com
 */
public class ZhiHuDaily implements Serializable {


    /**
     * date : 20170502
     * stories : [{"images":["https://pic3.zhimg.com/v2-06ff7ed5611a7efa699098b595ed39c6.jpg"],"type":0,"id":9394120,"ga_prefix":"050210","title":"有限责任公司的责任到底是多「有限」？"},{"images":["https://pic4.zhimg.com/v2-b44636ccd2affac97ccc0759a0f46f7f.jpg"],"type":0,"id":9393197,"ga_prefix":"050209","title":"「古墓就那么多，挖完了该怎么办？」"},{"images":["https://pic4.zhimg.com/v2-09c89dbe1ea44a8bbada2175e419b2db.jpg"],"type":0,"id":9393178,"ga_prefix":"050208","title":"拖了 30 年，那本能教你认识每一种「云」的书终于更新了"},{"title":"- 猪哪个部位的肉最好吃？\r\n- 哪里不好吃？","ga_prefix":"050207","images":["https://pic4.zhimg.com/v2-1eda9d1c2bdb39e1c0488abcea9fe813.jpg"],"multipic":true,"type":0,"id":9369814},{"images":["https://pic3.zhimg.com/v2-dddb32a335d539d5d10c4a8579f12d4e.jpg"],"type":0,"id":9393271,"ga_prefix":"050207","title":"用了谷歌和亚马逊的智能助手，发现这是属于未来的体验"},{"images":["https://pic1.zhimg.com/v2-9e729e7fa3cb767154da06bf79c03b9c.jpg"],"type":0,"id":9393254,"ga_prefix":"050207","title":"传销的骗人之处，在于它一开始就注定要破灭"},{"images":["https://pic3.zhimg.com/v2-ef7b3b190112833d6eed0d06c0800a9e.jpg"],"type":0,"id":9390083,"ga_prefix":"050206","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic1.zhimg.com/v2-a84ef94545a3d7cde1335253a059df90.jpg","type":0,"id":9393178,"ga_prefix":"050208","title":"拖了 30 年，那本能教你认识每一种「云」的书终于更新了"},{"image":"https://pic1.zhimg.com/v2-2c8a4ad3384d7eded55adc35f12e81bc.jpg","type":0,"id":9393271,"ga_prefix":"050207","title":"用了谷歌和亚马逊的智能助手，发现这是属于未来的体验"},{"image":"https://pic4.zhimg.com/v2-c3e5e53cb92086affc9f4a051f2ff567.jpg","type":0,"id":9392088,"ga_prefix":"050107","title":"奶白色的汤是怎么做出来的？真的会「营养翻倍」吗？"},{"image":"https://pic2.zhimg.com/v2-814884854b37e201a6124f0fa24995cd.jpg","type":0,"id":9390361,"ga_prefix":"043007","title":"刘看山 · 白色皮毛下的秘密"},{"image":"https://pic3.zhimg.com/v2-430ce94c5a87e98a7ed7a2d20f0ca85e.jpg","type":0,"id":9383848,"ga_prefix":"042706","title":"这里是广告 · 从电影的世界里看 AI"}]
     */

    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic3.zhimg.com/v2-06ff7ed5611a7efa699098b595ed39c6.jpg"]
         * type : 0
         * id : 9394120
         * ga_prefix : 050210
         * title : 有限责任公司的责任到底是多「有限」？
         * multipic : true
         */
        private String data;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private boolean multipic;
        private List<String> images;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean {
        /**
         * image : https://pic1.zhimg.com/v2-a84ef94545a3d7cde1335253a059df90.jpg
         * type : 0
         * id : 9393178
         * ga_prefix : 050208
         * title : 拖了 30 年，那本能教你认识每一种「云」的书终于更新了
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
