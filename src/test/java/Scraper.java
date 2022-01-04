import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Helper class to scrape coin prices from a URL.
 *
 * @author
 *      Yashar Ben Bakhshaeisarand
 * @version
 *      1/3/2022
 */
public class Scraper {

    /**
     * Grabs cryptocurrency prices from url, dependant on @param coinName,
     *  returns "DNE" is url is invalid
     *
     * @param coinName
     *          the name of the cryptocurrency
     * @return
     *          returns the price of the cryptocurrency, removing any {$} or {,} chars, or "DNE" if URL is invalid
     * @ensures
     *          <pre>
     *              iff url is valid
     *                  getCoinPrice(coinName) = coinPrice as String excluding {$} or {,} chars
     *              else
     *                  getCoinPrice(coinName) = "DNE"
     *          </pre>
     */
    public static String getCoinPrice(String coinName){

        String url = "https://coinmarketcap.com/currencies/";

        url += coinName + "/";

        try{
            Document document = Jsoup.connect(url).get();

            Elements elements = document.select("div.priceValue");
            Elements span = elements.select("span");

            String coinPrice = span.text();
            coinPrice = coinPrice.substring(1);
            coinPrice = coinPrice.replace(",", "");

            return coinPrice;
        }catch(IOException e){
            System.err.println("Coin price could not be grabbed, ERROR: " + e);

            return "DNE";
        }

    }

}
