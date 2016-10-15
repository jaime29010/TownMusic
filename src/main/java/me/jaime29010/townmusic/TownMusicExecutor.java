package me.jaime29010.townmusic;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import me.jaimemartz.faucet.Messager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownMusicExecutor implements CommandExecutor {
    private final Main main;
    public TownMusicExecutor(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            final Messager msgr = new Messager(sender);
            final Player player = (Player) sender;
            if (args.length == 0) {
                msgr.send(
                        "&6.oOo.___________.[ &eGeneral TownMusic Help &6].___________.oOo.",
                        "  &3/townmusic &bset [link] &7: Set the song of your own town (&cOnly mayors&7)",
                        "  &3/townmusic &bclear &7: Clear the song of your own town (&cOnly mayors&7)",
                        "&6.oOo.____________.[ &eAdmin TownMusic Help &6].____________.oOo.",
                        "  &3/townmusic &bset [town] [link] &7: Set the song of a town (&cOnly admins&7)",
                        "  &3/townmusic &bclear [town] &7: Clear the song of a town (&cOnly admins&7)",
                        "  &3/townmusic &breload &7: Reload the plugin (&cOnly admins&7)"
                );
            } else {
                switch (args[0].toLowerCase()) {
                    case "set": {
                        if (args.length == 2) {
                            Resident resident = TownyUtils.getResident(player);
                            Town town = TownyUtils.getTown(resident);
                            if (town != null && town.getMayor().equals(resident)) {
                                String link = args[1];
                                if (main.isValidLink(link)) {
                                    main.setSong(town, link);
                                    msgr.send(String.format("&aYou have set the song of your town %s to %s", town.getName(), link));
                                } else {
                                    msgr.send("&cThe link you provided is not a valid song", "&cMake sure it ends with .mp3");
                                }
                            } else {
                                msgr.send("&cThis command can only be executed by a mayor");
                            }
                            break;
                        } else if (args.length == 3) {
                            if (main.hasPermission(player, "townmusic.admin")) {
                                String name = args[1];
                                Town town = TownyUtils.getTown(name);
                                if (town != null) {
                                    String link = args[2];
                                    if (main.isValidLink(link)) {
                                        main.setSong(town, link);
                                        msgr.send(String.format("&aYou have set the song of the town %s to %s", town.getName(), link));
                                    } else {
                                        msgr.send("&cThe link you provided is not a valid song", "&cMake sure it ends with .mp3");
                                    }
                                } else {
                                    msgr.send(String.format("&cThere is no town called %s", name));
                                }
                            } else {
                                msgr.send("&cThis command can only be executed by a admin");
                            }
                            break;
                        }
                    }
                    case "clear": {
                        if (args.length == 1) {
                            Resident resident = TownyUtils.getResident(player);
                            Town town = TownyUtils.getTown(resident);
                            if (town != null) {
                                if (town.getMayor().equals(resident)) {
                                    if (main.hasSong(town)) {
                                        main.removeSong(town);
                                        msgr.send("&aYou have removed the song of your town");
                                    } else {
                                        msgr.send("&Your town does not have a song");
                                    }
                                } else {
                                    msgr.send("&cThis command can only be executed by a mayor");
                                }
                            } else {
                                msgr.send("&cYou are not in a town");
                            }
                            break;
                        } else if (args.length == 2) {
                            if (main.hasPermission(player, "townmusic.admin")) {
                                String name = args[1];
                                Town town = TownyUtils.getTown(name);
                                if (town != null) {
                                    if (main.hasSong(town)) {
                                        main.removeSong(town);
                                        msgr.send("&aYou have removed the song of your town");
                                    } else {
                                        msgr.send("&cThat town does not have a song");
                                    }
                                } else {
                                    msgr.send(String.format("&cThere is no town called %s", name));
                                }
                            } else {
                                msgr.send("&cThis command can only be executed by a admin");
                            }
                            break;
                        }
                    }
                    case "reload": {
                        if (main.hasPermission(player, "townmusic.admin")) {
                            main.reloadPlugin();
                            msgr.send("&aYou have successfully reloaded the plugin");
                        } else {
                            msgr.send("&cThis command can only be executed by a admin");
                        }
                        break;
                    }
                    default: {
                        msgr.send("&cYou have provided unvalid arguments for this command, for help type: /townmusic");
                    }
                }
            }
        } else {
            sender.sendMessage("This command can only be executed by a player");
        }
        return true;
    }
}