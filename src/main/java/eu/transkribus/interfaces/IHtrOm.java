/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import eu.transkribus.interfaces.types.Image;
import java.io.Serializable;

/**
 * For a given input, the representation (Confmat, WordGraph,...) is calculated.
 * Probably this interface is too simple and have to be changed.
 *
 * @author gundram
 */
public interface IHtrOm {

    /**
     * in URO-CITlab this is done by an MDRNN (sprnn). Output in this case would
     * be a Confidence-Matrix.
     *
     * @param image
     * @return
     */
    public Serializable processRepresentation(Image image);

}
