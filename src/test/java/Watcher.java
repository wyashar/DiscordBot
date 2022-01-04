import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.TimerTask;

/**
 * Watcher class to represent the monitoring of a cryptocurrency price.
 *
 * @author
 *      Yashar Ben Bakhshaeisarand
 * @version
 *      1/3/2022
 *
 */
public class Watcher extends TimerTask {

    /**
     * The coin price.
     */
    private String coinPrice;
    /**
     * The coin name.
     */
    private String coinName;
    /**
     * The user defined "wanted" coin price.
     */
    private String reqCoinPrice;
    /**
     * The user discord ID in mention form.
     */
    private String author;
    /**
     * The boolean representation of {@code coinPrice == @code reqCoinPrice}.
     */
    private boolean priceHit;
    /**
     * The discord text channel where watcher object was created.
     */
    private TextChannel channel;
    /**
     * The counter.
     */
    private int count;

    /**
     * Represents greater than or less than operator, true for less than, false for greater.
     */
    private boolean operator;


    /**
     * Default constructor.
     * @param coinName
     *          the name of the cryptocurrency
     * @param reqCoinPrice
     *          the user defined "wanted" price
     * @param author
     *          the author of the watcher
     * @param operator
     *          the boolean representation of greater than OR less than operator
     * @param channel
     *          the text channel of the watcher
     */
    public Watcher(String coinName, String reqCoinPrice, String author, boolean operator, TextChannel channel){
        this.coinName = coinName;
        this.coinPrice = Scraper.getCoinPrice(coinName);
        this.reqCoinPrice = reqCoinPrice;
        this.author = author;
        this.count = 1;
        this.priceHit = false;
        this.operator = operator;
        this.channel = channel;
    }

    /**
     * Method to grab this.author
     * @return
     *      this.author
     */
    public String getAuthor(){return this.author;}

    /**
     * Method to grab this.coinPrice
     * @return
     *      this.coinPrice
     */
    public String getCoinPrice(){return this.coinPrice;}

    /**
     * Method to grab this.coinName
     * @return
     *       this.coinName
     */
    public String getCoinName(){return this.coinName;}

    /**
     * Method to grab this.priceHit
     * @return
     *      this.priceHit
     */
    public boolean getPriceHit(){return this.priceHit;}

    /**
     * Helper method to update this.priceHit
     * dependant on operator, if this.reqCoinPrice is met.
     */
    private void checkPriceHit(){
        if(!this.operator && Double.parseDouble(this.coinPrice) >= Double.parseDouble(this.reqCoinPrice)){
            this.priceHit = true;
        }else if(this.operator && Double.parseDouble(this.coinPrice) <= Double.parseDouble(this.reqCoinPrice)){
            this.priceHit = true;
        }
    }

    /**
     * Helper method to notify this.author once this.priceHit.
     */
    private void notifyAuthor(){

        this.channel.sendMessage("**PRICEHIT**").queue();
        this.channel.sendMessage(this.author).queue();
        this.channel.sendMessage(this.author).queue();
        this.channel.sendMessage("****PRICEHIT****").queue();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0xd1f542);

        embed.setTitle(this.coinName + " watcher stopped");
        embed.setDescription("`"+ this.coinName + " price of $" + this.reqCoinPrice + " met, current price $" + this.coinPrice + "`");

        this.channel.sendMessage(embed.build()).queue();

    }

    @Override
    public void run(){
        this.coinPrice = Scraper.getCoinPrice(this.coinName);
        checkPriceHit();
        if(this.priceHit){notifyAuthor();}
        System.out.println("{r#}" + this.count + " " + this.coinName + " {price}: " + this.coinPrice + " {user}: " + this.author + " " + this.priceHit);
        this.count += 1;
    }


}
