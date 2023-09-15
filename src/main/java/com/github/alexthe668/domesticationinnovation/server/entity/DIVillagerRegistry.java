package com.github.alexthe668.domesticationinnovation.server.entity;

import com.github.alexthe668.domesticationinnovation.DomesticationMod;
import com.github.alexthe668.domesticationinnovation.server.misc.DIPOIRegistry;
import com.github.alexthe668.domesticationinnovation.server.misc.DISoundRegistry;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Predicate;

public class DIVillagerRegistry {

    public static final DeferredRegister<VillagerProfession> DEF_REG = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, DomesticationMod.MODID);
    
    public static final RegistryObject<VillagerProfession> ANIMAL_TAMER = DEF_REG.register("animal_tamer", () -> buildVillagerProfession());

    private static VillagerProfession buildVillagerProfession() {
        Predicate<Holder<PoiType>> heldJobSite = (poiType) -> {
            return poiType.is(DIPOIRegistry.PET_BED.getKey());
        };
        Predicate<Holder<PoiType>> acquirableJobSite = (poiType) -> {
            return poiType.is(DIPOIRegistry.PET_BED.getKey());
        };
        return new VillagerProfession("animal_tamer", heldJobSite, acquirableJobSite, ImmutableSet.of(), ImmutableSet.of(), DISoundRegistry.PET_BED_USE.get());
    }
}
