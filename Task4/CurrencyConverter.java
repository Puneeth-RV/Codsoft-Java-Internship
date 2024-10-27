import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    public static double getExchangeRate(String baseCurrency, String targetCurrency) {
        String apiKey = "---CONFIDENTIAL---";  
        String urlStr = "https://openexchangerates.org/api/latest.json?app_id=" + apiKey + "&base=" + baseCurrency;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String responseStr = response.toString();
            String targetPattern = "\"" + targetCurrency + "\":";
            int start = responseStr.indexOf(targetPattern);

            if (start == -1) {
                System.out.println("Target currency not found.");
                return -1;
            }

            start += targetPattern.length();
            int end = responseStr.indexOf(",", start);

            String exchangeRateStr = (end == -1) ? responseStr.substring(start) : responseStr.substring(start, end);
            return Double.parseDouble(exchangeRateStr);

        } catch (Exception e) {
            System.out.println("Error fetching exchange rate: " + e.getMessage());
            return -1;
        }
    }

    public static double convertCurrency(double amount, double exchangeRate) {
        return amount * exchangeRate;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Currency Converter!");
        
        System.out.print("Enter base currency (e.g., USD): ");
        String baseCurrency = scanner.next().toUpperCase();

        System.out.print("Enter target currency (e.g., EUR): ");
        String targetCurrency = scanner.next().toUpperCase();

        System.out.print("Enter amount to convert: ");
        double amount = scanner.nextDouble();

        double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);

        if (exchangeRate != -1) {
            double convertedAmount = convertCurrency(amount, exchangeRate);
            System.out.printf("Converted Amount: %.2f %s\n", convertedAmount, targetCurrency);
        } else {
            System.out.println("Currency conversion failed. Please try again.");
        }

        scanner.close();
    }
}
