/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import eu.transkribus.interfaces.types.Image;

/**
 * This is just a starting point of discussing - NCSR,CVL should change it as
 * they want. Please leave the structure shallow, otherwise an easy
 * JNI-interface is not possible. For this interface there will be a wrapper to
 * C++ which cares about the transformation of the types from C++ to Java and
 * vice versa.
 *
 * @author gundram
 */
public interface ILayoutAnalysis extends IModule {

//    public boolean processLayout(Image image, String xmlFileIn, String xmlFileOut);

    public void process(Image image, String xmlInOut, String[] ids, String[] props);

}
