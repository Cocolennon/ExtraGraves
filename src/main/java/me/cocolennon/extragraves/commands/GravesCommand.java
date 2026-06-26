package me.cocolennon.extragraves.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.cocolennon.extragraves.Main;
import me.cocolennon.extragraves.util.Localization;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GravesCommand {
    public static LiteralCommandNode<CommandSourceStack> register() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("graves")
                .then(Commands.literal("info")
                        .requires(source -> source.getSender().hasPermission("extragraves.info"))
                        .executes(GravesCommand::sendInfo))
                .then(Commands.literal("reload")
                        .requires(source -> source.getSender().hasPermission("extragraves.reload"))
                        .executes(GravesCommand::reloadConfig));
        return root.build();
    }

    private static int sendInfo(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        MiniMessage miniMessage = MiniMessage.miniMessage();
        List<Component> info = new ArrayList<>();
        info.add(miniMessage.deserialize("<#FF55FF><bold>========================="));
        info.add(miniMessage.deserialize("<#AA00AA><bold>Extra Graves <#AA00AA>" + Main.getInstance().getPluginMeta().getVersion()));
        info.add(miniMessage.deserialize("<#AA00AA>You're using the latest version"));
        info.add(miniMessage.deserialize("<#AA00AA>Made with <#FF5555>❤ <#AA00AA>by Cocolennon"));
        info.add(miniMessage.deserialize("<#FF55FF><bold>========================="));
        info.forEach(sender::sendMessage);
        if(sender instanceof Player player) player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        return Command.SINGLE_SUCCESS;
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        Main.getInstance().loadConfig(true);
        sender.sendMessage(Localization.get(sender, "success.reload", true));
        if(sender instanceof Player player) player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        return Command.SINGLE_SUCCESS;
    }
}
