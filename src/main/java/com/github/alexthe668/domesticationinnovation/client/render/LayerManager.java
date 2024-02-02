package com.github.alexthe668.domesticationinnovation.client.render;

import com.github.alexthe668.domesticationinnovation.DomesticationMod;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LayerManager {

    public static boolean canApply(EntityType<?> type) {
        return true; //mojang provides no way to check if an entity is a child class from a arbitrary superclass from it's entitytype
    }

    public static void addLayerIfApplicable(EntityType<? extends LivingEntity> entityType, EntityRenderersEvent.AddLayers event) {
        LivingEntityRenderer renderer = null;
        if(entityType != EntityType.ENDER_DRAGON){
            try{
                renderer = event.getRenderer(entityType);
            }catch (Exception e){
                
            }
            if(renderer != null ){
                renderer.addLayer(new LayerPetOverlays(renderer));
            }
        }
    }
}
