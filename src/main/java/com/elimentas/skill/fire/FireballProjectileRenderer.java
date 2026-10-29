package com.elimentas.skill.fire;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class FireballProjectileRenderer extends EntityRenderer<FireballProjectile> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/particle/flame.png");

    public FireballProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(FireballProjectile entity, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);

        poseStack.mulPose(entityRenderDispatcher.cameraOrientation());

        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

        consumer.addVertex(matrix, -0.5f, -0.5f, 0).setColor(255, 200, 100, 230)
                .setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880).setNormal(pose, 0, 1, 0);
        consumer.addVertex(matrix, 0.5f, -0.5f, 0).setColor(255, 200, 100, 230)
                .setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880).setNormal(pose, 0, 1, 0);
        consumer.addVertex(matrix, 0.5f, 0.5f, 0).setColor(255, 200, 100, 230)
                .setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880).setNormal(pose, 0, 1, 0);
        consumer.addVertex(matrix, -0.5f, 0.5f, 0).setColor(255, 200, 100, 230)
                .setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880).setNormal(pose, 0, 1, 0);

        poseStack.popPose();
        super.render(entity, yaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FireballProjectile entity) {
        return TEXTURE;
    }
}
