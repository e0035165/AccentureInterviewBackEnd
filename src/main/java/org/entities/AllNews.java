package org.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Entity
@Table(name="All_News")
@AllArgsConstructor
@Data
public class AllNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private byte[] image;

    @Column(name="subheader")
    private String subheader;

    @Column(name="news")
    private String news_path;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getSubheader() {
        return subheader;
    }

    public void setSubheader(String subheader) {
        this.subheader = subheader;
    }

    public String getNews_path() {
        return news_path;
    }

    public void setNews_path(String news_path) {
        this.news_path = news_path;
    }

    public AllNews() {

    }

    public AllNews(byte[] image, String subheader, String news_path) {
        this.image = image;
        this.subheader = subheader;
        this.news_path = news_path;
    }



    public byte[] getImage() {
        return image;
    }

    public void setImage(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        this.image = baos.toByteArray();
    }





}
