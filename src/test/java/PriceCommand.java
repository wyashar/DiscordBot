import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Locale;

public class PriceCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){

        String url = "https://coinmarketcap.com/currencies/";

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0xd1f542);

        String[] msg = e.getMessage().getContentRaw().split("\\s+");

        if(msg[0].equalsIgnoreCase("!price")){

            /**
             * <pre>
             * Magic number 1 for command syntax
             *  all price commands are |msg| = 2
             *   </pre>
             */
            if(1 >= msg.length){

                embed.setTitle("invalid syntax");
                embed.addField("command examples", "`!help`", false);
                embed.addField("coinmarketcap", "https://coinmarketcap.com/", false);
                e.getChannel().sendTyping().queue();
                e.getChannel().sendMessage(embed.build()).queue();

            }else{

                String price = Scraper.getCoinPrice(msg[1]);
                url += msg[1] + "/";

                if(price.equals("DNE")){
                    embed.setTitle("invalid crypto name");
                    embed.setDescription("`crypto with name " + msg[1] + " could not be found`");
                    embed.addField("command examples", "`!help`", false);
                    embed.addField("valid cryptos", "https://coinmarketcap.com/", false);
                    e.getChannel().sendTyping().queue();
                    e.getChannel().sendMessage(embed.build()).queue();

                }else{
                    embed.setTitle(msg[1] + " price");
                    embed.setDescription("`$" + price + "`");
                    embed.addField("buy " + msg[1], url, false);
                    e.getChannel().sendMessage(embed.build()).queue();
                }

            }


        }

    }

}
