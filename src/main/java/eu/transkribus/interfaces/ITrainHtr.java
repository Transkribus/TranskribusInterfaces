package eu.transkribus.interfaces;

public interface ITrainHtr extends IModule {

    public void trainHtr(String pathToModelsIn, String pathToModelsOut, String[] props, String inputDir);

    public void createTrainData(String[] pageXmls, String outputDir, String[] props);

}
