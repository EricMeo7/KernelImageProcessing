import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.File;

public class Sequential {

    public static void process(String fileLocation, Kernel kernel) throws IOException {

        int kernelLen = kernel.kernelMatrix.length;

        BufferedImage inputImg = ImageIO.read(new File(fileLocation));
        int wid = inputImg.getWidth();
        int hgt = inputImg.getHeight();

        BufferedImage outputImg = new BufferedImage(wid, hgt, inputImg.getType());

        long start = System.currentTimeMillis();
        //System.out.println("\nImage:" + fileLocation + kernel.name +" sequential start");
        for (int a = 0; a < wid; a++) {
            for (int b = 0; b < hgt; b++) {

                float redFloat = 0f;
                float greenFloat = 0f;
                float blueFloat = 0f;

                for (int m = 0; m < kernelLen; m++) {
                    for (int n = 0; n < kernelLen; n++) {

                        // calcolo delle coordinate per i pixel al bordo
                        int aCoordinate = (a - kernelLen / 2 + m + wid) % wid;
                        int bCoordinate = (b - kernelLen / 2 + n + hgt) % hgt;

                        int rgbTotal = inputImg.getRGB(aCoordinate, bCoordinate);

                        int rgbRed = (rgbTotal >> 16) & 0xff;
                        int rgbGreen = (rgbTotal >> 8) & 0xff;
                        int rgbBlue = (rgbTotal) & 0xff;

                        redFloat += (rgbRed * kernel.kernelMatrix[m][n]);
                        greenFloat += (rgbGreen * kernel.kernelMatrix[m][n]);
                        blueFloat += (rgbBlue * kernel.kernelMatrix[m][n]);
                    }
                }

                // do not allow it to be lower than 0 or greater than 255
                int redOutput = Math.min(Math.max((int) (redFloat * kernel.factor), 0), 255);
                int greenOutput = Math.min(Math.max((int) (greenFloat * kernel.factor), 0), 255);
                int blueOutput = Math.min(Math.max((int) (blueFloat * kernel.factor), 0), 255);

                // calcolo sul pixel dell'immagine
                Color color = new Color(redOutput, greenOutput, blueOutput);
                outputImg.setRGB(a, b, color.getRGB());
            }
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        //System.out.println("Image:" + fileLocation + kernel.name +" sequential  end");
        System.out.println("Total time sequential: "+ fileLocation + " "+ kernel.name +" : "+ timeElapsed) ;
        String outputName = fileLocation.substring(fileLocation.lastIndexOf("/") + 1).replace(".jpg","") + "_" + kernel.name + "_sequential" ;

        Common.saveImage(fileLocation, outputImg, outputName);

        // Release heap memory
        outputImg.flush();
        outputImg = null;
        System.gc();

    }

}