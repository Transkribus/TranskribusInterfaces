package eu.transkribus.interfaces;

public interface ITrainHtr{
  
  public void trainHtr( String pathToModelsIn, String pathToModelsOut, String[] props, String inputDir);    

  public void createTrainData(String[] pageXmls, String outputDir, IBaseLine2Coords mapper);
}
