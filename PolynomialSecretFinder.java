import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.*;

public class PolynomialSecretFinder {

    static class Point {
        BigInteger x, y;
        Point(BigInteger x, BigInteger y) {
            this.x = x; this.y = y;
        }
    }

    // Decode string number from a given base to BigInteger
    public static BigInteger decodeValue(String value, int base) {
        // bases can be upto 16 given the samples
        return new BigInteger(value, base);
    }

    // Simple JSON parsing assuming exactly provided format, no external libs
    public static Map<String, Object> parseJson(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
        }
        String json = sb.toString();

        // Extract n, k
        Pattern keysPattern = Pattern.compile("\"keys\"\\s*:\\s*\\{[^}]*\"n\"\\s*:\\s*(\\d+),[^}]*\"k\"\\s*:\\s*(\\d+)[^}]*}");
        Matcher mk = keysPattern.matcher(json);
        if (!mk.find()) {
            throw new RuntimeException("Unable to find keys n,k in JSON");
        }
        int n = Integer.parseInt(mk.group(1));
        int k = Integer.parseInt(mk.group(2));

        // Extract entries with their keys (integers) and base,value pairs
        Pattern entryPattern = Pattern.compile("\"(\\d+)\"\\s*:\\s*\\{\\s*\"base\"\\s*:\\s*\"?(\\d+)\"?\\s*,\\s*\"value\"\\s*:\\s*\"([^\"]+)\"\\s*}");
        Matcher matcher = entryPattern.matcher(json);

        Map<Integer, Map<String, String>> entries = new HashMap<>();
        while (matcher.find()) {
            int key = Integer.parseInt(matcher.group(1));
            String base = matcher.group(2);
            String val = matcher.group(3);
            Map<String, String> map = new HashMap<>();
            map.put("base", base);
            map.put("value", val);
            entries.put(key, map);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("n", n);
        res.put("k", k);
        res.put("entries", entries);
        return res;
    }

    // Lagrange interpolation to find the constant term f(0)
    // f(0) = sum_{j} y_j * product_{m != j} (-x_m) / (x_j - x_m)
    static BigInteger lagrangeInterpolationAtZero(List<Point> points) {
        BigInteger secret = BigInteger.ZERO;
        int k = points.size();
        for (int j = 0; j < k; j++) {
            BigInteger yj = points.get(j).y;
            BigInteger numerator = yj;            // starts with y_j
            BigInteger denominator = BigInteger.ONE;
            for (int m = 0; m < k; m++) {
                if (m == j) continue;
                BigInteger xm = points.get(m).x;
                BigInteger xj = points.get(j).x;
                numerator = numerator.multiply(xm.negate());
                denominator = denominator.multiply(xj.subtract(xm));
            }
            secret = secret.add(numerator.divide(denominator));
        }
        return secret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java PolynomialSecretFinder input.json");
            return;
        }
        String filePath = args[0];
        Map<String, Object> parsed = parseJson(filePath);
        int n = (Integer) parsed.get("n");
        int k = (Integer) parsed.get("k");
        @SuppressWarnings("unchecked")
        Map<Integer, Map<String, String>> entries = (Map<Integer, Map<String, String>>) parsed.get("entries");

        // Sort keys to get points in order
        List<Integer> keysList = new ArrayList<>(entries.keySet());
        Collections.sort(keysList);

        // Prepare points
        List<Point> points = new ArrayList<>();
        for (int key : keysList) {
            Map<String, String> d = entries.get(key);
            int base = Integer.parseInt(d.get("base"));
            String valStr = d.get("value");
            BigInteger yValue = decodeValue(valStr, base);
            points.add(new Point(BigInteger.valueOf(key), yValue));
        }

        // Pick any k points (here first k) - since problem states n>=k
        List<Point> chosenPoints = points.subList(0, k);

        // Compute the secret
        BigInteger secret = lagrangeInterpolationAtZero(chosenPoints);

        System.out.println("Secret (constant term c): " + secret.toString());
    }
}
