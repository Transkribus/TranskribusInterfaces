package eu.transkribus.interfaces;

public interface ITrainHtr extends IModule {

    /*
    props:
     Threads=<POS_NUMBER>;
     TrainSizePerEpoch=<POS_NUMBER>;     
     EarlyStopping=<POS_NUMBER>;
     NumEpochs=<POS_NUMBER>;
     LearningRate=<DOUBLE>;
     Noise=<NO|PREPROC|NET|BOTH>;
     HMM:
     CovergenceThresh=<Double>;
     */
    public void trainHtr(
            String pathToModelsIn,
            String pathToModelsOut,
            String inputTrainDir,
            String inputValDir,
            String[] props);

    /*
    pathToCharMapFile:File with List of channels. Defined as <CODE>=<CHANNEL-NO>.
    If in <CODE> a "=" or a "\" is written, it has to be escaped by "\".
    props:
    HMM:
     FeatDimension:<POS_NUMBER>;
     NumStatesPerHMMs=<POS_NUMBER>;
     TransitionLoopProb=<Double>;
    MDRNN:
     NumFeatMaps:<POS_NUMBER>;
     RelScaleHiddenUnits=<Double>;
     */
    public void createHtr(
            String pathToModelsOut,
            String pathToCharacterMap,
            String[] props);

    /*
    images are in parent folder of the PageXml-file
    with same name saved as "imageFilename" in the PageXml-file
     */
    public void createTrainData(
            String[] pageXmls,
            String outputDir,
            String pathToCharacterMap,
            String[] props);

}
