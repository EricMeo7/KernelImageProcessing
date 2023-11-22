
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Common {

    public static void saveImage(String fileLocation, BufferedImage outputImg, String outputName) throws IOException {

        String fileOutputPath = "src/main/resources/image_output/"+ outputName;

        String extOutput = getExtension(fileLocation);

        ImageIO.write(outputImg, extOutput, new File(fileOutputPath + ".jpg"));

    }

    public static String getExtension(String fileLocation) {

        String lastChars = "";
        String extOutput;
        if (fileLocation.length() > 4) {
            lastChars = fileLocation.substring(fileLocation.length() - 4);
        }

        if (lastChars.equals(".jpg")) {
            extOutput = "JPG";
        } else if (lastChars.equals(".png")) {
            extOutput = "PNG";
        } else {

            if (fileLocation.length() > 5) {
                lastChars = fileLocation.substring(fileLocation.length() - 5);
            }

            if (lastChars.equals(".jpeg")) {
                extOutput = "JPG";
            } else {              // if nothing from above
                extOutput = "PNG";
            }
        }

        return extOutput;

    }



}