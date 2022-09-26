import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture p;
    private int W, H;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.p = new Picture(picture);
        p.setOriginUpperLeft();
        this.W = picture.width();
        this.H = picture.height();
    }

    // current picture
    public Picture picture() {
        return this.p;
    }

    // width of current picture
    public int width() {
        return this.W;
    }

    // height of current picture
    public int height() {
        return this.H;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > W - 1 || y < 0 || y > H - 1) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || y == 0 || x == W - 1 || y == H - 1) {
            return 1000;
        }
        Color left = p.get(x - 1, y);
        Color right = p.get(x + 1, y);
        Color up = p.get(x, y - 1);
        Color down = p.get(x, y + 1);
        double deltax = Math.pow((left.getRed() - right.getRed()), 2)
                + Math.pow((left.getGreen() - right.getGreen()), 2)
                + Math.pow((left.getBlue() - right.getBlue()), 2);
        double deltay = Math.pow((up.getRed() - down.getRed()), 2)
                + Math.pow((up.getGreen() - down.getGreen()), 2)
                + Math.pow((up.getBlue() - down.getBlue()), 2);
        double energy = Math.sqrt(deltax + deltay);
        return energy;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] totalweight = new double[this.W][this.H];
        int[][] path = new int[this.W][this.H];

        // fill in left column as initial energy
        for (int i = 0; i < this.H; i++) {
            totalweight[0][i] = 1000;
        }
        // start from column 1 and find the least weight path among 3 continuous point
        for (int col = 1; col < W; col++) {
            for (int row = 0; row < H; row++) {
                if (row == 0 || row == H - 1) {
                    totalweight[col][row] = totalweight[col - 1][row] + energy(col, row);
                    path[col][row] = row;
                } else {
                    double min = totalweight[col - 1][row - 1];
                    int prevrow = row - 1;
                    if (min > totalweight[col - 1][row]) {
                        min = totalweight[col - 1][row];
                        prevrow = row;
                    }
                    if (min > totalweight[col - 1][row + 1]) {
                        min = totalweight[col - 1][row + 1];
                        prevrow = row + 1;
                    }
                    totalweight[col][row] = min + energy(col, row);
                    path[col][row] = prevrow;
                }
            }
        }
        // find the least weight amoung right column
        double minweight = 1000 * this.W;
        int minpath = 0;
        for (int i = 0; i < this.H; i++) {
            if (totalweight[this.W - 1][i] < minweight) {
                minweight = totalweight[W - 1][i];
                minpath = i;
            }
        }
        int[] horSeam = new int[this.W];
        horSeam[this.W - 1] = minpath;
        for (int j = this.W - 2; j >= 0; j--) {
            horSeam[j] = path[j + 1][horSeam[j + 1]];
        }

        return horSeam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] totalweight = new double[this.W][this.H];
        int[][] path = new int[this.W][this.H];

        // fill in top column as initial energy
        for (int i = 0; i < this.W; i++) {
            totalweight[i][0] = 1000;
        }
        // start from column 1 and find the least weight path among 3 continuous point
        for (int row = 1; row < H; row++) {
            for (int col = 0; col < W; col++) {
                if (col == 0 || col == W - 1) {
                    totalweight[col][row] = totalweight[col][row - 1] + energy(col, row);
                    path[col][row] = col;
                } else {
                    double min = totalweight[col - 1][row - 1];
                    int prevcol = col - 1;
                    if (min > totalweight[col][row - 1]) {
                        min = totalweight[col][row - 1];
                        prevcol = col;
                    }
                    if (min > totalweight[col + 1][row - 1]) {
                        min = totalweight[col + 1][row - 1];
                        prevcol = col + 1;
                    }
                    totalweight[col][row] = min + energy(col, row);
                    path[col][row] = prevcol;
                }
            }
        }
        // find the least weight amoung bottom column
        double minweight = 1000 * this.H;
        int minpath = 0;
        for (int i = 0; i < this.W; i++) {
            if (totalweight[i][this.H - 1] < minweight) {
                minweight = totalweight[i][H - 1];
                minpath = i;
            }
        }
        int[] verSeam = new int[this.H];
        verSeam[this.H - 1] = minpath;
        for (int j = this.H - 2; j >= 0; j--) {
            verSeam[j] = path[verSeam[j + 1]][j + 1];
        }
        return verSeam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != this.W) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (seam[i] < 0 || seam[i] > this.H - 1) {
                throw new IllegalArgumentException();
            }
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        if (seam[W - 1] < 0 || seam[W - 1] > this.H - 1) {
            throw new IllegalArgumentException();
        }
        Picture remHSp = new Picture(this.W, this.H - 1);
        for (int col = 0; col < this.W; col++) {
            for (int row = 0; row < seam[col]; row++) {
                remHSp.set(col, row, this.p.get(col, row));
            }
            for (int row = seam[col] + 1; row < this.H; row++) {
                remHSp.set(col, row - 1, this.p.get(col, row));
            }
        }
        this.p = new Picture(remHSp);
        this.H--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != this.H) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (seam[i] < 0 || seam[i] > this.W - 1) {
                throw new IllegalArgumentException();
            }
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        if (seam[H - 1] < 0 || seam[H - 1] > this.W - 1) {
            throw new IllegalArgumentException();
        }
        Picture remVSp = new Picture(this.W - 1, this.H);
        for (int row = 0; row < this.H; row++) {
            for (int col = 0; col < seam[row]; col++) {
                remVSp.set(col, row, this.p.get(col, row));
            }
            for (int col = seam[row] + 1; col < this.W; col++) {
                remVSp.set(col - 1, row, this.p.get(col, row));
            }
        }
        this.p = new Picture(remVSp);
        this.W--;
    }

    // unit testing (optional)
    public static void main(String[] args) {
        // to be tested
    }

}