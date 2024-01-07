#include <iostream>
#include <chrono>
#include <string>
#include "image.h"
#include "kernel.h"
using namespace std;


#define GAUSSIAN        	"gaussian"
#define SHARPENING      	"sharpen"
#define EDGE  				"edge_detect"
#define LAPLACIAN     		"laplacian"
#define GAUSSIAN_LAPLACIAN  "gaussian_laplacian"
#define LAST			"last"

#define CUDA_GLOBAL		"global"
#define CUDA_CONSTANT	"constant"
#define CUDA_SHARED		"shared"

#define OUTPUT_FOLDER   "image_output/"
#define IMAGE_EXT       ".jpg"


int main() 
{
	string filterType[5] = {"GAUSSIAN", "SHARPEN", "EDGE", "LAPLACIAN", "GAUSSIAN_LAPLACIAN"};
	Image img;
	img.loadImage("image_input/image_4k.png");
	CudaMemType cudaType = CudaMemType::SHARED;
	//CudaMemType cudaType = CudaMemType::GLOBAL;
  
	for (int i = 0; i < 5; i++)
	{
 
		Kernel filter = Kernel();
		
		if (filterType[i] == "GAUSSIAN"){
			filter.setGaussianFilter(7, 7, 1);
		} else if (filterType[i] == "SHARPEN"){
			filter.setSharpenFilter();
		} else if (filterType[i] == "EDGE"){
			filter.setEdgeDetectionFilter();
		} else if (filterType[i] == "LAPLACIAN"){
			filter.setLaplacianFilter();
		} else if (filterType[i] == "GAUSSIAN_LAPLACIAN"){
			filter.setGaussianLaplacianFilter();
		} else {
			filter.setGaussianFilter(5, 5, 2);
			
		}
		
		Image newMtImg;
		Image newNpImg;

		// Init the CUDA device
		cudaFree(0);	

		
		// Executing image processing
		auto start = std::chrono::high_resolution_clock::now();
		bool cudaResult = img.imageProcessing(newMtImg, filter, cudaType);
		auto end = std::chrono::high_resolution_clock::now();

		std::cout << std::endl;


		// Evaluating execution times and save results
		if (cudaResult) {
			auto multithreadDuration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
			std::cout << "Total CUDA Execution time: " << multithreadDuration << " μs" << std::endl;
			newMtImg.saveImage(std::string(std::string(OUTPUT_FOLDER) + 
								"result_" + filterType[i] +
								std::string(IMAGE_EXT)).c_str());
		}

	}
}
