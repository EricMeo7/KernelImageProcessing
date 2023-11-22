import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        ArrayList<Kernel> kernels = new ArrayList<Kernel>();
        float[][] kernelMatrix;
        // Edge Detection
        kernelMatrix = new float[][]{
                {-1, -1, -1},
                {-1, 8, -1},
                {-1, -1, -1}
        };
        kernels.add(new Kernel(kernelMatrix, "EdgeDetection3x3",1));

        // Sharpen
        /*kernelMatrix = new float[][]{
                {0, -1, 0},
                {-1, 5, -1},
                {0, -1, 0}
        };
        kernels.add(new Kernel(kernelMatrix, "Sharpen3x3",1));

        // Box Blur
        kernelMatrix = new float[][]{
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        };
        kernels.add(new Kernel(kernelMatrix, "BoxBlur3x3", 1f / 9f));

        // Gaussian Blur 3x3
        kernelMatrix = new float[][]{
                {-1, -1, -1},
                {-1, 8, -1},
                {-1, -1, -1}
        };
        kernels.add(new Kernel(kernelMatrix, "GaussianBlur3x3", 1f / 16f));*/

        /*// Gaussian Blur 5x5
        kernelMatrix = new float[][]{
                {1, 4, 6, 4, 1},
                {4, 16, 24, 16, 4},
                {6, 24, 36, 24, 6},
                {4, 16, 24, 16, 4},
                {1, 4, 6, 4, 1}
        };
        kernels.add(new Kernel(kernelMatrix, "GaussianBlur5x5", -(1f / 256f)));
        */
        // Smoothing
        kernelMatrix = new float[][]{
                {2, 4, 5, 4, 2},
                {4, 9, 12, 9, 4},
                {5, 12, 15, 12, 5},
                {4, 9, 12, 9, 4},
                {2, 4, 5, 4, 2}
        };
        kernels.add(new Kernel(kernelMatrix, "Smoothing5x5", (1f / 159f)));

        // Gaussian 7x7
        kernelMatrix = new float[][]{
                {1, 4, 8, 10, 8, 4, 1},
                {4, 12, 24, 30, 24, 12, 4},
                {8, 24, 47, 59, 47, 24, 8},
                {1, 30, 59, 73, 59, 30, 10},
                {8, 24, 47, 59, 47, 24, 8},
                {4, 12, 24, 30, 24, 12, 4},
                {1, 4, 8, 10, 8, 4, 1}
        };
        kernels.add(new Kernel(kernelMatrix, "Gaussian7x7",(1f / 1000f)));

        File pathOutput = new File("src/main/resources/image_output");
        for(File file: pathOutput.listFiles())
            if (!file.isDirectory())
                file.delete();

        File path = new File("src/main/resources/image_input/");
        String originalFilePath = "src/main/resources/image_input/";
        File [] files = path.listFiles();   
        for (int i = 0; i < files.length; i++){
            if (files[i].isFile()){
                for (int k = 0; k < kernels.size(); k++) {
                    Sequential.process(originalFilePath + files[i].getName(), kernels.get(k));
                    Parallel.process(originalFilePath + files[i].getName(), kernels.get(k));
                }
            }
        }
    }

}