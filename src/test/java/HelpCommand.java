import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0xd1f542);

        String[] msg = e.getMessage().getContentRaw().split("\\s+");

        if(msg[0].equalsIgnoreCase("!help")){

            embed.setTitle("command list");
            embed.addField("price commands", "`!price`", false);
            embed.addField("!price example", "`!price bitcoin`", false);
            embed.addField("watcher commands", "`!watch`, `!stop`", false);
            embed.addField("!watch example", "`!watch bitcoin 50000 > OR !watch bitcoin 50000 <`", false);
            embed.addField("!stop example", "`!stop bitcoin`", false);
            embed.setFooter("Note that price fields cannot use {$} or {,} and that watchers are author sensitive.");
            e.getChannel().sendMessage(embed.build()).queue();

        }


    }
}
