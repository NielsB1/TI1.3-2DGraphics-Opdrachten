import java.util.Random;

public class NoiseMapGenerator {
    private final int[] permutation;

    public NoiseMapGenerator(long seed) {
        permutation = new int[256 * 2];
        Random random = new Random(seed);

        for (int i = 0; i < 256; i++) {
            permutation[i] = i;
        }

        for (int i = 255; i >= 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
        }

        for (int i = 0; i < 256; i++) {
            permutation[i + 256] = permutation[i];
        }
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x) {
        int h = hash & 1;
        double gx = h == 0 ? x : -x;
        return gx;
    }

    public double noise(double x) {
        int xi = (int) Math.floor(x);
        double xf = x - xi;

        int a = permutation[xi % 256];
        int b = permutation[(xi + 1) % 256];

        double u = fade(xf);
        double d = grad(a, xf);
        double e = grad(b, xf - 1);

        return lerp(d, e, u);
    }
}
