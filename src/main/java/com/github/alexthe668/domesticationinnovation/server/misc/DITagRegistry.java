package com.github.alexthe668.domesticationinnovation.server.misc;

import com.github.alexthe668.domesticationinnovation.DomesticationMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class DITagRegistry {
    public static final TagKey<EntityType<?>> REFUSES_COLLAR_TAGS = registerEntity("refuses_collar_tags");
    public static final TagKey<Item> TAME_FROGS_WITH = registerItem("tame_frogs_with");

    private static TagKey<EntityType<?>> registerEntity(String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(DomesticationMod.MODID, name));
    }

    private static TagKey<Item> registerItem(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(DomesticationMod.MODID, name));
    }
}