package com.github.alexthe668.domesticationinnovation.client.render;

import com.github.alexthe666.citadel.client.render.LightningBoltData;
import com.github.alexthe666.citadel.client.render.LightningRender;
import com.github.alexthe668.domesticationinnovation.DomesticationMod;
import com.github.alexthe668.domesticationinnovation.client.ClientProxy;
import com.github.alexthe668.domesticationinnovation.client.model.BlazingBarModel;
import com.github.alexthe668.domesticationinnovation.server.enchantment.DIEnchantmentRegistry;
import com.github.alexthe668.domesticationinnovation.server.entity.TameableUtils;
import com.github.alexthe668.domesticationinnovation.server.item.DIItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeRenderTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LayerPetOverlays extends RenderLayer {

    private static final int CLOUD_COUNT = 14;
    private static final Vec3[] CLOUD_OFFSETS = new Vec3[CLOUD_COUNT];
    private static final Vec3[] CLOUD_SCALES = new Vec3[CLOUD_COUNT];
    private static final BlazingBarModel BLAZING_BAR_MODEL = new BlazingBarModel();
    private static final ResourceLocation BLAZE_TEXTURE = new ResourceLocation("textures/entity/blaze.png");
    private static final ResourceLocation AURA_TEXTURE = new ResourceLocation(DomesticationMod.MODID, "textures/healing_aura.png");
    private static final Map<ResourceLocation, Integer> MODELS_TO_XSIZE = new HashMap<>();
    private static final Map<ResourceLocation, Integer> MODELS_TO_YSIZE = new HashMap<>();

    static {
        Random random = new Random(500);
        for (int i = 0; i < CLOUD_COUNT; i++) {
            CLOUD_OFFSETS[i] = new Vec3(random.nextFloat() - 0.5F, 0.2F * (random.nextFloat() - 0.5F), random.nextFloat() - 0.5F).scale(1.2F);
            CLOUD_SCALES[i] = new Vec3(0.6F + random.nextFloat() * 0.2F, 0.4F + random.nextFloat() * 0.2F, 0.4F + random.nextFloat() * 0.2F);
        }
    }

    private final RenderLayerParent parent;
    private final LightningRender lightningRender = new LightningRender();
    private final LightningBoltData.BoltRenderInfo healthBoltData = new LightningBoltData.BoltRenderInfo(0.3F, 0.0F, 0.0F, 0.0F, new Vector4f(0.4F, 0, 0, 0.4F), 0.2F);
    private final LightningBoltData.BoltRenderInfo shadowBoltData = new LightningBoltData.BoltRenderInfo(0.3F, 0.0F, 0.0F, 0.0F, new Vector4f(1F, 1F, 1F, 1F), 0.2F);

    public LayerPetOverlays(RenderLayerParent parent) {
        super(parent);
        this.parent = parent;
    }

    private static void addVertexPairAlex(VertexConsumer p_174308_, Matrix4f p_174309_, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_) {
        float f = (float) p_174321_ / 8.0F;
        int i = (int) Mth.lerp(f, (float) p_174313_, (float) p_174314_);
        int j = (int) Mth.lerp(f, (float) p_174315_, (float) p_174316_);
        int k = LightTexture.pack(i, j);
        float f2 = 0;
        float f3 = 0;
        float f4 = 0;
        float f5 = p_174310_ * f;
        float f6 = p_174311_ < 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float f7 = p_174312_ * f;
        p_174308_.vertex(p_174309_, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        p_174308_.vertex(p_174309_, f5 + p_174319_, f6 + p_174317_ - p_174318_, f7 - p_174320_).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }

    public static <E extends Entity> void renderShadowString(Entity from, Vec3 fromVec, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, Vec3 to) {
        poseStack.pushPose();
        double d3 = fromVec.x;
        double d4 = fromVec.y;
        double d5 = fromVec.z;
        poseStack.translate(d3, d4, d5);
        float f = (float) (to.x - d3);
        float f1 = (float) (to.y - d4);
        float f2 = (float) (to.z - d5);
        float f3 = 0.025F;
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.leash());
        Matrix4f matrix4f = poseStack.last().pose();
        BlockPos blockpos = new BlockPos(fromVec);
        BlockPos blockpos1 = new BlockPos(to);
        int i = 0;
        int j = from.level.getBrightness(LightLayer.BLOCK, blockpos1);
        int k = from.level.getBrightness(LightLayer.SKY, blockpos);
        int l = from.level.getBrightness(LightLayer.SKY, blockpos1);
        for (int i1 = 0; i1 <= 8; ++i1) {
            float width = 0.05F - (i1 / 8F) * 0.025F;
            addVertexPairAlex(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.2F, width, width, width, i1, false);
        }
        poseStack.popPose();
    }

    private static void vertex(VertexConsumer p_114090_, Matrix4f p_114091_, Matrix3f p_114092_, int p_114093_, float p_114094_, int p_114095_, int p_114096_, int p_114097_, float alpha) {
        p_114090_.vertex(p_114091_, p_114094_ - 0.5F, (float) p_114095_ - 0.5F, 0.0F).color(255, 255, 255, alpha * 255).uv((float) p_114096_, (float) p_114097_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_114093_).normal(p_114092_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (TameableUtils.couldBeTamed(entity)) {
            LivingEntity living = (LivingEntity) entity;
            float f = Mth.rotLerp(partialTicks, living.yBodyRotO, living.yBodyRot);
            float realAge = living.tickCount + partialTicks;
            if (TameableUtils.hasEnchant(living, DIEnchantmentRegistry.HEALTH_SIPHON)) {
                Entity owner = TameableUtils.getOwnerOf(living);
                if (owner != null && owner.isAlive() && owner.distanceTo(living) < 100) {
                    float x = (float) Mth.lerp(partialTicks, entity.xo, entity.getX());
                    float y = (float) Mth.lerp(partialTicks, entity.yo, entity.getY());
                    float z = (float) Mth.lerp(partialTicks, entity.zo, entity.getZ());
                    if (living.hurtTime > 0 && living.hurtTime == living.hurtDuration - 1) {
                        float height = -2 + entity.getBbHeight() * 0.8F;
                        float ownerHeight = -2 + owner.getBbHeight() * 0.6F;
                        LightningBoltData bolt = new LightningBoltData(healthBoltData, new Vec3(x, y + height, z), owner.position().add(0, ownerHeight, 0), 3)
                                .size(0.5F)
                                .lifespan(5)
                                .spawn(LightningBoltData.SpawnFunction.NO_DELAY);
                        lightningRender.update(living, bolt, partialTicks);
                    }
                    matrixStackIn.pushPose();
                    matrixStackIn.mulPose(Vector3f.YN.rotationDegrees(f));
                    matrixStackIn.mulPose(Vector3f.XN.rotationDegrees(180));
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(-x, -y, -z);
                    lightningRender.render(partialTicks, matrixStackIn, bufferIn);
                    matrixStackIn.popPose();
                    matrixStackIn.popPose();
                }
            }
            if (TameableUtils.isZombiePet(living)) {
                ResourceLocation mobTexture = getTextureLocation(living);
                Pair<Integer, Integer> xandyDimensions = TextureSizer.getTextureWidth(mobTexture);
                VertexConsumer zombieBuffer = bufferIn.getBuffer(DIRenderTypes.getZombieOverlay(mobTexture, xandyDimensions.getFirst(), xandyDimensions.getSecond()));
                float alpha = 0.1F;
                matrixStackIn.pushPose();
                this.getParentModel().renderToBuffer(matrixStackIn, zombieBuffer, packedLightIn, LivingEntityRenderer.getOverlayCoords((LivingEntity) entity, 0), 0, 0.5F, 0, alpha);
                matrixStackIn.popPose();
            }
            if (TameableUtils.hasEnchant(living, DIEnchantmentRegistry.BLAZING_PROTECTION)) {
                int bars = TameableUtils.getBlazingProtectionBars(living);
                float f1 = realAge * 7;
                float seperation = 360F / (TameableUtils.getEnchantLevel(living, DIEnchantmentRegistry.BLAZING_PROTECTION) * 2F);
                VertexConsumer vertexconsumer = bufferIn.getBuffer(ForgeRenderTypes.getUnlitTranslucent(BLAZE_TEXTURE));
                matrixStackIn.pushPose();
                matrixStackIn.mulPose(Vector3f.YN.rotationDegrees(f));
                for (int i = 0; i < bars; i++) {
                    f1 += seperation;
                    matrixStackIn.pushPose();
                    matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(f1));
                    float bob = (float) Math.sin(realAge * 0.6F + Math.toRadians(seperation * i)) * 0.15F - 0.07F;
                    matrixStackIn.translate(0, 0.4F - entity.getBbHeight() * 0.5F - bob, -entity.getBbWidth() - 0.2F);
                    BLAZING_BAR_MODEL.animateBar(f1);
                    BLAZING_BAR_MODEL.renderToBuffer(matrixStackIn, vertexconsumer, 240, LivingEntityRenderer.getOverlayCoords((LivingEntity) entity, 0), 1, 1, 1, 1);
                    matrixStackIn.popPose();
                }
                matrixStackIn.popPose();
            }
            if (TameableUtils.hasEnchant(living, DIEnchantmentRegistry.HEALING_AURA)) {
                int t = TameableUtils.getHealingAuraTime(living);
                if (t > 0) {
                    float time = t > 20 ? 200 - Math.max(180, t + partialTicks) : t - partialTicks;
                    float pulse = 0.9F + (float) (Math.sin(realAge * 0.08F) * 0.1F + 0.1F);
                    float healscale = (Math.min(time, 20) / 20F) * 2.2F * pulse;
                    matrixStackIn.pushPose();
                    matrixStackIn.translate(0, 1.8F - entity.getBbHeight() * 0.5F, 0);
                    matrixStackIn.mulPose(Vector3f.YN.rotationDegrees(f));
                    matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(realAge * 3F));
                    matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90F));
                    matrixStackIn.scale(3 * healscale, 3 * healscale, 3 * healscale);
                    VertexConsumer vertexconsumer = bufferIn.getBuffer(DIRenderTypes.HEALING_AURA);
                    PoseStack.Pose posestack$pose = matrixStackIn.last();
                    Matrix4f matrix4f = posestack$pose.pose();
                    Matrix3f matrix3f = posestack$pose.normal();
                    vertex(vertexconsumer, matrix4f, matrix3f, 240, 0.0F, 0, 0, 1, 1);
                    vertex(vertexconsumer, matrix4f, matrix3f, 240, 1.0F, 0, 1, 1, 1);
                    vertex(vertexconsumer, matrix4f, matrix3f, 240, 1.0F, 1, 1, 0, 1);
                    vertex(vertexconsumer, matrix4f, matrix3f, 240, 0.0F, 1, 0, 0, 1);
                    matrixStackIn.popPose();
                }
            }
        }
    }

}
