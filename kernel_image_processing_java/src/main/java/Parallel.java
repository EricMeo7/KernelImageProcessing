import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;


public class Parallel {

    static float[][] kernelMatrix;
    static float multiplier;
    static int kernelLen;
    static int wid;
    static int hgt;
    static BufferedImage inputImg;
    static BufferedImage outputImg;

    static class MyThread extends Thread {
        int start;
        int end;
        MyThread(int start, int end) {
            this.start = start;
            this.end = end;
        }
        public void run() {
            processParallel(start, end);
        }
    }

    public static void processParallel(int from, int to) {
        //for (int x = from; x <= to; x++) {   // caso divisione per colonne
        for (int x = 0; x < wid; x++) {        // caso divisione per righe
            //for (int y = 0; y < hgt; y++) {  // caso divisione per colonne
            for (int y = from; y <= to; y++) { // caso divisione per righe
                float redFloat = 0f;
                float greenFloat = 0f;
                float blueFloat = 0f;

                for (int m = 0; m < kernelLen; m++) {
                    for (int n = 0; n < kernelLen; n++) {

                        int aCoordinate = (x - kernelLen / 2 + m + wid) % wid;
                        int bCoordinate = (y - kernelLen / 2 + n + hgt) % hgt;

                        int rgbTotal = inputImg.getRGB(aCoordinate, bCoordinate);

                        int rgbRed = (rgbTotal >> 16) & 0xff;
                        int rgbGreen = (rgbTotal >> 8) & 0xff;
                        int rgbBlue = (rgbTotal) & 0xff;

                        redFloat += (rgbRed * kernelMatrix[m][n]);
                        greenFloat += (rgbGreen * kernelMatrix[m][n]);
                        blueFloat += (rgbBlue * kernelMatrix[m][n]);
                    }
                }

                int redOutput = Math.min(Math.max((int) (redFloat * multiplier), 0), 255);
                int greenOutput = Math.min(Math.max((int) (greenFloat * multiplier), 0), 255);
                int blueOutput = Math.min(Math.max((int) (blueFloat * multiplier), 0), 255);

                Color color = new Color(redOutput, greenOutput, blueOutput);
                outputImg.setRGB(x, y, color.getRGB());
            }
        }
    }

    public static void process(String fileLocation, Kernel kernel) throws IOException, InterruptedException {
        Parallel.kernelLen = kernel.kernelMatrix.length;
        Parallel.inputImg = ImageIO.read(new File(fileLocation));
        Parallel.wid = inputImg.getWidth();
        Parallel.hgt = inputImg.getHeight();
        Parallel.outputImg = new BufferedImage(wid, hgt, inputImg.getType());
        Parallel.kernelMatrix = kernel.kernelMatrix;
        Parallel.multiplier = kernel.factor;


        //System.out.println("\nImage:" + fileLocation + kernel.name +" parallel start");
        // Parallel part:
        int threads = Runtime.getRuntime().availableProcessors();
        int t = threads;
        //for (int t=1 ; t<= threads; t++) {
            long start = System.currentTimeMillis();
            MyThread[] thread = new MyThread[t];
            //int chunkLength = wid / t;    // divido immagine per colonne
            int chunkLength = hgt / t;      // divido immagine per righe

            int[] bags = new int[t];
            Arrays.fill(bags, chunkLength);                     // distribute elements
            //for (int rest = wid % t; rest > 0; rest--) {      // per colonne
            for (int rest = hgt % t; rest > 0; rest--) {        // per righe
                bags[bags.length - rest] += 1;                  // distribute the rest of them
            }

            int[] rangeValues = new int[t];
            for (int i = 0; i < bags.length; i++) {
                for (int j = 0; j <= i; j++) {
                    rangeValues[i] += bags[j];
                }
            }

            // Launching threads with evenly distributed chunks:
            thread[0] = new MyThread(0, rangeValues[0] - 1);
            thread[0].start();
            for (int i = 0; i < t - 1; i++) {
                thread[i + 1] = new MyThread(rangeValues[i], rangeValues[i + 1] - 1);
                thread[i + 1].start();
            }


            // Finishing threads:
            for (int i = 0; i < t; i++) {
                thread[i].join();
            }

            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            //System.out.println("Image:" + fileLocation + kernel.name +" parallel end");
            //System.out.println("Total time parallel:" + timeElapsed) ;
            System.out.println("Total time parallel: " + fileLocation + " " + kernel.name + " Numero Threads: "  + t + " Time: " + timeElapsed);
            String outputName = fileLocation.substring(fileLocation.lastIndexOf("/") + 1).replace(".jpg", "") + "_" + kernel.name + "_parallel";

            Common.saveImage(fileLocation, outputImg, outputName);


        //}
        // Release heap memory
        outputImg.flush();
        outputImg = null;
        System.gc();
    }

}