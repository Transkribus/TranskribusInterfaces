
#pragma once

#include <vector>

#include "IModule.h"

namespace transkribus {

class Image;

/// <summary>
/// This interface controls the Writer Retrieval Module.
/// </summary>
class IWriterRetrieval : public IModule {

public:
	virtual ~IWriterRetrieval() {};

	/// <summary>
	/// Processes the WR feature for one images and writes it to the PAGE XML.
	/// </summary>
	/// <param name="image">The image.</param>
	/// <param name="xmlInOut">The XML input (regions).</param>
	/// <param name="ids">The settings IDs.</param>
	/// <param name="props">The settings values.</param>
	/// <returns>feature vector with comma separated values.</returns>
	virtual std::string process(
		const Image& image, 
		const std::string& xmlIn, 
		const std::vector<std::string>& ids = std::vector<std::string>(), 
		const std::vector<std::string>& props = std::vector<std::string>()) = 0;

	/// <summary>
	/// Computes all distances of the features (i.e document images).
	/// Hence, the matrix returns the similarity of the handwriting.
	/// </summary>
	/// <param name="features">a NxM matrix with N features (1 feature per unit/page) of M dimensions.</param>
	/// <param name="ids">The settings IDs.</param>
	/// <param name="props">The settings values.</param>
	/// <returns>A NxN matrix (usually symetric) with distances for all images.</returns>
	virtual Image distances(
		const Image& features, 
		const std::vector<std::string>& ids = std::vector<std::string>(), 
		const std::vector<std::string>& props = std::vector<std::string>()) = 0;

	/// <summary>
	/// Trains the Writer Retrieval Module.
	/// </summary>
	/// <param name="features">a vector containing all features per unit (e.g. page)
	/// The features are a NxP matrix with N features of P dimensions (NOTE: P != M).</param>
	/// <param name="ids">The settings IDs (must at least contain the path where the model file is written).</param>
	/// <param name="props">The settings values.</param>
	/// <returns>
	/// A NxN matrix (usually symetric) with distances for all images.
	/// </returns>
	virtual Image train(
		const std::vector<Image>& features, 
		const std::vector<std::string>& ids, 
		const std::vector<std::string>& props) = 0;

};


}
