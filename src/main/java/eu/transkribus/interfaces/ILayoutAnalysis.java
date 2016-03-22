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
public interface ILayoutAnalysis {

//	public boolean processLayout(String xmlFileIn, String xmlFileOut, String[] ids);
	
	public boolean processLayout(Image image, String xmlFileIn, String xmlFileOut);

//	public boolean processLayout(Image image, String xmlFileOut);

//	public boolean processLayout(String xmlFileIn, String xmlFileOut, String[] props);

	public boolean process(Image image, String xmlFileIn, String xmlFileOut, String[] ids, String[] props);
	

    /**
     * find basic elements in the image. Each region can have some properties,
     * like id, type(line, block, ...), parent(id from parent element),
     *
     * @param image
     * @return
     */
//    public Region[] processLayout(Image image);

	/**
	 * find basic elements in the image. Each region can have some properties,
     * like id, type(line, block, ...), parent(id from parent element),
     *
	 * CVL 18.04.2016
	 * The blocks are either GT, created by a human, or the results of
	 * previous processes (e.g. a rough page estimation, text region, etc...)
	 * 
     * @param image
	 * @param blocks contains previously processed blocks
     * @return
     */
//    public Region[] processLayout(Image image, Region[] blocks);
	
	
    /**
     * for each block there can be done a line finding. The blocks should have
     * an id. The result are line which all have the property "parent=block_id"
     *
     * @param image
     * @param blocks
     * @return
     */
//    public Region[] processBaseline(Image image, Region[] blocks);

    /**
     * both methods
     * {@link eu.transkribus.interfaces.ILayoutAnalysis#processBaseline(eu.transkribus.interfaces.Image)}
     * and
     * {@link eu.transkribus.interfaces.ILayoutAnalysis#processLayout(eu.transkribus.interfaces.Image)}
     * can be applied at once.
     *
     * @param image
     * @return
     */
//    public Region[] processBaseline(Image image);

}
