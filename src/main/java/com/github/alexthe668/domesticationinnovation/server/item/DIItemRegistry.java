package com.github.alexthe668.domesticationinnovation.server.item;

import com.github.alexthe668.domesticationinnovation.DomesticationMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class DIItemRegistry {

    public static final DeferredRegister<Item> DEF_REG = DeferredRegister.create(ForgeRegistries.ITEMS, DomesticationMod.MODID);

    public static final RegistryObject<Item> COLLAR_TAG = DEF_REG.register("collar_tag", () -> new CollarTagItem());
    public static final RegistryObject<Item> FEATHER_ON_A_STICK = DEF_REG.register("feather_on_a_stick", () -> new FeatherOnAStickItem());
    public static final RegistryObject<Item> DEED_OF_OWNERSHIP = DEF_REG.register("deed_of_ownership", () -> new DeedOfOwnershipItem());

}
