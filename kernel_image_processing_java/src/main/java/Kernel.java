public class Kernel {
    float[][] kernelMatrix;
    String name;
    float factor;
    public Kernel(float [][] kernelMatrix, String name, float factor){
        this.kernelMatrix=kernelMatrix;
        this.name=name;
        this.factor=factor;
    }
}
