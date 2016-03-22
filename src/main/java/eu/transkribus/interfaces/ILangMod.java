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
 */
public interface ILangMod {

    public void setTopN(int topN);

    public Token[] nextTokens(Token[] prefix);

    public Token[] getTokens();
    
    public String usage();
    public String getToolName();
    public String getVersion();
    public String getProvider();

//    public double getProb(Token[] prefix);
}
