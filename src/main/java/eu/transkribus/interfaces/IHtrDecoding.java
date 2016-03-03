/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import java.io.File;
import java.io.Serializable;

/**
 * for a given ConfMat or WordGraph this algorithm calculates the output.
 * Probably the interface have to be changed, because the structure is too easy.
 *
 * @author gundram
 */
public interface IHtrDecoding {

    /**
     *
     * @param representation
     * @param properties
     * @return
     */
    public String processOutput(Serializable representation, String[] properties);

    public String processOutput(File path, String[] properties);

}
