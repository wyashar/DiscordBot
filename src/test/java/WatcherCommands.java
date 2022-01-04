import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class WatcherCommands extends ListenerAdapter {

    private ArrayList<Watcher> watchers = new ArrayList<>();
    private ArrayList<Timer> timers = new ArrayList<>();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0xd1f542);

        String url = "https://coinmarketcap.com/currencies/";
        String author = e.getAuthor().getAsMention();

        TextChannel channel = e.getJDA().getTextChannelById(e.getChannel().getId());

        String[] msg = e.getMessage().getContentRaw().split("\\s+");

        if(msg[0].equalsIgnoreCase("!watch")){

            /**
             * <pre>
             * Magic number 3 for command syntax
             *  all watch commands are |msg| = 4
             *   </pre>
             */
            if(3 >= msg.length){

                embed.setTitle("invalid syntax");
                embed.addField("command examples", "`!help`", false);
                embed.addField("coinmarketcap", "https://coinmarketcap.com/", false);
                e.getChannel().sendTyping().queue();
                e.getChannel().sendMessage(embed.build()).queue();

            }else{

                String price = Scraper.getCoinPrice(msg[1]);

                if(price.equals("DNE")){

                    embed.setTitle("invalid crypto name");
                    embed.setDescription("`crypto with name " + msg[1] + " could not be found`");
                    embed.addField("command examples", "`!help`", false);
                    embed.addField("valid cryptos", "https://coinmarketcap.com/", false);
                    e.getChannel().sendTyping().queue();
                    e.getChannel().sendMessage(embed.build()).queue();

                }else{

                    if(isDouble(msg[2])){

                        if(msg[3].equals("<") || msg[3].equals(">")){

                            embed.setTitle(msg[1] + " watcher created");
                            e.getChannel().sendMessage(embed.build()).queue();

                            boolean operator = !msg[3].equals(">");

                            Watcher watcher = new Watcher(msg[1], msg[2], author, operator, channel);

                            TimerTask task = watcher;
                            Timer timer = new Timer();
                            /**
                             * Zero second delay, with a four second run frequency
                             */
                            timer.schedule(task, 0, 4000);

                            url += msg[1] + "/";

                            if(!operator && Double.parseDouble(price) >= Double.parseDouble(msg[2])){

                                timer.cancel();
                                embed.setTitle(msg[1] + " watcher stopped");
                                embed.addField("buy " + msg[1], url, false);
                                embed.setDescription("`price is greater than or equal to $" + msg[2] + ", current price $" + price +"`");
                                e.getChannel().sendMessage(embed.build()).queue();
                            }else if(operator && Double.parseDouble(price) <= Double.parseDouble(msg[2])){

                                timer.cancel();
                                embed.setTitle(msg[1] + " watcher stopped");
                                embed.addField("buy " + msg[1], url, false);
                                embed.setDescription("`price is less than or equal to $" + msg[2] + ", current price $" + price +"`");
                                e.getChannel().sendMessage(embed.build()).queue();
                            }else{
                                watchers.add(watcher);
                                timers.add(timer);
                            }



                        }else{

                            embed.setTitle("invalid operator");
                            embed.setDescription("`operator must be > or <`");
                            embed.addField("command examples", "`!help`", false);
                            e.getChannel().sendTyping().queue();
                            e.getChannel().sendMessage(embed.build()).queue();

                        }


                    }else{

                        embed.setTitle("invalid price");
                        embed.setDescription("`prices cannot contain non-integer characters, except for a decimal point`");
                        embed.addField("command examples", "`!help`", false);
                        e.getChannel().sendTyping().queue();
                        e.getChannel().sendMessage(embed.build()).queue();

                    }

                }

            }


        }
        if(msg[0].equalsIgnoreCase("!stop")){

            /**
             * <pre>
             * Magic number 1 for command syntax
             *  all stop commands are |msg| = 2
             *   </pre>
             */
            if(1 >= msg.length){

                embed.setTitle("invalid syntax");
                embed.setDescription("`!stop must be followed by an already existing watcher`");
                embed.addField("command examples", "`!help`", false);
                e.getChannel().sendMessage(embed.build()).queue();

            }else{

                for(int i = 0; i < watchers.size(); i++){

                    Watcher tWatcher = watchers.get(i);

                    if(tWatcher.getAuthor().equals(author) && tWatcher.getCoinName().equals(msg[1])){

                        timers.get(i).cancel();
                        System.out.println(tWatcher.getCoinName() + " {watcher stopped}: user requested");

                        embed.setTitle(tWatcher.getCoinName() + " watcher stopped");
                        embed.setDescription("`"+ tWatcher.getCoinName() + " watcher stopped at $" + tWatcher.getCoinPrice() + "`");

                        e.getChannel().sendMessage(embed.build()).queue();

                        watchers.remove(i);
                        timers.remove(i);
                        return;
                    }
                }
                embed.setTitle("watcher does not exist");
                embed.setDescription("`watcher with coin type " + msg[1] + " by author `" + author + "` does not exist`");
                embed.addField("command examples", "`!help`", false);
                e.getChannel().sendMessage(embed.build()).queue();

            }

        }if(msg[0].equals("**PRICEHIT**") && e.getAuthor().isBot()){
            for(int i = 0; i < watchers.size(); i++){

                Watcher tWatcher = watchers.get(i);

                if(tWatcher.getPriceHit()){
                    timers.get(i).cancel();

                    System.out.println(tWatcher.getCoinName() + " {watcher stopped}: pricehit");

                    watchers.remove(i);
                    timers.remove(i);
                    return;
                }

            }
        }

    }

    private boolean isDouble(String str){
        try{
            Double.parseDouble(str);
        }catch(NumberFormatException e){
            return false;
        }catch(Exception e){
            return false;
        }
        return true;
    }
}
