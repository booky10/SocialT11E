package tk.t11e.social.util;
// Created by booky10 in SocialT11E (19:31 30.01.20)

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabCompleter {

    public static List<String> convert(String[] args, List<String> completions) {
        List<String> list = new ArrayList<>();

        if (args.length < 1)
            return Collections.emptyList();
        String word = args[args.length - 1];
        if (word.equalsIgnoreCase(""))
            return completions;

        for (String entry : completions)
            if (entry.startsWith(word)&& !entry.equals(word))
                list.add(entry);

        return list;
    }
}