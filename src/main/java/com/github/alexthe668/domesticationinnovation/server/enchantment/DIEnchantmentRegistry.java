package com.github.alexthe668.domesticationinnovation.server.enchantment;

import com.github.alexthe668.domesticationinnovation.DomesticationMod;
import com.github.alexthe668.domesticationinnovation.server.item.DIItemRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;

public class DIEnchantmentRegistry {
    public static final EnchantmentCategory CATEGORY = EnchantmentCategory.create("pet", (item -> item == DIItemRegistry.COLLAR_TAG.get()));
    public static final DeferredRegister<Enchantment> DEF_REG = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, DomesticationMod.MODID);

    public static final PetEnchantment HEALTH_BOOST = new PetEnchantment("health_boost", Enchantment.Rarity.COMMON, 3, 5);
    public static final PetEnchantment FIREPROOF = new PetEnchantment("fireproof", Enchantment.Rarity.UNCOMMON, 1, 7);
    public static final PetEnchantment POISON_RESISTANCE = new PetEnchantment("poison_resistance", Enchantment.Rarity.COMMON, 1, 4);
    public static final PetEnchantment SPEEDSTER = new PetEnchantment("speedster", Enchantment.Rarity.RARE, 3, 6);
    public static final PetEnchantment FROST_FANG = new PetEnchantment("frost_fang", Enchantment.Rarity.RARE, 1, 7);
    public static final PetEnchantment LINKED_INVENTORY = new PetEnchantment("linked_inventory", Enchantment.Rarity.COMMON, 1, 5);
    public static final PetEnchantment HEALTH_SIPHON = new PetEnchantment("health_siphon", Enchantment.Rarity.RARE, 1, 11);
    public static final PetEnchantment AMPHIBIOUS = new PetEnchantment("amphibious", Enchantment.Rarity.UNCOMMON, 1, 6);
    public static final PetEnchantment VAMPIRE = new PetEnchantmentLootOnly("vampire", Enchantment.Rarity.VERY_RARE, 2, 10);
    public static final PetEnchantment CHARISMA = new PetEnchantmentTradeOnly("charisma", Enchantment.Rarity.VERY_RARE, 3, 7);
    public static final PetEnchantment ORE_SCENTING = new PetEnchantmentLootOnly("ore_scenting", Enchantment.Rarity.VERY_RARE, 3, 9);
    public static final PetEnchantment GLUTTONOUS = new PetEnchantment("gluttonous", Enchantment.Rarity.COMMON, 1, 4);
    public static final PetEnchantment INTIMIDATION = new PetEnchantment("intimidation", Enchantment.Rarity.UNCOMMON, 2, 8);
    public static final PetEnchantment MUFFLED = new PetEnchantmentLootOnly("muffled", Enchantment.Rarity.VERY_RARE, 1, 5);
    public static final PetEnchantment BLAZING_PROTECTION = new PetEnchantmentLootOnly("blazing_protection", Enchantment.Rarity.VERY_RARE, 3, 8);
    public static final PetEnchantment HEALING_AURA = new PetEnchantment("healing_aura", Enchantment.Rarity.RARE, 2, 12);

    public static final PetEnchantment REJUVENATION = new PetEnchantment("rejuvenation", Enchantment.Rarity.UNCOMMON, 1, 6);
    public static final PetEnchantment INFAMY_CURSE = new PetEnchantmentCurse("infamy_curse", Enchantment.Rarity.VERY_RARE);
    public static final PetEnchantment IMMATURITY_CURSE = new PetEnchantmentCurse("immaturity_curse", Enchantment.Rarity.VERY_RARE);

    public static void registerEnchantments(IEventBus bus) {
        try {
            for (Field f : DIEnchantmentRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof PetEnchantment petEnchantment) {
                    DEF_REG.register(petEnchantment.getName(), () -> petEnchantment);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        DEF_REG.register(bus);
    }

    public static boolean areCompatible(PetEnchantment e1, Enchantment e2) {
        if(e1 == HEALTH_BOOST) {
            return e2 != HEALTH_SIPHON;
        }
        if(e1 == FIREPROOF){
            return e2 != POISON_RESISTANCE && e2 != FROST_FANG && e2 != AMPHIBIOUS;
        }
        if(e1 == POISON_RESISTANCE){
            return e2 != FIREPROOF;
        }
        if(e1 == FROST_FANG){
            return e2 != FIREPROOF && e2 != BLAZING_PROTECTION;
        }
        if(e1 == HEALTH_SIPHON){
            return e2 != HEALTH_BOOST && e2 != VAMPIRE && e2 != GLUTTONOUS  && e2 != HEALING_AURA;
        }
        if(e1 == AMPHIBIOUS){
            return e2 != FIREPROOF && e2 != BLAZING_PROTECTION;
        }
        if(e1 == VAMPIRE){
            return e2 != HEALTH_SIPHON && e2 != GLUTTONOUS  && e2 != HEALING_AURA;
        }
        if(e1 == ORE_SCENTING){
            return true;
        }
        if(e1 == GLUTTONOUS){
            return e2 != VAMPIRE && e2 != HEALTH_SIPHON;
        }
        if(e1 == INTIMIDATION){
            return true;
        }
        if(e1 == MUFFLED){
            return true;
        }
        if(e1 == BLAZING_PROTECTION){
            return e2 != AMPHIBIOUS && e2 != FROST_FANG;
        }
        if(e1 == HEALING_AURA){
            return e2 != REJUVENATION && e2 != VAMPIRE && e2 != HEALTH_SIPHON;
        }
        if(e1 == REJUVENATION){
            return e2 != HEALING_AURA && e2 != HEALTH_SIPHON;
        }
        return true;
    }
}
