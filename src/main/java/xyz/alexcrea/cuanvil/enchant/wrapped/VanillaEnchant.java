package xyz.alexcrea.cuanvil.enchant.wrapped;

import io.delilaheve.util.ItemUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.alexcrea.cuanvil.enchant.EnchantmentProperties;
import xyz.alexcrea.cuanvil.enchant.EnchantmentRarity;
import xyz.alexcrea.cuanvil.enchant.WrappedEnchantment;

import java.util.Locale;

public class VanillaEnchant extends WrappedEnchantment {

    private final @NotNull Enchantment enchantment;

    public VanillaEnchant(@NotNull Enchantment enchantment){
        super(enchantment.getKey(),
                getRarity(enchantment),
                enchantment.getMaxLevel());
        this.enchantment = enchantment;

    }

    @Override
    protected boolean isOptimised() {
        return true;
    }

    @Override
    public int getLevel(@NotNull ItemStack item, @NotNull ItemMeta meta) {
        if (ItemUtil.INSTANCE.isEnchantedBook(item)) {
            return ((EnchantmentStorageMeta)meta).getStoredEnchantLevel(this.enchantment);
        } else {
            return meta.getEnchantLevel(this.enchantment);
        }
    }

    @Override
    public boolean isEnchantmentPresent(@NotNull ItemStack item, @NotNull ItemMeta meta) {
        if (ItemUtil.INSTANCE.isEnchantedBook(item)) {
            EnchantmentStorageMeta bookMeta = ((EnchantmentStorageMeta)meta);

            return bookMeta.getStoredEnchants().containsKey(this.enchantment);
        }else{
            return item.containsEnchantment(this.enchantment);
        }
    }

    @Override
    public void addEnchantmentUnsafe(@NotNull ItemStack item, int level) {
        if (ItemUtil.INSTANCE.isEnchantedBook(item)) {
            EnchantmentStorageMeta bookMeta = ((EnchantmentStorageMeta)item.getItemMeta());

            assert bookMeta != null;
            bookMeta.addStoredEnchant(this.enchantment, level, true);
            item.setItemMeta(bookMeta);
        } else {
            item.addUnsafeEnchantment(this.enchantment, level);
        }

    }

    @Override
    public void removeFrom(@NotNull ItemStack item) {
        if (ItemUtil.INSTANCE.isEnchantedBook(item)) {
            EnchantmentStorageMeta bookMeta = ((EnchantmentStorageMeta)item.getItemMeta());

            assert bookMeta != null;
            bookMeta.removeStoredEnchant(this.enchantment);
            item.setItemMeta(bookMeta);
        }else{
            item.removeEnchantment(this.enchantment);
        }

    }

    public static EnchantmentRarity getRarity(Enchantment enchantment){
        try {return EnchantmentProperties.valueOf(enchantment.getKey().getKey().toUpperCase(Locale.ENGLISH)).getRarity();}
        catch (IllegalArgumentException ignored) {}

        return EnchantmentRarity.COMMON;
    }

}
