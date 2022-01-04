
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.JDABuilder.createDefault;

public class Main {


    /**
     * The main method.
     * @param args
     *         the arguments
     * @throws LoginException
     *         net.dv8tion.jda.api.JDABuilder login error
     */
    public static void main(String[] args) throws LoginException {

        String token = "OTIxOTAxODE5NTI4MTE0MTg3.Yb5p-Q.ARFDrG0UVKekIG4_hA0KWIXzads";

        JDABuilder builder = createDefault(token);

        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);

        builder.setBulkDeleteSplittingEnabled(false);

        builder.setCompression(Compression.NONE);

        builder.setActivity(Activity.listening("!help"));
        builder.setStatus(OnlineStatus.ONLINE);

        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);

        builder.addEventListeners(new WatcherCommands());
        builder.addEventListeners(new HelpCommand());
        builder.addEventListeners(new PriceCommand());

        builder.build();

    }


}
