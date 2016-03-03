/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.net.URL;
import org.opencv.core.Mat;

/**
 *
 * @author gundram
 */
public class Image {

    private URL imageUrl;
    private BufferedImage imageBufferedImage;
    private Mat imageOpenCVImage;

    private Polygon polygon;
    private String[] properties;
    private Type type;

    public enum Type {
        URL, OPEN_CV, JAVA
    }

    /**
     * This type already exists as instance in C++ <br/>
     * In C++ and Java there have to be methods to use such an image. The
     * position in the memory is accessible using
     * this.imageOpenCVImage.nativeObject
     *
     * @param image
     */
    public Image(Mat image) {
        this.imageOpenCVImage = image;
        type = Type.OPEN_CV;
    }

    /**
     * If the image should not be transfered by this instance, it is possible
     * that the image is loaded by the URL
     *
     * @param url
     */
    public Image(URL url) {
        this.imageUrl = url;
        type = Type.URL;
    }

    /**
     * as first workaround - maybe deleted later
     *
     * @param imageBufferedImage
     */
    public Image(BufferedImage imageBufferedImage) {
        this.imageBufferedImage = imageBufferedImage;
        type = Type.JAVA;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public String[] getProperties() {
        return properties;
    }

    /**
     * as first workaround
     *
     * @return
     */
    public Mat getImageOpenCVImage() {
        if (imageOpenCVImage == null) {
            throw new RuntimeException("no convertion done from " + type.toString() + " to " + Type.OPEN_CV + " implemented.");
        }
        return imageOpenCVImage;
    }

    /**
     * as first workaround
     *
     * @return
     */
    public BufferedImage getImageBufferedImage() {
        if (imageBufferedImage == null) {
            throw new RuntimeException("no convertion done from " + type.toString() + " to " + Type.JAVA + " implemented.");
        }
        return imageBufferedImage;
    }

    public URL getImageUrl() {
        if (type != Type.URL) {
            throw new RuntimeException("no url given, image was set with type " + type.toString() + ".");
        }
        return imageUrl;
    }

    /**
     * shows if a given type is supported
     *
     * @param type
     * @return
     */
    public boolean hasType(Type type) {
        switch (type) {
            case JAVA:
                return imageBufferedImage != null;
            case OPEN_CV:
                return imageOpenCVImage != null;
            case URL:
                return imageUrl != null;
            default:
                throw new RuntimeException("unknown type '" + type + "'");
        }
    }

    /**
     * create internal repesentation for the given type, if possible.
     *
     * @param toType
     */
    public void convert(Type toType) {
        switch (toType) {
            case JAVA:
//                imageBufferedImage=... create instance in Java;
                break;
            case OPEN_CV:
//                imageOpenCVImage=... create instance in C++;
                break;
            case URL:
                throw new RuntimeException("cannot convert from instance to url");
            default:
                throw new RuntimeException("unknown type '" + toType + "'");
        }
    }

}
