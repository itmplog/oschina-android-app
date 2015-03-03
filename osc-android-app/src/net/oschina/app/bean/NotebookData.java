package net.oschina.app.bean;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 便签数据bean（有重载equals()方法）
 * 
 * @author kymjs (https://github.com/kymjs)
 * 
 */
@XStreamAlias("sticky")
public class NotebookData extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    @XStreamAlias("id")
    private int id;
    @XStreamAlias("iid")
    private int iid;
    @XStreamAlias("timestamp")
    private long unixTime;
    @XStreamAlias("updateTime")
    private String date;
    @XStreamAlias("content")
    private String content;
    @XStreamAlias("color")
    private String colorText;

    private int color;

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        } else {
            if (o instanceof NotebookData) {
                NotebookData data = (NotebookData) o;
                try {
                    return (this.id == data.getId())
                            && (this.iid == data.getIid())
                            && (this.unixTime == data.getUnixTime())
                            && (this.date.equals(data.getDate()))
                            && (this.content == data.getContent())
                            && (this.color == data.getColor());
                } catch (NullPointerException e) {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long time) {
        this.unixTime = time;
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String color) {
        this.colorText = color;
    }

    public int getColor() {
        if (this.color == 0) { // 客户端始终以当前手机上的颜色为准
            if ("blue".equals(colorText)) {
                this.color = 3;
            } else if ("red".equals(colorText)) {
                this.color = 2;
            } else if ("yellow".equals(colorText)) {
                this.color = 1;
            } else if ("purple".equals(colorText)) {
                this.color = 4;
            } else if ("green".equals(colorText)) {
                this.color = 0;
            }
        }
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}