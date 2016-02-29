/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageTypeSpecifier;

/**
 *
 * @author gundram
 */
public class Image {

    private long imagePointer;
    private byte[] imageData;
    private int imageWidth;
    private int imageHeight;
    private URL imageUrl;
    private BufferedImage imageBufferedImage;

    private Polygon polygon;
    private String[] properties;

    /**
     * This type will also exist as instance in C++ <br/>
     * Used for Java and C to refer to an image in the memory. In C++ and Java
     * there have to be methods to load such an image
     *
     * @param imagePointer Pointer to the image in memory
     * @param imageWidth
     * @param imageHeight
     */
    public Image(long imagePointer, int imageWidth, int imageHeight) {
        this.imagePointer = imagePointer;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }

    /**
     * If the image should not be transfered by this instance, it is possible
     * that the image is loaded by the URL
     *
     * @param url
     */
    public Image(URL url) {
        this.imageUrl = url;
    }

    /**
     * as first workaround - maybe deleted later
     *
     * @param imageBufferedImage
     */
    public Image(BufferedImage imageBufferedImage) {
        this.imageBufferedImage = imageBufferedImage;
    }

    /**
     * when the image is neighter in local memory nor online, it has to be
     * transfered directly. It has to be discussed, which format is the best and
     * which image depth is enough (byte, float,...)
     *
     * @param imageData
     * @param imageWidth
     * @param imageHeight
     */
    public Image(byte[] imageData, int imageWidth, int imageHeight) {
        this.imageData = imageData;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public long getImagePointer() {
        return imagePointer;
    }

    /**
     * as first workaround - maybe deleted later
     *
     * @return
     */
    public BufferedImage getImageBufferedImage() {
        return imageBufferedImage;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public String[] getProperties() {
        return properties;
    }

}
