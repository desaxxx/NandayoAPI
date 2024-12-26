package org.nandayo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class GeneralMethods {

    public static int getRandomInteger(Integer size) {
        int r = (int) Math.ceil(Math.random() * size);
        return r == 0 ? 0 : r-1;
    }

    public static <X> X getMaxKey(HashMap<X, Integer> hashMap) {
        X key = null;
        int a = Integer.MIN_VALUE;
        for(Map.Entry<X, Integer> entry : hashMap.entrySet()) {
            if(entry.getValue() > a) {
                a = entry.getValue();
                key = entry.getKey();
            }
        }
        return key;
    }

    public static List<String> fixedTabComp(String arg, String options) {
        return Arrays.asList(options);
    }
    public static List<String> fixedTabComp(String arg, String... options) {
        return Arrays.stream(options)
                .filter(x -> x.startsWith(arg))
                .collect(Collectors.toList());
    }

    public static boolean hasEnoughItem(Player p, ItemStack item) {
        return p.getInventory().containsAtLeast(item, item.getAmount());
    }
    public static void removeItems(Player p, ItemStack item) {
        int toRemove = item.getAmount();
        for(ItemStack i : p.getInventory().getContents()) {
            if(i != null && i.isSimilar(item)) {
                int stackAmount = i.getAmount();

                if (stackAmount > toRemove) {
                    i.setAmount(stackAmount - toRemove);
                    break;
                }else {
                    toRemove -= stackAmount;
                    i.setAmount(0);
                }
                if (toRemove <= 0) {
                    break;
                }
            }
        }
    }

    public static String romanNumber(long num) {
        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        return thousands[(int) (num / 1000)] +
                hundreds[(int) ((num % 1000) / 100)] +
                tens[(int) ((num % 100) / 10)] +
                ones[(int) (num % 10)];
    }
    public static List<Location> getNeighbouringLocations(Location location) {
        int[][] offsets = {
                {1, 0, 0}, {-1, 0, 0}, // East, West
                {0, 1, 0}, {0, -1, 0}, // Up, Down
                {0, 0, 1}, {0, 0, -1}  // South, North
        };
        List<Location> list = new ArrayList<>();
        for (int[] offset : offsets) {
            Location checkLocation = location.clone().add(offset[0], offset[1], offset[2]);
            list.add(checkLocation.getBlock().getLocation());
        }
        return list;
    }

    public static void sendToAll(String message) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(message);
        }
    }
    public static void sendToAll(String... messages) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(messages);
        }
    }
    public static int parseInteger(Object x, int def) {
        try {
            return Integer.parseInt(x.toString());
        }catch (NumberFormatException e) {
            return def;
        }
    }
    public static double parseDouble(Object x, double def) {
        try {
            return Double.parseDouble(x.toString());
        }catch (NumberFormatException e) {
            return def;
        }
    }
    public static String formattedLocation(Location location) {
        if(location == null) {
            return "?";
        }
        return String.join(",", location.getWorld().getName(), String.format("%.2f", location.getX()),
                String.format("%.2f", location.getY()), String.format("%.2f", location.getZ()),
                String.format("%.2f", location.getYaw()), String.format("%.2f", location.getPitch()));
    }
    public static Location parseLocation(String str) {
        //World,X,Y,Z,YAW,PITCH format
        if(str != null && str.contains(",")) {
            String[] locations = str.split(",");
            if(locations.length >= 4) {
                World world = Bukkit.getWorld(locations[0]);
                double x = parseDouble(locations[1], 0);
                double y = parseDouble(locations[2], 0);
                double z = parseDouble(locations[3], 0);
                if(locations.length == 4) {
                    return new Location(world,x,y,z);
                }else if(locations.length == 6) {
                    float yaw = (float) parseDouble(locations[4], 0);
                    float pitch = (float) parseDouble(locations[5], 0);
                    return new Location(world,x,y,z,yaw,pitch);
                }
            }
        }
        return null;
    }
    public static ItemStack getItemStack(String material, Material def) {
        /*
        if(material.startsWith("ia-")) {
            return YWars.getIAItem(material.replaceFirst("ia-", ""));
        }
        */
        Material material1 = Material.getMaterial(material);
        return material1 != null ? new ItemStack(material1) : new ItemStack(def);
    }
    public static Date parseDate(String str) {
        if(str == null || str.isEmpty()) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(str);
        } catch (ParseException e) {
            return null;
        }
    }
    public static String dateToString(Date date) {
        if(date == null) {
            return "NONE";
        }
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
    }
    public static String formattedDate(String str) {
        if(str == null || str.isEmpty()) {
            return null;
        }
        Date date = parseDate(str);
        return date != null ? new SimpleDateFormat("MM/dd/yy HH:mm").format(date) : "NONE";
    }
    public static String formattedDate(Date date) {
        if(date == null) {
            return "NONE";
        }
        return new SimpleDateFormat("MM/dd/yy HH:mm").format(date);
    }
    public static List<Player> getPlayerList(List<String> list) {
        return list.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    public static Location getTargetBlockLocation(Player player) {
        Set<Material> transparentMaterials = new HashSet<>();
        transparentMaterials.add(Material.AIR);
        Block targetBlock = player.getTargetBlock(transparentMaterials, 100);
        if(targetBlock.getType() != Material.AIR) {
            return targetBlock.getLocation();
        }
        return null;
    }

    public static long timeSinceInMS(Date date, long def) {
        //1 sec = 1000 millisecond;
        return date == null ? def : System.currentTimeMillis() - date.getTime();
    }
    public static String formattedTime(long millisecond) {
        long day = 24 * 60 * 60 * 1000;
        long hour = 60 * 60 * 1000;
        long minute = 60 * 1000;
        long second = 1000;

        long days = millisecond / day;
        millisecond %= day;

        long hours = millisecond / hour;
        millisecond %= hour;

        long minutes = millisecond / minute;
        millisecond %= minute;

        long seconds = millisecond / second;

        if(days == 0 && hours == 0 && minutes == 0) {
            return String.format("%d s", seconds);
        }else if(days == 0 && hours == 0) {
            return String.format("%d m, %d s", minutes, seconds);
        }else if(days == 0) {
            return String.format("%d h, %d m", hours, minutes);
        }
        return String.format("%d d, %d h", days, hours);
    }
}
