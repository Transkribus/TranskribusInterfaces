/*
 *  To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces.types;

import java.awt.Polygon;

/**
 * This type will also exist as instance in C++ <br/>
 * region (block, table, column, line, word,...) containing one specific type.
 * Using the properties it is possible to describe the region.
 *
 * @author gundram
 */
public class Region {

//    public String id;
    private Polygon polygon;
    private String[] properties;
    private String transcription;

    public Region(Polygon polygon, String[] properties) {
        this.polygon = polygon;
        this.properties = properties;
    }

    public Region(Polygon polygon, String[] properties, String transcription) {
        this.polygon = polygon;
        this.properties = properties;
        this.transcription = transcription;
    }
    
//    public Region(int[] x, int[] y, int n, String[] properties) {
//        this(new Polygon(x, y, n), properties);
//    }
    public Region(Polygon polygon) {
        this(polygon, null);
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public String[] getProperties() {
        return properties;
    }

    public String getTranscription(){
    	return transcription;
    }
}
