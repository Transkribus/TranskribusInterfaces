/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces;

import eu.transkribus.interfaces.types.Token;

/**
 *
 * @author gundram
 * 
 */
public interface ILangMod extends IModule {

    public void setTopN(int topN);

    public Token[] nextTokens(Token[] prefix);

    public Token[] getTokens();

//    public double getProb(Token[] prefix);
}
