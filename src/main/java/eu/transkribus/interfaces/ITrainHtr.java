public interface ITrainHtr{
  
  public void trainHtr( String pathToModelsIn, String pathToModelsOut, String[] props, String inputDir);    

  public void createTrainData(String[] pageXmls, String outputDir, IBaseline2Coords mapper);
}
